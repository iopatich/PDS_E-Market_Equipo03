import { apiClient } from '../core/api/axiosInstance';
import type {
  PedidoResponseDto,
  NotificacionResponseDto,
  ApiResponseDto,
} from '../core/types';

export const pedidoService = {
  async listarMios(): Promise<PedidoResponseDto[]> {
    const { data } = await apiClient.get<PedidoResponseDto[]>('/pedidos/mios');
    return data;
  },

  async listarTodos(): Promise<PedidoResponseDto[]> {
    const { data } = await apiClient.get<PedidoResponseDto[]>('/pedidos');
    return data;
  },

  async avanzarEstado(id: number): Promise<ApiResponseDto<PedidoResponseDto>> {
    const { data } = await apiClient.put<ApiResponseDto<PedidoResponseDto>>(
      `/pedidos/${id}/estado/siguiente`,
    );
    return data;
  },

  async listarNotificaciones(id: number): Promise<NotificacionResponseDto[]> {
    const { data } = await apiClient.get<NotificacionResponseDto[]>(
      `/pedidos/${id}/notificaciones`,
    );
    return data;
  },
};
