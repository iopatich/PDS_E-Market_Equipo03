package com.emarket.controller;
import com.emarket.dto.api.ApiResponseDto;
import com.emarket.dto.varianteProducto.VarianteProductoRequestDto;
import com.emarket.dto.varianteProducto.VarianteProductoResponseDto;
import com.emarket.entity.VarianteProducto;
import com.emarket.service.interfaz.VarianteProductoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/variantesproducto")
@RequiredArgsConstructor
public class VarianteProductoController {
    private final VarianteProductoService varianteProductoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VarianteProductoResponseDto crear(@Valid @RequestBody VarianteProductoRequestDto dto) {
        return varianteProductoService.crear(dto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<VarianteProductoResponseDto> listar() {
        return varianteProductoService.listar();
    }

    @PutMapping("/reducirstock/{id}")
    public ResponseEntity<ApiResponseDto<VarianteProductoResponseDto>> reducirStock(@PathVariable Long id, @RequestParam Integer cantidad) {
        return ResponseEntity.ok(new ApiResponseDto<>("Stock reducido exitosamente al id " + id, varianteProductoService.reducirStock(id, cantidad)));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<ApiResponseDto<VarianteProductoResponseDto>> eliminar(@PathVariable Long id) {
        VarianteProductoResponseDto varianteProductoEliminada = varianteProductoService.eliminar(id);
        return ResponseEntity.ok(new ApiResponseDto<>("Se ha eliminado la variante ", varianteProductoEliminada));
    }
}