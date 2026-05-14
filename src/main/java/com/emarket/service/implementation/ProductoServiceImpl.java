package com.emarket.service.implementation;
import com.emarket.dto.producto.ProductoRequestDto;
import com.emarket.dto.producto.ProductoResponseDto;
import com.emarket.entity.Categoria;
import com.emarket.entity.Producto;
import com.emarket.mapper.ProductoMapper;
import com.emarket.repository.CategoriaRepository;
import com.emarket.repository.ProductoRepository;
import com.emarket.service.interfaz.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {
    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    @Override
    public ProductoResponseDto crear(ProductoRequestDto dto) {
        Categoria CategoriaPadre = categoriaRepository.findById(dto.idCategoriaPadre())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        Producto guardado = productoRepository.save(ProductoMapper.toEntity(dto, CategoriaPadre));
        return ProductoMapper.toResponseDto(guardado);
    }

    @Override
    public List<ProductoResponseDto> listar() {
        return productoRepository.findAllByActivoTrue()
                .stream()
                .map(ProductoMapper::toResponseDto)
                .toList();
    }

    @Override
    public ProductoResponseDto eliminar(Long id) {
        Producto producto = productoRepository.findById(id).orElseThrow();
        producto.setActivo(false);
        return ProductoMapper.toResponseDto(productoRepository.save(producto));
    }
}