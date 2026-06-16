import { apiClient } from '../core/api/axiosInstance';
import type {
  VarianteProductoRequestDto,
  VarianteProductoResponseDto,
  ApiResponseDto,
} from '../core/types';

export const varianteService = {
  async crear(dto: VarianteProductoRequestDto): Promise<VarianteProductoResponseDto> {
    const { data } = await apiClient.post<VarianteProductoResponseDto>(
      '/variantesproducto',
      dto,
    );
    return data;
  },

  async listar(): Promise<VarianteProductoResponseDto[]> {
    const { data } = await apiClient.get<VarianteProductoResponseDto[]>('/variantesproducto');
    return data;
  },

  async reducirStock(
    id: number,
    cantidad: number,
  ): Promise<ApiResponseDto<VarianteProductoResponseDto>> {
    const { data } = await apiClient.put<ApiResponseDto<VarianteProductoResponseDto>>(
      `/variantesproducto/reducirstock/${id}`,
      null,
      { params: { cantidad } },
    );
    return data;
  },

  async eliminar(id: number): Promise<ApiResponseDto<VarianteProductoResponseDto>> {
    const { data } = await apiClient.delete<ApiResponseDto<VarianteProductoResponseDto>>(
      `/variantesproducto/eliminar/${id}`,
    );
    return data;
  },
};
