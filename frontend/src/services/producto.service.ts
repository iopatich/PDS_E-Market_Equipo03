import { apiClient } from '../core/api/axiosInstance';
import type {
  ProductoRequestDto,
  ProductoResponseDto,
  ApiResponseDto,
} from '../core/types';

export const productoService = {
  async crear(dto: ProductoRequestDto): Promise<ProductoResponseDto> {
    const { data } = await apiClient.post<ProductoResponseDto>('/productos', dto);
    return data;
  },

  async listar(): Promise<ProductoResponseDto[]> {
    const { data } = await apiClient.get<ProductoResponseDto[]>('/productos');
    return data;
  },

  async eliminar(id: number): Promise<ApiResponseDto<ProductoResponseDto>> {
    const { data } = await apiClient.delete<ApiResponseDto<ProductoResponseDto>>(
      `/productos/eliminar/${id}`,
    );
    return data;
  },
};
