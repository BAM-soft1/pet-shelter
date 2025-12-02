import axios from "axios";
import { API_URL } from "../settings";
import getToken from "./authToken";

const axiosWithAuth = axios.create({
  baseURL: API_URL,
});

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

export default axiosWithAuth;
