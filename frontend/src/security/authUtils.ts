import axios from "axios";
import { API_URL } from "../settings";
import type { LoginRequest, RegisterRequest, AuthResponse, AuthUser } from "../types/types";

const AUTH_URL = `${API_URL}/auth`;

// Configure axios to include credentials (cookies) for refresh token
const authAxios = axios.create({
  baseURL: AUTH_URL,
  withCredentials: true,
});

export interface LoginResponse {
  token: string;
  expiresInSeconds: number;
}

export const authProvider = {
  async signIn(credentials: LoginRequest): Promise<LoginResponse> {
    const response = await authAxios.post<AuthResponse>("/login", credentials);
    const authData = response.data;

    return {
      token: authData.accessToken,
      expiresInSeconds: authData.expiresInSeconds,
    };
  },

  async register(userData: RegisterRequest): Promise<AuthUser> {
    const response = await authAxios.post<AuthUser>("/register", userData);
    return response.data;
  },

  async logout(token: string): Promise<void> {
    await authAxios.post("/logout", null, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
  },

  async getCurrentUser(token: string): Promise<AuthUser> {
    const response = await axios.get<AuthUser>(`${API_URL}/me`, {
      headers: {
        Authorization: `Bearer ${token}`,
      },
      withCredentials: true,
    });
    return response.data;
  },

  async refreshToken(): Promise<LoginResponse> {
    const response = await authAxios.post<AuthResponse>("/refresh");
    const authData = response.data;

    return {
      token: authData.accessToken,
      expiresInSeconds: authData.expiresInSeconds,
    };
  },
};
