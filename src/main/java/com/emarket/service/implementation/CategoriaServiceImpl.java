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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {
    private final CategoriaRepository categoriaRepo;
    private final AuthService authService;

    @Override
    @Transactional
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
    @Transactional(readOnly = true)
    public List<CategoriaResponseDto> listar() {
        return categoriaRepo.findAllByActivoTrue().
                stream()
                .map(CategoriaMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Double obtenerPrecioTotal(Long id) {
        Categoria categoria = buscarCategoriaActiva(id);
        return categoria.calcularPrecio();
    }

    @Override
    @Transactional
    public CategoriaResponseDto eliminar(Long id, String token) {
        authService.validarPermiso(token, Permiso.GESTIONAR_PRODUCTOS);
        Categoria categoria = buscarCategoriaActiva(id);
        categoria.setActivo(false);
        return CategoriaMapper.toResponseDto(categoriaRepo.save(categoria));
    }

    private Categoria buscarCategoriaActiva(Long id) {
        Categoria categoria = categoriaRepo.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se ha encontrado la categoria con el id " + id
                ));
        if (!Boolean.TRUE.equals(categoria.getActivo())) {
            throw new RecursoNoEncontradoException("La categoria seleccionada no se encuentra disponible");
        }
        return categoria;
    }
}