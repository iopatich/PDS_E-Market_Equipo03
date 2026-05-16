package com.emarket.mapper;

import com.emarket.dto.categoria.CategoriaRequestDto;
import com.emarket.dto.categoria.CategoriaResponseDto;
import com.emarket.entity.Categoria;

public class CategoriaMapper {
    private CategoriaMapper() {

    }

    public static Categoria toEntity(CategoriaRequestDto dto, Categoria categoriaPadre) {
        Categoria categoria = new Categoria();
        categoria.setNombre(dto.nombre());
        categoria.setCategoriaPadre(categoriaPadre);

        return categoria;
    }

    public static CategoriaResponseDto toResponseDto(Categoria categoria) {
        return new CategoriaResponseDto(
                categoria.getId(),
                categoria.getNombre(),
                categoria.getCategoriaPadre() != null ? categoria.getCategoriaPadre().getNombre() : null
        );
    }
}
