package com.emarket.notificacion;

import com.emarket.entity.CanalNotificacion;
import com.emarket.entity.Notificacion;
import com.emarket.repository.NotificacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class NotificacionPedidoListener {

    private final NotificacionRepository notificacionRepository;

    @EventListener
    public void notificarPorEmail(PedidoEstadoActualizadoEvent event) {
        guardarNotificacion(event, CanalNotificacion.EMAIL);
    }

    @EventListener
    public void notificarPorSms(PedidoEstadoActualizadoEvent event) {
        guardarNotificacion(event, CanalNotificacion.SMS);
    }

    @EventListener
    public void notificarPorPush(PedidoEstadoActualizadoEvent event) {
        guardarNotificacion(event, CanalNotificacion.PUSH);
    }

    private void guardarNotificacion(PedidoEstadoActualizadoEvent event, CanalNotificacion canal) {
        Notificacion notificacion = new Notificacion();
        notificacion.setPedido(event.pedido());
        notificacion.setCanal(canal);
        notificacion.setMensaje(event.mensaje());
        notificacion.setFechaEnvio(LocalDateTime.now());
        notificacionRepository.save(notificacion);
    }
}
