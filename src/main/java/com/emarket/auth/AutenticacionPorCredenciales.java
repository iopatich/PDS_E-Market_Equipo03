package com.emarket.auth;

import com.emarket.entity.Usuario;
import com.emarket.exception.CredencialesInvalidasException;
import com.emarket.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AutenticacionPorCredenciales implements EstrategiaAutenticacion {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Usuario autenticar(String username, String password) {
        Usuario usuario = usuarioRepository.findByUsernameAndActivoTrue(username)
                .orElseThrow(() -> new CredencialesInvalidasException("Usuario o contraseña incorrectos"));

        if (!passwordEncoder.matches(password, usuario.getPassword())) {
            throw new CredencialesInvalidasException("Usuario o contraseña incorrectos");
        }

        return usuario;
    }
}
