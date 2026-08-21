package com.proyecto.gestortareas.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utilidad centralizada para obtener conexiones a la base de datos MySQL.
 * Ajusta URL, USUARIO y CLAVE segun tu entorno (usuario y contrasena de tu
 * instalacion local de MySQL).
 */
public class ConexionBD {

    private static final String URL =
            "jdbc:mysql://localhost:3306/gestor_tareas?useSSL=false&serverTimezone=UTC";
    private static final String USUARIO = "gestor_app";
    private static final String CLAVE = "gestor123";

    private ConexionBD() {}

    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CLAVE);
    }
}
