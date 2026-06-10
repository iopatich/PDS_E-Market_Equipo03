package com.emarket.pago;

import com.emarket.entity.Pedido;

public interface MetodoPago {
    void procesarPago(Pedido pedido);
    TipoPago getTipo();
}
