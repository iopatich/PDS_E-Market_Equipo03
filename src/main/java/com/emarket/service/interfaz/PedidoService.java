package com.emarket.service.interfaz;

import com.emarket.dto.notificacion.NotificacionResponseDto;
import com.emarket.dto.pedido.PedidoResponseDto;
import com.emarket.entity.EstadoPedido;

import java.util.List;

public interface PedidoService {
    List<PedidoResponseDto> listarMisPedidos(String token);
    List<PedidoResponseDto> listarPedidos(String token, EstadoPedido estado);
    PedidoResponseDto avanzarEstado(Long pedidoId, String token);
    List<NotificacionResponseDto> listarNotificaciones(Long pedidoId, String token);
}