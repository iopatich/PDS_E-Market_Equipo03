import axios, { type AxiosError, type InternalAxiosRequestConfig } from 'axios';
import { authStorage } from '../auth/authStorage';

/**
 * Instancia única de Axios para centralizar la configuración del cliente HTTP.
 */
export const apiClient = axios.create({
  baseURL: '/api',
  headers: { 'Content-Type': 'application/json' },
  timeout: 30000,
});

apiClient.interceptors.request.use((config: InternalAxiosRequestConfig) => {
  const token = authStorage.getToken();
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

apiClient.interceptors.response.use(
  (response) => response,
  (error: AxiosError) => {
    if (error.response?.status === 401) {
      authStorage.clear();
      if (!window.location.pathname.includes('/ingresar')) {
        window.location.href = '/ingresar';
      }
    }
    return Promise.reject(error);
  },
);
