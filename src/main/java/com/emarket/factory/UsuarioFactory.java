package com.emarket.factory;

import com.emarket.dto.administrador.AdministradorRequestDto;
import com.emarket.dto.cliente.ClienteRequestDto;
import com.emarket.entity.Administrador;
import com.emarket.entity.Cliente;
import com.emarket.mapper.AdministradorMapper;
import com.emarket.mapper.ClienteMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Patrón Simple Factory: centraliza la creación de usuarios
 * y la asignación de permisos y contraseñas según el rol.
 */



@Component
@RequiredArgsConstructor
public class UsuarioFactory {

    private final PasswordEncoder passwordEncoder;

    public Cliente crearCliente(ClienteRequestDto dto) {
        Cliente cliente = ClienteMapper.toEntity(dto);
        cliente.setPassword(passwordEncoder.encode(dto.password()));
        cliente.asignarPermisoCliente();
        return cliente;
    }

    public Administrador crearAdministrador(AdministradorRequestDto dto) {
        Administrador administrador = AdministradorMapper.toEntity(dto);
        administrador.setPassword(passwordEncoder.encode(dto.password()));
        administrador.asignarPermisoAdministrador();
        return administrador;
    }
}
