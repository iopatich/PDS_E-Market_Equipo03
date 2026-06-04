package com.emarket.service.implementation;

import com.emarket.dto.categoria.CategoriaRequestDto;
import com.emarket.dto.categoria.CategoriaResponseDto;
import com.emarket.entity.Categoria;
import com.emarket.exception.RecursoNoEncontradoException;
import com.emarket.mapper.CategoriaMapper;
import com.emarket.entity.Permiso;
import com.emarket.repository.CategoriaRepository;
import com.emarket.service.interfaz.AuthService;
import com.emarket.service.interfaz.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {
    private final CategoriaRepository categoriaRepo;
    private final AuthService authService;

    @Override
    public CategoriaResponseDto crear(CategoriaRequestDto dto, String token) {
        authService.validarPermiso(token, Permiso.CARGAR_PRODUCTO);
        Categoria padre = null;
        if (dto.idCategoriaPadre() != null) {
            padre = categoriaRepo.findById(dto.idCategoriaPadre())
                    .orElseThrow(() -> new RecursoNoEncontradoException(
                            "No se ha encontrado la categoria con el id" + dto.idCategoriaPadre()
                    ));
        }
        Categoria guardada = categoriaRepo.save(CategoriaMapper.toEntity(dto, padre));
        return CategoriaMapper.toResponseDto(guardada);
    }

    @Override
    public List<CategoriaResponseDto> listar() {
        return categoriaRepo.findAllByActivoTrue().
                stream()
                .map(CategoriaMapper::toResponseDto)
                .toList();
    }

    @Override
    public CategoriaResponseDto eliminar(Long id, String token) {
        authService.validarPermiso(token, Permiso.GESTIONAR_PRODUCTOS);
        Categoria categoria = categoriaRepo.findById(id).orElseThrow();
        categoria.setActivo(false);
        return CategoriaMapper.toResponseDto(categoriaRepo.save(categoria));
    }
}