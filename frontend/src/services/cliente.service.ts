import { apiClient } from '../core/api/axiosInstance';
import type {
  ClienteRequestDto,
  ClienteResponseDto,
  ApiResponseDto,
} from '../core/types';

export const clienteService = {
  async registrar(dto: ClienteRequestDto): Promise<ClienteResponseDto> {
    const { data } = await apiClient.post<ClienteResponseDto>('/clientes/registro', dto);
    return data;
  },

  async listar(): Promise<ClienteResponseDto[]> {
    const { data } = await apiClient.get<ClienteResponseDto[]>('/clientes');
    return data;
  },

  async eliminar(id: number): Promise<ApiResponseDto<ClienteResponseDto>> {
    const { data } = await apiClient.delete<ApiResponseDto<ClienteResponseDto>>(
      `/clientes/eliminar/${id}`,
    );
    return data;
  },
};
