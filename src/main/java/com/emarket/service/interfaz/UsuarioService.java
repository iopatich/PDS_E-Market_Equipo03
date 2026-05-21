package com.emarket.service.interfaz;

import com.emarket.dto.usuario.UsuarioLoginRequestDto;
import com.emarket.dto.usuario.UsuarioLoginResponseDto;
import com.emarket.dto.usuario.UsuarioRegistroRequestDto;
import com.emarket.dto.usuario.UsuarioResponseDto;

public interface UsuarioService {
    UsuarioResponseDto registrar(UsuarioRegistroRequestDto dto);
    UsuarioLoginResponseDto login(UsuarioLoginRequestDto dto);
}
