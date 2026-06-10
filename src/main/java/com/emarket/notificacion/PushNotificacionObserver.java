package com.emarket.notificacion;

import com.emarket.entity.CanalNotificacion;
import com.emarket.entity.EstadoPedido;
import com.emarket.entity.Notificacion;
import com.emarket.entity.Pedido;
import com.emarket.repository.NotificacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Observer;

@Component //
@RequiredArgsConstructor //
public class PushNotificacionObserver implements ObservadorPedido {
    private final NotificacionRepository notificacionRepository;

    @Override
    public void actualizar(Pedido pedido, EstadoPedido anterior, EstadoPedido nuevo, String mensaje) {
        Notificacion notificacion = new Notificacion();
        notificacion.setPedido(pedido);
        notificacion.setCanal(CanalNotificacion.PUSH);
        notificacion.setMensaje(mensaje);
        notificacion.setFechaEnvio(LocalDateTime.now());
        notificacionRepository.save(notificacion);
    }


}
