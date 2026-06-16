package com.emarket.dto.varianteProducto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record VarianteProductoRequestDto(
        @NotBlank(message = "El color es obligatorio")
        String color,

        @NotNull(message = "El stock es obligatorio")
        @Min(value = 0, message = "El stock debe ser mayor o igual a cero")
        Integer stock,

        @NotNull(message = "El precio es obligatorio")
        @PositiveOrZero(message = "El precio adicional debe ser mayor o igual a cero")
        Double precio,

        @NotNull(message = "El id de producto es obligatorio")
        @Min(value = 1, message = "El id de producto debe ser mayor o igual a 1")
        Long idProducto
) {
}