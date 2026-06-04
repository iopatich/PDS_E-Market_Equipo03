package com.emarket.service.implementation;

import com.emarket.auth.EstrategiaAutenticacion;
import com.emarket.dto.auth.LoginRequestDto;
import com.emarket.dto.auth.LoginResponseDto;
import com.emarket.entity.Administrador;
import com.emarket.entity.Cliente;
import com.emarket.exception.AccesoDenegadoException;
import com.emarket.entity.Permiso;
import com.emarket.entity.Sesion;
import com.emarket.entity.Usuario;
import com.emarket.exception.SesionInvalidaException;
import com.emarket.mapper.AdministradorMapper;
import com.emarket.mapper.ClienteMapper;
import com.emarket.repository.SesionRepository;
import com.emarket.service.interfaz.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final int HORAS_VALIDEZ_SESION = 24;

    private final EstrategiaAutenticacion estrategiaAutenticacion;
    private final SesionRepository sesionRepository;

    @Override
    @Transactional
    public LoginResponseDto login(LoginRequestDto dto) {
        Usuario usuario = estrategiaAutenticacion.autenticar(dto.username(), dto.password());
        Sesion sesion = crearSesion(usuario);
        return toLoginResponseDto(sesion);
    }

    @Override
    @Transactional
    public void logout(String token) {
        Sesion sesion = obtenerSesionVigente(extraerToken(token));
        sesion.setActiva(false);
        sesionRepository.save(sesion);
    }

    @Override
    public Usuario obtenerUsuarioAutenticado(String token) {
        return obtenerSesionVigente(extraerToken(token)).getUsuario();
    }

    @Override
    public Usuario validarPermiso(String token, Permiso permiso) {
        Usuario usuario = obtenerUsuarioAutenticado(token);
        if (!usuario.tienePermiso(permiso)) {
            throw new AccesoDenegadoException("No tiene permiso para realizar esta operación");
        }
        return usuario;
    }

    @Override
    public Usuario validarEsAdministrador(String token) {
        Usuario usuario = obtenerUsuarioAutenticado(token);
        if (!(usuario instanceof Administrador)) {
            throw new AccesoDenegadoException("Solo un administrador puede realizar esta operación");
        }
        return usuario;
    }

    private Sesion crearSesion(Usuario usuario) {
        String token = UUID.randomUUID().toString();
        Sesion sesion = new Sesion(
                token,
                usuario,
                LocalDateTime.now().plusHours(HORAS_VALIDEZ_SESION)
        );
        return sesionRepository.save(sesion);
    }

    private Sesion obtenerSesionVigente(String token) {
        Sesion sesion = sesionRepository.findByTokenAndActivaTrue(token)
                .orElseThrow(() -> new SesionInvalidaException("Sesión inválida o expirada"));

        if (!sesion.estaVigente()) {
            sesion.setActiva(false);
            sesionRepository.save(sesion);
            throw new SesionInvalidaException("Sesión inválida o expirada");
        }

        return sesion;
    }

    private String extraerToken(String authorizationHeader) {
        if (authorizationHeader == null || authorizationHeader.isBlank()) {
            throw new SesionInvalidaException("Token de autenticación requerido");
        }
        if (authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7).trim();
        }
        return authorizationHeader.trim();
    }

    private LoginResponseDto toLoginResponseDto(Sesion sesion) {
        Usuario usuario = sesion.getUsuario();
        if (usuario instanceof Cliente cliente) {
            sesion.setUsuario(cliente);
            return ClienteMapper.toLoginResponseDto(sesion);
        }
        if (usuario instanceof Administrador administrador) {
            sesion.setUsuario(administrador);
            return AdministradorMapper.toLoginResponseDto(sesion);
        }
        throw new IllegalStateException("Tipo de usuario no soportado");
    }
}
