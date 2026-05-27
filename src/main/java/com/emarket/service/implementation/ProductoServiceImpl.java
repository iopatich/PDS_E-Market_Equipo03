package com.emarket.service.implementation;
import com.emarket.dto.producto.ProductoRequestDto;
import com.emarket.dto.producto.ProductoResponseDto;
import com.emarket.entity.Categoria;
import com.emarket.entity.Producto;
import com.emarket.exception.RecursoNoEncontradoException;
import com.emarket.mapper.ProductoMapper;
import com.emarket.repository.CategoriaRepository;
import com.emarket.entity.Permiso;
import com.emarket.repository.ProductoRepository;
import com.emarket.service.interfaz.AuthService;
import com.emarket.service.interfaz.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {
    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final AuthService authService;

    @Override
    public ProductoResponseDto crear(ProductoRequestDto dto, String token) {
        authService.validarPermiso(token, Permiso.CARGAR_PRODUCTO);
        Categoria CategoriaPadre = categoriaRepository.findById(dto.idCategoriaPadre())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se ha encontrado la categoria con el id" + dto.idCategoriaPadre()
                ));
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
    public ProductoResponseDto eliminar(Long id, String token) {
        authService.validarPermiso(token, Permiso.GESTIONAR_PRODUCTOS);
        Producto producto = productoRepository.findById(id).orElseThrow();
        producto.setActivo(false);
        return ProductoMapper.toResponseDto(productoRepository.save(producto));
    }
}