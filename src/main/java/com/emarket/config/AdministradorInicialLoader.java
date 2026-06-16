package com.emarket.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Crea un administrador por defecto al arrancar si no existe ninguno activo.
 */
@Component
@Order(0)
@RequiredArgsConstructor
@Slf4j
public class AdministradorInicialLoader implements CommandLineRunner {

    private final AdministradorInicialService administradorInicialService;

    @Value("${emarket.admin.cargar-inicial:true}")
    private boolean cargarInicial;

    @Value("${emarket.admin.username:admin}")
    private String username;

    @Value("${emarket.admin.password:admin123}")
    private String password;

    @Override
    public void run(String... args) {
        if (!cargarInicial) {
            log.info("Carga inicial de administrador deshabilitada por configuración.");
            return;
        }

        try {
            administradorInicialService.ejecutarCarga(username, password);
        } catch (Exception e) {
            log.error("Error al crear el administrador inicial: {}", e.getMessage(), e);
        }
    }
}
