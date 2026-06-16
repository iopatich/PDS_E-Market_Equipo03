import { apiClient } from '../core/api/axiosInstance';
import type {
  CategoriaRequestDto,
  CategoriaResponseDto,
  ApiResponseDto,
} from '../core/types';

export const categoriaService = {
  async crear(dto: CategoriaRequestDto): Promise<CategoriaResponseDto> {
    const { data } = await apiClient.post<CategoriaResponseDto>('/categorias', dto);
    return data;
  },

  async listar(): Promise<CategoriaResponseDto[]> {
    const { data } = await apiClient.get<CategoriaResponseDto[]>('/categorias');
    return data;
  },

  async eliminar(id: number): Promise<ApiResponseDto<CategoriaResponseDto>> {
    const { data } = await apiClient.delete<ApiResponseDto<CategoriaResponseDto>>(
      `/categorias/eliminar/${id}`,
    );
    return data;
  },
};
