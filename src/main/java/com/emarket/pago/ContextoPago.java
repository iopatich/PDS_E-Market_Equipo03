package com.emarket.pago;

import com.emarket.entity.Pedido;
import com.emarket.exception.EstrategiaIndefinidaException;

public class ContextoPago {
    private MetodoPago estrategia;

    public void setEstrategia(MetodoPago estrategia) {
        this.estrategia = estrategia;
    }

    public void ejecutarPago(Pedido pedido) {
        if (this.estrategia == null) {
            throw new EstrategiaIndefinidaException("Estrategia de pago no definida");
        }
        this.estrategia.procesarPago(pedido);
    }
}