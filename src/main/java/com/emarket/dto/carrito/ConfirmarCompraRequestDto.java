package com.emarket.dto.carrito;

import com.emarket.pago.TipoPago;
import jakarta.validation.constraints.NotNull;

public record ConfirmarCompraRequestDto(
        @NotNull(message = "El tipo de pago es obligatorio")
        TipoPago tipoPago
) {
}
