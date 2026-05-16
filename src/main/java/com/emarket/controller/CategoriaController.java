package com.emarket.controller;
import com.emarket.dto.categoria.CategoriaRequestDto;
import com.emarket.dto.api.ApiResponseDto;
import com.emarket.dto.categoria.CategoriaResponseDto;
import com.emarket.service.interfaz.CategoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
public class CategoriaController {
    private final CategoriaService categoriaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoriaResponseDto crear(@Valid @RequestBody CategoriaRequestDto dto) {
        return categoriaService.crear(dto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CategoriaResponseDto> listar() {
        return categoriaService.listar();
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<ApiResponseDto<CategoriaResponseDto>> eliminar(@PathVariable Long id) {
        CategoriaResponseDto CategoriaEliminada = categoriaService.eliminar(id);
        return ResponseEntity.ok(new ApiResponseDto<>("Se ha eliminado la categoria ", CategoriaEliminada));
    }
}