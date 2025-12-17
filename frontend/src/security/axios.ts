import axios from "axios";
import type { AxiosError, InternalAxiosRequestConfig } from "axios";
import { API_URL } from "../settings";
import { authService } from "./authUtils";

const axiosWithAuth = axios.create({
  baseURL: API_URL,
  withCredentials: true, // Important: sends cookies (access_token and refresh_token) with requests
});

let isRefreshing = false;
let failedQueue: Array<{
  resolve: (value?: unknown) => void;
  reject: (error: unknown) => void;
}> = [];

const processQueue = (error: unknown) => {
  failedQueue.forEach((prom) => {
    if (error) {
      prom.reject(error);
    } else {
      prom.resolve();
    }
  });

  failedQueue = [];
};

// Response interceptor for handling 401 errors and refreshing tokens
axiosWithAuth.interceptors.response.use(
  (response) => response,
  async (error: AxiosError) => {
    const originalRequest = error.config as InternalAxiosRequestConfig & {
      _retry?: boolean;
    };

    // If error is 401 and we haven't tried to refresh yet
    if (error.response?.status === 401 && !originalRequest._retry) {
      if (isRefreshing) {
        // If already refreshing, queue this request
        return new Promise((resolve, reject) => {
          failedQueue.push({ resolve, reject });
        })
          .then(() => {
            // Retry with the new access token cookie set by refresh endpoint
            return axiosWithAuth(originalRequest);
          })
          .catch((err) => {
            return Promise.reject(err);
          });
      }

      originalRequest._retry = true;
      isRefreshing = true;

      try {
        // Try to refresh the token - new cookies are set by the backend
        await authService.refreshToken();

        // Process all queued requests - they'll use the new access token cookie
        processQueue(null);

        // Retry the original request with new access token cookie
        return axiosWithAuth(originalRequest);
      } catch (refreshError) {
        // Refresh failed - clear auth state and redirect to login
        processQueue(refreshError);
        localStorage.removeItem("user");

        // Redirect to login page
        window.location.href = "/login";

        return Promise.reject(refreshError);
      } finally {
        isRefreshing = false;
      }
    }

    return Promise.reject(error);
  }
);

export default axiosWithAuth;
