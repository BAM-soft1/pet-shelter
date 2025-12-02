import { createContext, useContext, useState, useEffect } from "react";
import type { ReactNode } from "react";
import { authProvider } from "../security/authUtils";
import type { LoginRequest, RegisterRequest, AuthUser } from "../types/types";
import getToken from "../security/authToken";

interface AuthContextType {
  user: AuthUser | null;
  username: string | null;
  signIn: (credentials: LoginRequest) => Promise<void>;
  signUp: (userData: RegisterRequest) => Promise<void>;
  signOut: () => Promise<void>;
  isLoggedIn: () => boolean;
  isLoggedInAs: (roles: string[]) => boolean;
}

const AuthContext = createContext<AuthContextType | null>(null);

let logoutTimer: ReturnType<typeof setTimeout> | null = null;

export default function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<AuthUser | null>(() => {
    const storedUser = localStorage.getItem("user");
    return storedUser ? JSON.parse(storedUser) : null;
  });
  const [username, setUsername] = useState<string | null>(user ? `${user.firstName} ${user.lastName}` : null);

  const scheduleLogout = (_token: string, expiresInSeconds: number) => {
    if (logoutTimer) {
      clearTimeout(logoutTimer);
    }

    // Schedule logout 30 seconds before token expires to be safe
    const timeout = (expiresInSeconds - 30) * 1000;

    logoutTimer = setTimeout(
      () => {
        signOut();
      },
      timeout > 0 ? timeout : 0
    );
  };

  const signIn = async (credentials: LoginRequest) => {
    const response = await authProvider.signIn(credentials);

    // Store token and expiry time
    localStorage.setItem("token", response.token);
    const expiresAt = Date.now() + response.expiresInSeconds * 1000; // Calculate absolute expiry timestamp
    localStorage.setItem("tokenExpiresAt", expiresAt.toString());

    // Fetch full user details
    const userDetails = await authProvider.getCurrentUser(response.token);

    // Store user info
    setUser(userDetails);
    setUsername(`${userDetails.firstName} ${userDetails.lastName}`);
    localStorage.setItem("user", JSON.stringify(userDetails));

    // Schedule logout based on token expiration
    scheduleLogout(response.token, response.expiresInSeconds);
  };

  const signUp = async (userData: RegisterRequest) => {
    // Register the user
    await authProvider.register(userData);

    // Auto-login after registration
    await signIn({ email: userData.email, password: userData.password });
  };

  const signOut = async () => {
    if (logoutTimer) {
      clearTimeout(logoutTimer);
    }

    try {
      const token = getToken();
      if (token) {
        await authProvider.logout(token);
      }
      console.log("User logged out successfully");
    } catch (error) {
      console.error("Failed to log out", error);
    }

    // Clear client-side state
    setUser(null);
    setUsername(null);
    localStorage.removeItem("token");
    localStorage.removeItem("user");
    localStorage.removeItem("tokenExpiresAt");
  };

  function isLoggedIn() {
    return user !== null;
  }

  function isLoggedInAs(roles: string[]) {
    if (!user) return false;
    return roles.includes(user.role);
  }

  useEffect(() => {
    const token = getToken();
    const storedUser = localStorage.getItem("user");
    const tokenExpiresAt = localStorage.getItem("tokenExpiresAt");

    if (token && storedUser) {
      try {
        // Check if token is already expired
        if (tokenExpiresAt) {
          const expiryTime = parseInt(tokenExpiresAt);
          const now = Date.now();
          
          if (now >= expiryTime) {
            // Token already expired, clear everything
            signOut();
            return;
          }
        }

        // Verify token by fetching current user
        authProvider
          .getCurrentUser(token)
          .then((freshUser: AuthUser) => {
            setUser(freshUser);
            setUsername(`${freshUser.firstName} ${freshUser.lastName}`);

            // Calculate remaining time until expiry
            if (tokenExpiresAt) {
              const expiryTime = parseInt(tokenExpiresAt);
              const now = Date.now();
              const remainingSeconds = Math.floor((expiryTime - now) / 1000);
              scheduleLogout(token, remainingSeconds);
            } else {
              // Fallback if expiry time not stored
              scheduleLogout(token, 900);
            }
          })
          .catch(() => {
            // Token is invalid, clear everything
            signOut();
          });
      } catch {
        // Invalid stored data
        signOut();
      }
    }
  }, []); // eslint-disable-line react-hooks/exhaustive-deps

  const value = { user, username, isLoggedIn, isLoggedInAs, signIn, signUp, signOut };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  return useContext(AuthContext);
}
