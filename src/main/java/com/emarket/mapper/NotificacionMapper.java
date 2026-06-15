package com.emarket.mapper;

import com.emarket.dto.notificacion.NotificacionResponseDto;
import com.emarket.entity.Notificacion;

public class NotificacionMapper {

    private NotificacionMapper() {
    }

    public static NotificacionResponseDto toResponseDto(Notificacion notificacion) {
        return new NotificacionResponseDto(
                notificacion.getId(),
                notificacion.getCanal(),
                notificacion.getMensaje(),
                notificacion.getFechaEnvio()
        );
    }
}
