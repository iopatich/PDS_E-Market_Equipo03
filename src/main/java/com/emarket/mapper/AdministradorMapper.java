package com.emarket.mapper;

import com.emarket.dto.administrador.AdministradorRequestDto;
import com.emarket.dto.administrador.AdministradorResponseDto;
import com.emarket.dto.auth.LoginResponseDto;
import com.emarket.entity.Administrador;
import com.emarket.entity.Sesion;

public class AdministradorMapper {
    private AdministradorMapper() {
    }

    public static Administrador toEntity(AdministradorRequestDto dto) {
        Administrador administrador = new Administrador();
        administrador.setUsername(dto.username());
        return administrador;
    }

    public static AdministradorResponseDto toResponseDto(Administrador administrador) {
        return new AdministradorResponseDto(
                administrador.getId(),
                administrador.getUsername(),
                administrador.getPermisos()
        );
    }

    public static LoginResponseDto toLoginResponseDto(Sesion sesion) {
        Administrador administrador = (Administrador) sesion.getUsuario();
        return new LoginResponseDto(
                sesion.getToken(),
                administrador.getTipoUsuario(),
                administrador.getId(),
                administrador.getUsername(),
                administrador.getPermisos()
        );
    }
}
