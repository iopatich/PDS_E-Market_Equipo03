package com.emarket.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ErrorResponse(
        LocalDateTime fecha,
        int status,
        String mensaje,
        Map<String, String> errores

) {
    public ErrorResponse(int status, String mensaje) {
        this(LocalDateTime.now(), status, mensaje, null);
    }

    public ErrorResponse(int status, String mensaje, Map<String, String> errores) {
        this(LocalDateTime.now(), status, mensaje, errores);
    }
}
