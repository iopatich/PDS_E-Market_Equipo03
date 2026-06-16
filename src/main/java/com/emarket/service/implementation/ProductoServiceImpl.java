package com.emarket.service.implementation;
import com.emarket.dto.producto.ProductoDetalleResponseDto;
import com.emarket.dto.producto.ProductoRequestDto;
import com.emarket.dto.producto.ProductoResponseDto;
import com.emarket.dto.producto.ProductoUpdateDto;
import com.emarket.entity.Categoria;
import com.emarket.entity.Producto;
import com.emarket.entity.VarianteProducto;
import com.emarket.exception.OperacionInvalidaException;
import com.emarket.exception.RecursoNoEncontradoException;
import com.emarket.mapper.ProductoMapper;
import com.emarket.mapper.VarianteProductoMapper;
import com.emarket.repository.CategoriaRepository;
import com.emarket.entity.Permiso;
import com.emarket.repository.ProductoRepository;
import com.emarket.repository.VarianteProductoRepository;
import com.emarket.service.interfaz.AuthService;
import com.emarket.service.interfaz.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {
    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final VarianteProductoRepository varianteProductoRepository;
    private final AuthService authService;

    @Override
    @Transactional
    public ProductoResponseDto crear(ProductoRequestDto dto, String token) {
        authService.validarPermiso(token, Permiso.CARGAR_PRODUCTO);
        Categoria categoriaPadre = buscarCategoriaActiva(dto.idCategoriaPadre());
        Producto guardado = productoRepository.save(ProductoMapper.toEntity(dto, categoriaPadre));
        return ProductoMapper.toResponseDto(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDto> listar() {
        return productoRepository.findAllByActivoTrue()
                .stream()
                .map(ProductoMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoDetalleResponseDto obtenerDetalle(Long id) {
        Producto producto = buscarProductoActivo(id);
        List<VarianteProducto> variantes = varianteProductoRepository.findAllByProductoIdAndActivoTrue(id);
        return ProductoMapper.toDetalleResponseDto(
                producto,
                variantes.stream()
                        .map(variante -> VarianteProductoMapper.toResponseDto(variante, calcularPrecioFinal(variante)))
                        .toList()
        );
    }

    @Override
    @Transactional
    public ProductoResponseDto actualizar(Long id, ProductoUpdateDto dto, String token) {
        authService.validarPermiso(token, Permiso.GESTIONAR_PRODUCTOS);
        Producto producto = buscarProductoActivo(id);

        if (dto.nombre() != null) {
            validarTexto(dto.nombre(), "El nombre no puede estar vacio");
            producto.setNombre(dto.nombre());
        }

        if (dto.descripcion() != null) {
            validarTexto(dto.descripcion(), "La descripcion no puede estar vacia");
            producto.setDescripcion(dto.descripcion());
        }

        if (dto.precioBase() != null) {
            producto.setPrecioBase(dto.precioBase());
        }

        if (dto.idCategoriaPadre() != null) {
            producto.setCategoriaPadre(buscarCategoriaActiva(dto.idCategoriaPadre()));
        }

        return ProductoMapper.toResponseDto(productoRepository.save(producto));
    }

    @Override
    @Transactional
    public ProductoResponseDto eliminar(Long id, String token) {
        authService.validarPermiso(token, Permiso.GESTIONAR_PRODUCTOS);
        Producto producto = buscarProductoActivo(id);
        producto.setActivo(false);
        return ProductoMapper.toResponseDto(productoRepository.save(producto));
    }

    private Producto buscarProductoActivo(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se ha encontrado el producto con el id " + id
                ));
        if (!Boolean.TRUE.equals(producto.getActivo())) {
            throw new RecursoNoEncontradoException("El producto seleccionado no se encuentra disponible");
        }
        return producto;
    }

    private Categoria buscarCategoriaActiva(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No se ha encontrado la categoria con el id " + id
                ));
        if (!Boolean.TRUE.equals(categoria.getActivo())) {
            throw new RecursoNoEncontradoException("La categoria seleccionada no se encuentra disponible");
        }
        return categoria;
    }

    private Double calcularPrecioFinal(VarianteProducto variante) {
        return variante.getProducto().getPrecioBase() + variante.getPrecioAdicional();
    }

    private void validarTexto(String valor, String mensaje) {
        if (valor.isBlank()) {
            throw new OperacionInvalidaException(mensaje);
        }
    }
}