package com.emarket.notificacion;

import com.emarket.entity.EstadoPedido;
import com.emarket.entity.Pedido;

public interface ObservadorPedido {
    void actualizar(Pedido pedido, EstadoPedido anterior, EstadoPedido nuevo, String mensaje);
}