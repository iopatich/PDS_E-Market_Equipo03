package com.emarket.service.implementation;

import com.emarket.dto.cliente.ClienteRequestDto;
import com.emarket.dto.cliente.ClienteResponseDto;
import com.emarket.entity.Cliente;
import com.emarket.exception.RecursoNoEncontradoException;
import com.emarket.exception.UsuarioYaExisteException;
import com.emarket.factory.UsuarioFactory;
import com.emarket.mapper.ClienteMapper;
import com.emarket.repository.ClienteRepository;
import com.emarket.repository.UsuarioRepository;
import com.emarket.entity.Permiso;
import com.emarket.service.interfaz.AuthService;
import com.emarket.service.interfaz.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioFactory usuarioFactory;
    private final AuthService authService;

    @Override
    public ClienteResponseDto registrar(ClienteRequestDto dto) {
        if (usuarioRepository.existsByUsername(dto.username())) {
            throw new UsuarioYaExisteException("El username ya está registrado");
        }
        Cliente guardado = clienteRepository.save(usuarioFactory.crearCliente(dto));
        return ClienteMapper.toResponseDto(guardado);
    }

    @Override
    public List<ClienteResponseDto> listar(String token) {
        authService.validarPermiso(token, Permiso.DAR_ALTA_CLIENTE);
        return clienteRepository.findAllByActivoTrue()
                .stream()
                .map(ClienteMapper::toResponseDto)
                .toList();
    }

    @Override
    public ClienteResponseDto eliminar(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se ha encontrado el cliente con el id " + id
                ));
        cliente.setActivo(false);
        return ClienteMapper.toResponseDto(clienteRepository.save(cliente));
    }
}
