import { apiClient } from '../core/api/axiosInstance';
import type {
  LoginRequestDto,
  LoginResponseDto,
  PerfilResponse,
} from '../core/types';

/**
 * Servicio de autenticación: login, logout y perfil.
 */
export const authService = {
  async login(dto: LoginRequestDto): Promise<LoginResponseDto> {
    const { data } = await apiClient.post<LoginResponseDto>('/auth/login', dto);
    return data;
  },

  async logout(): Promise<void> {
    await apiClient.post('/auth/logout');
  },

  async getPerfil(): Promise<PerfilResponse> {
    const { data } = await apiClient.get<PerfilResponse>('/auth/perfil');
    return data;
  },
};
