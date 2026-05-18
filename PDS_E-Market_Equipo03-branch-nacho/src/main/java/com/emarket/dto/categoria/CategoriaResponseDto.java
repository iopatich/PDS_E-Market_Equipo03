package com.emarket.dto.categoria;

public record CategoriaResponseDto(
        Long id,
        String nombre,
        String CategoriaPadre
) {
}
