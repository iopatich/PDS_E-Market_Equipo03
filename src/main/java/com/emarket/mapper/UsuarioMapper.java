package com.emarket.mapper;

import com.emarket.dto.usuario.UsuarioLoginResponseDto;
import com.emarket.dto.usuario.UsuarioRegistroRequestDto;
import com.emarket.dto.usuario.UsuarioResponseDto;
import com.emarket.entity.Usuario;

public class UsuarioMapper {

    private UsuarioMapper() {
    }

    public static Usuario toEntity(UsuarioRegistroRequestDto dto, String passwordEncriptada) {
        Usuario usuario = new Usuario();
        usuario.setUsername(dto.username());
        usuario.setEmail(dto.email());
        usuario.setPassword(passwordEncriptada);
        usuario.setRol(dto.rol());
        return usuario;
    }

    public static UsuarioResponseDto toResponseDto(Usuario usuario) {
        return new UsuarioResponseDto(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getEmail(),
                usuario.getRol()
        );
    }

    public static UsuarioLoginResponseDto toLoginResponseDto(Usuario usuario) {
        return new UsuarioLoginResponseDto(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getRol(),
                "Login exitoso"
        );
    }
}
