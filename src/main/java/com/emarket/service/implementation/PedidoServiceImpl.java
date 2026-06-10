package com.emarket.service.implementation;

import com.emarket.dto.notificacion.NotificacionResponseDto;
import com.emarket.dto.pedido.PedidoResponseDto;
import com.emarket.entity.*;
import com.emarket.exception.AccesoDenegadoException;
import com.emarket.exception.RecursoNoEncontradoException;
import com.emarket.mapper.NotificacionMapper;
import com.emarket.mapper.PedidoMapper;
import com.emarket.notificacion.*;
import com.emarket.repository.NotificacionRepository;
import com.emarket.repository.PedidoRepository;
import com.emarket.service.interfaz.AuthService;
import com.emarket.service.interfaz.PedidoService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService, SujetoPedido {

    private final PedidoRepository pedidoRepository;
    private final NotificacionRepository notificacionRepository;
    private final AuthService authService;

    // --- DEPENDENCIAS DE LOS OBSERVADORES ---
    private final EmailNotificacionObserver emailObserver;
    private final SmsNotificacionObserver smsObserver;
    private final PushNotificacionObserver pushObserver;

    // --- 1. ESTRUCTURA CLÁSICA DEL PATRÓN OBSERVER ---
    private List<ObservadorPedido> observadores = new ArrayList<>();

    // Simulamos que el sistema "suscribe" a los interesados al arrancar
    @PostConstruct
    public void inicializarSuscripciones() {
        adjuntar(emailObserver);
        adjuntar(smsObserver);
        adjuntar(pushObserver);
    }

    @Override
    public void adjuntar(ObservadorPedido observador) {
        if (!observadores.contains(observador)) {
            observadores.add(observador);
        }
    }

    @Override
    public void desvincular(ObservadorPedido observador) {
        observadores.remove(observador);
    }

    @Override
    public void notificar(Pedido pedido, EstadoPedido anterior, EstadoPedido nuevo, String mensaje) {
        // EL FAMOSO CICLO FOR QUE PIDE LA CÁTEDRA
        for (ObservadorPedido observador : observadores) {
            observador.actualizar(pedido, anterior, nuevo, mensaje);
        }
    }
    // --------------------------------------------------

    @Override
    @Transactional(readOnly = true)
    public List<PedidoResponseDto> listarMisPedidos(String token) {
        Usuario usuario = authService.validarPermiso(token, Permiso.REALIZAR_COMPRA);
        if (!(usuario instanceof Cliente cliente)) {
            throw new AccesoDenegadoException("Solo un cliente puede consultar sus propios pedidos");
        }
        return pedidoRepository.findByClienteOrderByFechaCreacionDesc(cliente).stream()
                .map(PedidoMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoResponseDto> listarPedidos(String token) {
        authService.validarPermiso(token, Permiso.ACTUALIZAR_ESTADO_PEDIDO);
        return pedidoRepository.findAllByOrderByFechaCreacionDesc().stream()
                .map(PedidoMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional
    public PedidoResponseDto avanzarEstado(Long pedidoId, String token) {
        authService.validarPermiso(token, Permiso.ACTUALIZAR_ESTADO_PEDIDO);
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontro el pedido con el id " + pedidoId));

        EstadoPedido anterior = pedido.getEstadoActual();

        // Ejecución del Patrón State que arreglamos antes
        pedido.avanzarEstado();

        EstadoPedido nuevo = pedido.getEstadoActual();
        Pedido actualizado = pedidoRepository.save(pedido);

        // --- 2. EJECUCIÓN DEL PATRÓN OBSERVER ---
        // En lugar de usar applicationEventPublisher, llamamos al método notificar()
        String mensajeAviso = "El pedido " + actualizado.getId() + " cambio de estado: " + anterior + " -> " + nuevo;
        notificar(actualizado, anterior, nuevo, mensajeAviso);

        return PedidoMapper.toResponseDto(actualizado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificacionResponseDto> listarNotificaciones(Long pedidoId, String token) {
        authService.validarPermiso(token, Permiso.ACTUALIZAR_ESTADO_PEDIDO);
        pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No se encontro el pedido con el id " + pedidoId));

        return notificacionRepository.findByPedidoIdOrderByFechaEnvioAsc(pedidoId).stream()
                .map(NotificacionMapper::toResponseDto)
                .toList();
    }
}