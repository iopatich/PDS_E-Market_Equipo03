package com.emarket.dto.carrito;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AgregarItemCarritoRequestDto(
        @NotNull(message = "La variante es obligatoria")
        @Min(value = 1, message = "El id de variante debe ser mayor o igual a 1")
        Long idVarianteProducto,

        @NotNull(message = "La cantidad es obligatoria")
        @Min(value = 1, message = "La cantidad debe ser mayor o igual a 1")
        Integer cantidad
) {
}
