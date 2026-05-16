package com.emarket.controller;
import com.emarket.dto.producto.ProductoRequestDto;
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
    public ProductoResponseDto crear(@Valid @RequestBody ProductoRequestDto dto) {
        return productoService.crear(dto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductoResponseDto> listar() {
        return productoService.listar();
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<ApiResponseDto<ProductoResponseDto>> eliminar(@PathVariable Long id) {
        ProductoResponseDto productoEliminado = productoService.eliminar(id);
        return ResponseEntity.ok(new ApiResponseDto<>("Se ha eliminado el producto ", productoEliminado));
    }
}