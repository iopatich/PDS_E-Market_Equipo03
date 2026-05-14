package com.emarket.service.implementation;

import com.emarket.dto.categoria.CategoriaRequestDto;
import com.emarket.dto.categoria.CategoriaResponseDto;
import com.emarket.entity.Categoria;
import com.emarket.mapper.CategoriaMapper;
import com.emarket.repository.CategoriaRepository;
import com.emarket.service.interfaz.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {
    private final CategoriaRepository categoriaRepo;

    @Override
    public CategoriaResponseDto crear(CategoriaRequestDto dto) {
        Categoria padre = null;
        if (dto.idCategoriaPadre() != null) {
            padre = categoriaRepo.findById(dto.idCategoriaPadre()).orElseThrow(() -> new RuntimeException("Categoría padre no encontrada"));
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
    public CategoriaResponseDto eliminar(Long id) {
        Categoria categoria = categoriaRepo.findById(id).orElseThrow();
        categoria.setActivo(false);
        return CategoriaMapper.toResponseDto(categoriaRepo.save(categoria));
    }
}
