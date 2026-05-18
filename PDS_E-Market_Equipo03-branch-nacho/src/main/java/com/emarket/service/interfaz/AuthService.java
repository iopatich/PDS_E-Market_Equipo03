package com.emarket.service.interfaz;

import com.emarket.dto.auth.LoginRequestDto;
import com.emarket.dto.auth.LoginResponseDto;
import com.emarket.entity.Permiso;
import com.emarket.entity.Usuario;

public interface AuthService {
    LoginResponseDto login(LoginRequestDto dto);
    void logout(String token);
    Usuario obtenerUsuarioAutenticado(String token);
    Usuario validarPermiso(String token, Permiso permiso);
    Usuario validarEsAdministrador(String token);
}
