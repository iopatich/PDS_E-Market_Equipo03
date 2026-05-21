package com.emarket.mapper;

import com.emarket.dto.auth.LoginResponseDto;
import com.emarket.dto.cliente.ClienteRequestDto;
import com.emarket.dto.cliente.ClienteResponseDto;
import com.emarket.entity.Cliente;
import com.emarket.entity.Sesion;

public class ClienteMapper {
    private ClienteMapper() {
    }

    public static Cliente toEntity(ClienteRequestDto dto) {
        Cliente cliente = new Cliente();
        cliente.setUsername(dto.username());
        cliente.setEmail(dto.email());
        return cliente;
    }

    public static ClienteResponseDto toResponseDto(Cliente cliente) {
        return new ClienteResponseDto(
                cliente.getId(),
                cliente.getUsername(),
                cliente.getEmail(),
                cliente.getPermisos()
        );
    }

    public static LoginResponseDto toLoginResponseDto(Sesion sesion) {
        Cliente cliente = (Cliente) sesion.getUsuario();
        return new LoginResponseDto(
                sesion.getToken(),
                cliente.getTipoUsuario(),
                cliente.getId(),
                cliente.getUsername(),
                cliente.getPermisos()
        );
    }
}
