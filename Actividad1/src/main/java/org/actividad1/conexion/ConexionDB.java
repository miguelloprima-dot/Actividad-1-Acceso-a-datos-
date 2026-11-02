package org.actividad1.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {
    private static final String URL = "jdbc:mysql://localhost:3306/tiendadb";
    private static final String USER = "root"; // Cambia por tu usuario
    private static final String PASSWORD = "usuario"; // Cambia por tu contraseña

    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
