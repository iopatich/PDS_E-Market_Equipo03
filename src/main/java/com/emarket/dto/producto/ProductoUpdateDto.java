package com.emarket.dto.producto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProductoUpdateDto(
        String nombre,

        String descripcion,

        @Positive(message = "El precio debe ser mayor a cero")
        Double precioBase,

        @Min(value = 1, message = "El id de categoria debe ser mayor o igual a 1")
        Long idCategoriaPadre
) {
}
