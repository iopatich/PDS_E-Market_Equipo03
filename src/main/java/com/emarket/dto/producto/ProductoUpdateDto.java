package com.emarket.dto.producto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

public record ProductoUpdateDto(
        String nombre,

        @Positive(message = "El precio debe ser mayor a cero")
        Double precio,

        @Min(value = 0, message = "El stock debe ser mayor o igual a cero")
        Integer stock,

        Color color,

        @Min(value = 1, message = "El id de categoria debe ser mayor o igual a 1")
        Long categoria
) {
}
