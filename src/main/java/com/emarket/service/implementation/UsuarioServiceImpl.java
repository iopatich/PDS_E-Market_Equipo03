package com.emarket.service.implementation;

import com.emarket.dto.usuario.UsuarioLoginRequestDto;
import com.emarket.dto.usuario.UsuarioLoginResponseDto;
import com.emarket.dto.usuario.UsuarioRegistroRequestDto;
import com.emarket.dto.usuario.UsuarioResponseDto;
import com.emarket.entity.Usuario;
import com.emarket.exception.CredencialesInvalidasException;
import com.emarket.exception.DuplicadoException;
import com.emarket.mapper.UsuarioMapper;
import com.emarket.repository.UsuarioRepository;
import com.emarket.service.interfaz.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UsuarioResponseDto registrar(UsuarioRegistroRequestDto dto) {
        validarDuplicados(dto);
        Usuario usuario = UsuarioMapper.toEntity(dto, passwordEncoder.encode(dto.password()));
        Usuario guardado = usuarioRepository.save(usuario);
        return UsuarioMapper.toResponseDto(guardado);
    }

    @Override
    public UsuarioLoginResponseDto login(UsuarioLoginRequestDto dto) {
        Usuario usuario = usuarioRepository.findByUsernameAndActivoTrue(dto.username())
                .orElseThrow(() -> new CredencialesInvalidasException("Credenciales invalidas"));

        if (!passwordEncoder.matches(dto.password(), usuario.getPassword())) {
            throw new CredencialesInvalidasException("Credenciales invalidas");
        }

        return UsuarioMapper.toLoginResponseDto(usuario);
    }

    private void validarDuplicados(UsuarioRegistroRequestDto dto) {
        if (usuarioRepository.existsByUsernameIgnoreCase(dto.username())) {
            throw new DuplicadoException("Ya existe un usuario con ese username");
        }
        if (usuarioRepository.existsByEmailIgnoreCase(dto.email())) {
            throw new DuplicadoException("Ya existe un usuario con ese email");
        }
    }
}
