package com.emarket.controller;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.emarket.dto.api.ApiResponseDto;
import com.emarket.dto.categoria.CategoriaRequestDto;
import com.emarket.dto.categoria.CategoriaResponseDto;
import com.emarket.service.interfaz.CategoriaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
public class CategoriaController {
    private final CategoriaService categoriaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoriaResponseDto crear(
            @Valid @RequestBody CategoriaRequestDto dto,
            @RequestHeader("Authorization") String authorization
    ) {
        return categoriaService.crear(dto, authorization);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CategoriaResponseDto> listar() {
        return categoriaService.listar();
    }

    @GetMapping("/{id}/precio-total")
    @ResponseStatus(HttpStatus.OK)
    public Double obtenerPrecioTotal(@PathVariable Long id) {
        return categoriaService.obtenerPrecioTotal(id);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<ApiResponseDto<CategoriaResponseDto>> eliminar(
            @PathVariable Long id,
            @RequestHeader("Authorization") String authorization
    ) {
        CategoriaResponseDto CategoriaEliminada = categoriaService.eliminar(id, authorization);
        return ResponseEntity.ok(new ApiResponseDto<>("Se ha eliminado la categoria ", CategoriaEliminada));
    }
}