package com.emarket.dto.cliente;

import com.emarket.entity.Permiso;

import java.util.List;

public record ClienteResponseDto(
        Long id,
        String username,
        String email,
        List<Permiso> permisos
) {
}
