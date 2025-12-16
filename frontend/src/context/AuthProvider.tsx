import { createContext, useContext, useState, useEffect } from "react";
import type { ReactNode } from "react";
import { authService } from "../security/authUtils";
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

export default function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<AuthUser | null>(() => {
    const storedUser = localStorage.getItem("user");
    return storedUser ? JSON.parse(storedUser) : null;
  });
  const [username, setUsername] = useState<string | null>(user ? `${user.firstName} ${user.lastName}` : null);

  const signIn = async (credentials: LoginRequest) => {
    const response = await authService.signIn(credentials);

    // Store token and expiry time
    localStorage.setItem("token", response.token);
    const expiresAt = Date.now() + response.expiresInSeconds * 1000; // Calculate absolute expiry timestamp
    localStorage.setItem("tokenExpiresAt", expiresAt.toString());

    // Fetch full user details
    const userDetails = await authService.getCurrentUser(response.token);

    // Store user info
    setUser(userDetails);
    setUsername(`${userDetails.firstName} ${userDetails.lastName}`);
    localStorage.setItem("user", JSON.stringify(userDetails));
  };

  const signUp = async (userData: RegisterRequest) => {
    // Register the user
    await authService.register(userData);

    // Auto-login after registration
    await signIn({ email: userData.email, password: userData.password });
  };

  const signOut = async () => {
    try {
      const token = getToken();
      if (token) {
        await authService.logout(token);
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

    if (token && storedUser) {
      try {
        // Verify token by fetching current user
        authService
          .getCurrentUser(token)
          .then((freshUser: AuthUser) => {
            setUser(freshUser);
            setUsername(`${freshUser.firstName} ${freshUser.lastName}`);
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
  }, []);

  const value = { user, username, isLoggedIn, isLoggedInAs, signIn, signUp, signOut };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  return useContext(AuthContext);
}
