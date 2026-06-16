package com.emarket.config;

import com.emarket.dto.administrador.AdministradorRequestDto;
import com.emarket.entity.Administrador;
import com.emarket.mapper.AdministradorMapper;
import com.emarket.repository.AdministradorRepository;
import com.emarket.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdministradorInicialService {

    private final AdministradorRepository administradorRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void ejecutarCarga(String username, String password) {
        if (!administradorRepository.findAllByActivoTrue().isEmpty()) {
            log.info("Ya existen administradores activos. Carga inicial omitida.");
            return;
        }

        if (usuarioRepository.existsByUsername(username)) {
            log.warn("El username '{}' ya está registrado. Carga inicial de administrador omitida.", username);
            return;
        }

        AdministradorRequestDto dto = new AdministradorRequestDto(username, password);
        
        Administrador nuevoAdministrador = AdministradorMapper.toEntity(dto);
        nuevoAdministrador.setPassword(passwordEncoder.encode(dto.password()));
        nuevoAdministrador.asignarPermisoAdministrador();
        
        Administrador guardado = administradorRepository.save(nuevoAdministrador);
        
        log.info("Administrador inicial creado: username={}", guardado.getUsername());
    }
}