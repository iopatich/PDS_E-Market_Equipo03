package com.emarket.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Dispara la carga inicial del catálogo al arrancar la aplicación.
 */
@Component
@Order(1)
@RequiredArgsConstructor
@Slf4j
public class CatalogoInicialLoader implements CommandLineRunner {

    private final CatalogoInicialService catalogoInicialService;

    @Value("${emarket.catalogo.cargar-inicial:true}")
    private boolean cargarInicial;

    @Value("${emarket.catalogo.forzar-recarga:false}")
    private boolean forzarRecarga;

    @Override
    public void run(String... args) {
        if (!cargarInicial) {
            log.info("Carga inicial del catálogo deshabilitada por configuración.");
            return;
        }

        try {
            catalogoInicialService.ejecutarCarga(forzarRecarga);
        } catch (Exception e) {
            log.error("Error al cargar el catálogo inicial: {}", e.getMessage(), e);
        }
    }
}
