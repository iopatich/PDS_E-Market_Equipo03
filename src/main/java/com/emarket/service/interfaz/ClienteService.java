package com.emarket.service.interfaz;

import com.emarket.dto.cliente.ClienteRequestDto;
import com.emarket.dto.cliente.ClienteResponseDto;

import java.util.List;

public interface ClienteService {
    ClienteResponseDto registrar(ClienteRequestDto dto);
    List<ClienteResponseDto> listar(String token);
    ClienteResponseDto eliminar(Long id);
}
