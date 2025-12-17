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
  async (error: AxiosError<{ message?: string; status?: number; error?: string }>) => {
    const originalRequest = error.config as InternalAxiosRequestConfig & {
      _retry?: boolean;
    };

    // Enhanced error message
    const enhanceError = (err: AxiosError<{ message?: string; status?: number; error?: string }>) => {
      if (err.response?.data?.message) {
        err.message = err.response.data.message;
      } else if (err.response?.data?.error) {
        err.message = err.response.data.error;
      } else if (err.response?.statusText) {
        err.message = `${err.response.status}: ${err.response.statusText}`;
      }
      return err;
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
            return Promise.reject(enhanceError(err));
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

        console.error("Session expired. Redirecting to login...");

        // Redirect to login page
        window.location.href = "/login";

        return Promise.reject(enhanceError(refreshError as AxiosError<{ message?: string; status?: number; error?: string }>));
      } finally {
        isRefreshing = false;
      }
    }

    // For other errors, enhance the error message
    return Promise.reject(enhanceError(error));
  }
);

export default axiosWithAuth;
