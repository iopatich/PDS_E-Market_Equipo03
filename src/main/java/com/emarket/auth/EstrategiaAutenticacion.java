package com.emarket.auth;

import com.emarket.entity.Usuario;

/**
 * Patrón Strategy: permite intercambiar la forma de autenticar usuarios
 * sin modificar el servicio de autenticación.
 */
public interface EstrategiaAutenticacion {
    Usuario autenticar(String username, String password);
}
