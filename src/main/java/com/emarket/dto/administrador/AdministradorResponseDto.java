package com.emarket.dto.administrador;

import com.emarket.entity.Permiso;

import java.util.List;

public record AdministradorResponseDto(
        Long id,
        String username,
        List<Permiso> permisos
) {
}
