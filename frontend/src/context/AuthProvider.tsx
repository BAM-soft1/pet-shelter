import { createContext, useContext, useState, useEffect } from "react";
import type { ReactNode } from "react";
import { authService } from "../security/authUtils";
import type { LoginRequest, RegisterRequest, AuthUser } from "../types/types";

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
    // Tokens are set as HttpOnly cookies by the backend
    await authService.signIn(credentials);

    // Fetch full user details (access token cookie is automatically sent)
    const userDetails = await authService.getCurrentUser();

    // Store user info (non-sensitive display data)
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
      // Backend reads tokens from cookies and clears them
      await authService.logout();
      console.log("User logged out successfully");
    } catch (error) {
      console.error("Failed to log out", error);
    }

    // Clear client-side state
    setUser(null);
    setUsername(null);
    localStorage.removeItem("user");
  };

  function isLoggedIn() {
    return user !== null;
  }

  function isLoggedInAs(roles: string[]) {
    if (!user) return false;
    return roles.includes(user.role);
  }

  useEffect(() => {
    const storedUser = localStorage.getItem("user");

    if (storedUser) {
      try {
        // Verify authentication by fetching current user (access token cookie is sent automatically)
        authService
          .getCurrentUser()
          .then((freshUser: AuthUser) => {
            setUser(freshUser);
            setUsername(`${freshUser.firstName} ${freshUser.lastName}`);
          })
          .catch(() => {
            // Access token is invalid or expired, clear everything
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
