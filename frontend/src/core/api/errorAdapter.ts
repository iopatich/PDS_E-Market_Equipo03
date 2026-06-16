import type { AxiosError } from 'axios';
import type { AppError, ErrorResponse } from '../types';

/**
 * Convierte la respuesta de error del backend al formato usado en el frontend.
 */
export function adaptApiError(error: unknown): AppError {
  const axiosError = error as AxiosError<ErrorResponse>;

  if (axiosError.response?.data) {
    const { status, mensaje, errores } = axiosError.response.data;
    return {
      status: status ?? axiosError.response.status,
      message: mensaje ?? 'Error inesperado',
      fieldErrors: errores,
    };
  }

  if (axiosError.message === 'Network Error') {
    return { status: 0, message: 'No se pudo conectar con el servidor' };
  }

  return { status: 500, message: 'Ocurrió un error en la aplicación' };
}

export function getErrorMessage(error: unknown): string {
  return adaptApiError(error).message;
}
