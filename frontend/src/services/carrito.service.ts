import { apiClient } from '../core/api/axiosInstance';
import type {
  AgregarItemCarritoRequestDto,
  ActualizarCantidadCarritoRequestDto,
  CarritoResponseDto,
  ConfirmarCompraRequestDto,
  ApiResponseDto,
  PedidoResponseDto,
} from '../core/types';

export const carritoService = {
  async ver(): Promise<CarritoResponseDto> {
    const { data } = await apiClient.get<CarritoResponseDto>('/carrito');
    return data;
  },

  async agregarItem(dto: AgregarItemCarritoRequestDto): Promise<CarritoResponseDto> {
    const { data } = await apiClient.post<CarritoResponseDto>('/carrito/items', dto);
    return data;
  },

  async actualizarCantidad(
    itemId: number,
    dto: ActualizarCantidadCarritoRequestDto,
  ): Promise<CarritoResponseDto> {
    const { data } = await apiClient.put<CarritoResponseDto>(`/carrito/items/${itemId}`, dto);
    return data;
  },

  async eliminarItem(itemId: number): Promise<ApiResponseDto<CarritoResponseDto>> {
    const { data } = await apiClient.delete<ApiResponseDto<CarritoResponseDto>>(
      `/carrito/items/${itemId}`,
    );
    return data;
  },

  async confirmarCompra(
    dto: ConfirmarCompraRequestDto,
  ): Promise<ApiResponseDto<PedidoResponseDto>> {
    const { data } = await apiClient.post<ApiResponseDto<PedidoResponseDto>>(
      '/carrito/confirmar',
      dto,
    );
    return data;
  },
};
