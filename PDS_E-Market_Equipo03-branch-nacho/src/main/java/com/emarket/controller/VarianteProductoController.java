package com.emarket.controller;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.emarket.dto.api.ApiResponseDto;
import com.emarket.dto.varianteProducto.VarianteProductoRequestDto;
import com.emarket.dto.varianteProducto.VarianteProductoResponseDto;
import com.emarket.service.interfaz.VarianteProductoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/variantesproducto")
@RequiredArgsConstructor
public class VarianteProductoController {
    private final VarianteProductoService varianteProductoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VarianteProductoResponseDto crear(
            @Valid @RequestBody VarianteProductoRequestDto dto,
            @RequestHeader("Authorization") String authorization
    ) {
        return varianteProductoService.crear(dto, authorization);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<VarianteProductoResponseDto> listar() {
        return varianteProductoService.listar();
    }

    @PutMapping("/reducirstock/{id}")
    public ResponseEntity<ApiResponseDto<VarianteProductoResponseDto>> reducirStock(
            @PathVariable Long id,
            @RequestParam Integer cantidad,
            @RequestHeader("Authorization") String authorization
    ) {
        return ResponseEntity.ok(new ApiResponseDto<>("Stock reducido exitosamente al id " + id, varianteProductoService.reducirStock(id, cantidad, authorization)));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<ApiResponseDto<VarianteProductoResponseDto>> eliminar(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authorization
    ) {
        VarianteProductoResponseDto varianteProductoEliminada = varianteProductoService.eliminar(id, authorization);
        return ResponseEntity.ok(new ApiResponseDto<>("Se ha eliminado la variante ", varianteProductoEliminada));
    }
}