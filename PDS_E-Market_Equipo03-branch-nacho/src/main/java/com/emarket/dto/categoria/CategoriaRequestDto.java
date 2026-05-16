package com.emarket.dto.categoria;

import jakarta.validation.constraints.NotBlank;

public record CategoriaRequestDto(
        @NotBlank(message = "El nombre es obligatorio")
        String nombre,
        Long idCategoriaPadre
) {
}
