import { apiClient } from '../core/api/axiosInstance';
import type {
  AdministradorRequestDto,
  AdministradorResponseDto,
  ApiResponseDto,
  ClienteResponseDto,
} from '../core/types';

export const administradorService = {
  async registrar(dto: AdministradorRequestDto): Promise<AdministradorResponseDto> {
    const { data } = await apiClient.post<AdministradorResponseDto>(
      '/administradores/registro',
      dto,
    );
    return data;
  },

  async listar(): Promise<AdministradorResponseDto[]> {
    const { data } = await apiClient.get<AdministradorResponseDto[]>('/administradores');
    return data;
  },

  async eliminar(id: number): Promise<ApiResponseDto<AdministradorResponseDto>> {
    const { data } = await apiClient.delete<ApiResponseDto<AdministradorResponseDto>>(
      `/administradores/eliminar/${id}`,
    );
    return data;
  },

  async darDeBajaCliente(id: number): Promise<ApiResponseDto<ClienteResponseDto>> {
    const { data } = await apiClient.delete<ApiResponseDto<ClienteResponseDto>>(
      `/administradores/clientes/eliminar/${id}`,
    );
    return data;
  },
};
