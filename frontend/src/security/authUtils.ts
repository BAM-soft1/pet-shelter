import axios from "axios";
import { API_URL } from "../settings";
import type { LoginRequest, RegisterRequest, AuthUser } from "../types/types";

const AUTH_URL = `${API_URL}/auth`;

// Configure axios to include credentials (cookies) - both access and refresh tokens are now in cookies
const authAxios = axios.create({
  baseURL: AUTH_URL,
  withCredentials: true,
});

export const authService = {
  async signIn(credentials: LoginRequest): Promise<void> {
    // Tokens are set as HttpOnly cookies by the backend
    await authAxios.post("/login", credentials);
  },

  async register(userData: RegisterRequest): Promise<AuthUser> {
    const response = await authAxios.post<AuthUser>("/register", userData);
    return response.data;
  },

  async logout(): Promise<void> {
    // Backend reads tokens from cookies
    await authAxios.post("/logout");
  },

  async getCurrentUser(): Promise<AuthUser> {
    // Backend reads access token from cookie
    const response = await axios.get<AuthUser>(`${API_URL}/me`, {
      withCredentials: true,
    });
    return response.data;
  },

  async refreshToken(): Promise<void> {
    // Backend reads refresh token from cookie and sets new access token cookie
    await authAxios.post("/refresh");
  },
};
