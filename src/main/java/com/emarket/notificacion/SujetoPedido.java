package com.emarket.notificacion;


import com.emarket.entity.EstadoPedido;
import com.emarket.entity.Pedido;

public interface SujetoPedido {
    void adjuntar(ObservadorPedido observador);
    void desvincular(ObservadorPedido observador);
    void notificar(Pedido pedido, EstadoPedido anterior, EstadoPedido nuevo, String mensaje);
}