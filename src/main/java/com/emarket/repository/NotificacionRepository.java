package com.emarket.repository;

import com.emarket.entity.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    List<Notificacion> findByPedidoIdOrderByFechaEnvioAsc(Long pedidoId);
}
