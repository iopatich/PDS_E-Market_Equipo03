package com.emarket.service.interfaz;

import com.emarket.dto.administrador.AdministradorRequestDto;
import com.emarket.dto.administrador.AdministradorResponseDto;
import com.emarket.dto.cliente.ClienteResponseDto;

import java.util.List;

public interface AdministradorService {
    AdministradorResponseDto registrar(AdministradorRequestDto dto, String token);
    List<AdministradorResponseDto> listar(String token);
    AdministradorResponseDto eliminar(Long id);
    ClienteResponseDto darDeBajaCliente(Long clienteId, String token);
}
