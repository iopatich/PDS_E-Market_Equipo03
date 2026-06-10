package com.emarket.service.implementation;

import com.emarket.dto.administrador.AdministradorRequestDto;
import com.emarket.dto.administrador.AdministradorResponseDto;
import com.emarket.dto.cliente.ClienteResponseDto;
import com.emarket.entity.Administrador;
import com.emarket.entity.Cliente;
import com.emarket.entity.Permiso;
import com.emarket.exception.RecursoNoEncontradoException;
import com.emarket.exception.UsuarioYaExisteException;
import com.emarket.mapper.ClienteMapper;
import com.emarket.repository.AdministradorRepository;
import com.emarket.repository.ClienteRepository;
import com.emarket.repository.UsuarioRepository;
import com.emarket.service.interfaz.AdministradorService;
import com.emarket.service.interfaz.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdministradorServiceImpl implements AdministradorService {

    private final AdministradorRepository administradorRepository;
    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioFactory usuarioFactory;
    private final AuthService authService;

    @Override
    public AdministradorResponseDto registrar(AdministradorRequestDto dto) {
        if (usuarioRepository.existsByUsername(dto.username())) {
            throw new UsuarioYaExisteException("El username ya está registrado");
        }
        Administrador guardado = administradorRepository.save(usuarioFactory.crearAdministrador(dto));
        return AdministradorMapper.toResponseDto(guardado);
    }

    @Override
    public List<AdministradorResponseDto> listar(String token) {
        authService.validarEsAdministrador(token);
        return administradorRepository.findAllByActivoTrue()
                .stream()
                .map(AdministradorMapper::toResponseDto)
                .toList();
    }

    @Override
    public AdministradorResponseDto eliminar(Long id) {
        Administrador administrador = administradorRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se ha encontrado el administrador con el id " + id
                ));
        administrador.setActivo(false);
        return AdministradorMapper.toResponseDto(administradorRepository.save(administrador));
    }

    @Override
    public ClienteResponseDto darDeBajaCliente(Long clienteId, String token) {
        Administrador administrador = (Administrador) authService.validarPermiso(token, Permiso.DAR_BAJA_CLIENTE);

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se ha encontrado el cliente con el id " + clienteId
                ));

        administrador.darDeBajaCliente(cliente);
        return ClienteMapper.toResponseDto(clienteRepository.save(cliente));
    }
}
