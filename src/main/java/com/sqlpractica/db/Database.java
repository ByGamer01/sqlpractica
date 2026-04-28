package com.sqlpractica.db;

import com.sqlpractica.exception.DAOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Gestor central de la conexión a SQLite.
 * - Mantiene una única conexión para toda la aplicación (singleton).
 * - Activa las claves foráneas (por defecto SQLite no las hace cumplir).
 * - Crea todas las tablas si todavía no existen.
 */
public final class Database {

    private static final String DB_URL = "jdbc:sqlite:sqlpractica.db";

    private static Connection connection;

    private Database() {
    }

    /**
     * Devuelve la conexión compartida. Si todavía no existe, la crea.
     */
    public static synchronized Connection getConnection() throws DAOException {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(DB_URL);
                try (Statement st = connection.createStatement()) {
                    st.execute("PRAGMA foreign_keys = ON;");
                }
            }
            return connection;
        } catch (SQLException e) {
            throw new DAOException("No se ha podido conectar a la base de datos.", e);
        }
    }
}
