package com.emarket.controller;
import com.emarket.dto.producto.ProductoRequestDto;
import com.emarket.dto.producto.ProductoDetalleResponseDto;
import com.emarket.dto.producto.ProductoUpdateDto;
import com.emarket.dto.api.ApiResponseDto;
import com.emarket.dto.producto.ProductoResponseDto;
import com.emarket.service.interfaz.ProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {
    private final ProductoService productoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductoResponseDto crear(
            @Valid @RequestBody ProductoRequestDto dto,
            @RequestHeader("Authorization") String authorization
    ) {
        return productoService.crear(dto, authorization);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductoResponseDto> listar() {
        return productoService.listar();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductoDetalleResponseDto obtenerDetalle(@PathVariable Long id) {
        return productoService.obtenerDetalle(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto<ProductoResponseDto>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProductoUpdateDto dto,
            @RequestHeader("Authorization") String authorization
    ) {
        ProductoResponseDto productoActualizado = productoService.actualizar(id, dto, authorization);
        return ResponseEntity.ok(new ApiResponseDto<>("Producto actualizado", productoActualizado));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<ApiResponseDto<ProductoResponseDto>> eliminar(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authorization
    ) {
        ProductoResponseDto productoEliminado = productoService.eliminar(id, authorization);
        return ResponseEntity.ok(new ApiResponseDto<>("Se ha eliminado el producto ", productoEliminado));
    }
}