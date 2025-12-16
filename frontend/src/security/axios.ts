import axios from "axios";
import type { AxiosError, InternalAxiosRequestConfig } from "axios";
import { API_URL } from "../settings";
import getToken from "./authToken";
import { authProvider } from "./authUtils";

const axiosWithAuth = axios.create({
  baseURL: API_URL,
  withCredentials: true, // Important: sends cookies with requests
});

let isRefreshing = false;
let failedQueue: Array<{
  resolve: (token: string) => void;
  reject: (error: unknown) => void;
}> = [];

const processQueue = (error: unknown, token: string | null = null) => {
  failedQueue.forEach((prom) => {
    if (error) {
      prom.reject(error);
    } else if (token) {
      prom.resolve(token);
    }
  });

  failedQueue = [];
};

// Interceptor that will run before every request made using axios
axiosWithAuth.interceptors.request.use(
  (config) => {
    const token = getToken();
    // If a token exists, add it to the req headers as a Bearer (for making authenticated reqs to API)
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) =>
    // in case of error while setting up the request, reject the promise
    Promise.reject(error)
);

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
          .then((token) => {
            originalRequest.headers.Authorization = `Bearer ${token}`;
            return axiosWithAuth(originalRequest);
          })
          .catch((err) => {
            return Promise.reject(err);
          });
      }

      originalRequest._retry = true;
      isRefreshing = true;

      try {
        // Try to refresh the token
        const { token: newAccessToken, expiresInSeconds } = await authProvider.refreshToken();

        // Store new token
        localStorage.setItem("token", newAccessToken);
        const expiresAt = Date.now() + expiresInSeconds * 1000;
        localStorage.setItem("tokenExpiresAt", expiresAt.toString());

        // Update default authorization header
        axiosWithAuth.defaults.headers.common.Authorization = `Bearer ${newAccessToken}`;

        // Process all queued requests with new token
        processQueue(null, newAccessToken);

        // Retry the original request with new token
        originalRequest.headers.Authorization = `Bearer ${newAccessToken}`;
        return axiosWithAuth(originalRequest);
      } catch (refreshError) {
        // Refresh failed - clear auth state and redirect to login
        processQueue(refreshError, null);
        localStorage.removeItem("token");
        localStorage.removeItem("user");
        localStorage.removeItem("tokenExpiresAt");

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
