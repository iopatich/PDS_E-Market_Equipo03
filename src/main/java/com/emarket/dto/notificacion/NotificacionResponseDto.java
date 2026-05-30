package com.emarket.dto.notificacion;

import com.emarket.entity.CanalNotificacion;

import java.time.LocalDateTime;

public record NotificacionResponseDto(
        Long id,
        CanalNotificacion canal,
        String mensaje,
        LocalDateTime fechaEnvio
) {
}
