package com.emarket.service.implementation;

import com.emarket.dto.usuario.UsuarioLoginRequestDto;
import com.emarket.dto.usuario.UsuarioRegistroRequestDto;
import com.emarket.entity.RolUsuario;
import com.emarket.entity.Usuario;
import com.emarket.exception.CredencialesInvalidasException;
import com.emarket.exception.DuplicadoException;
import com.emarket.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    private PasswordEncoder passwordEncoder;
    private UsuarioServiceImpl usuarioService;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        usuarioService = new UsuarioServiceImpl(usuarioRepository, passwordEncoder);
    }

    @Test
    void registrarDeberiaGuardarUsuarioConPasswordEncriptada() {
        UsuarioRegistroRequestDto dto = new UsuarioRegistroRequestDto(
                "cliente1",
                "cliente1@test.com",
                "secret123",
                RolUsuario.CLIENTE
        );

        when(usuarioRepository.existsByUsernameIgnoreCase(dto.username())).thenReturn(false);
        when(usuarioRepository.existsByEmailIgnoreCase(dto.email())).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario usuario = invocation.getArgument(0);
            usuario.setId(1L);
            return usuario;
        });

        var response = usuarioService.registrar(dto);

        assertEquals("cliente1", response.username());
        assertEquals(RolUsuario.CLIENTE, response.rol());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void registrarDeberiaFallarSiUsernameYaExiste() {
        UsuarioRegistroRequestDto dto = new UsuarioRegistroRequestDto(
                "admin1",
                "admin1@test.com",
                "secret123",
                RolUsuario.ADMINISTRADOR
        );

        when(usuarioRepository.existsByUsernameIgnoreCase(dto.username())).thenReturn(true);

        assertThrows(DuplicadoException.class, () -> usuarioService.registrar(dto));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void loginDeberiaRetornarRolCuandoLasCredencialesSonValidas() {
        Usuario usuario = new Usuario();
        usuario.setId(7L);
        usuario.setUsername("cliente1");
        usuario.setPassword(passwordEncoder.encode("secret123"));
        usuario.setRol(RolUsuario.CLIENTE);
        usuario.setActivo(true);

        when(usuarioRepository.findByUsernameAndActivoTrue("cliente1")).thenReturn(Optional.of(usuario));

        var response = usuarioService.login(new UsuarioLoginRequestDto("cliente1", "secret123"));

        assertEquals(RolUsuario.CLIENTE, response.rol());
        assertEquals("Login exitoso", response.mensaje());
    }

    @Test
    void loginDeberiaFallarSiLaPasswordEsIncorrecta() {
        Usuario usuario = new Usuario();
        usuario.setUsername("cliente1");
        usuario.setPassword(passwordEncoder.encode("secret123"));
        usuario.setActivo(true);

        when(usuarioRepository.findByUsernameAndActivoTrue("cliente1")).thenReturn(Optional.of(usuario));

        assertThrows(CredencialesInvalidasException.class,
                () -> usuarioService.login(new UsuarioLoginRequestDto("cliente1", "otra-clave")));
    }
}
