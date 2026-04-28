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

    /**
     * Crea el esquema completo si no existe. Se llama una sola vez
     * al inicio de la aplicación.
     */
    public static void initSchema() throws DAOException {
        String[] ddl = new String[] {
            "CREATE TABLE IF NOT EXISTS empleado (" +
            "  nss TEXT PRIMARY KEY," +
            "  nombre TEXT NOT NULL," +
            "  apellidos TEXT NOT NULL," +
            "  email TEXT," +
            "  iban TEXT" +
            ");",

            "CREATE TABLE IF NOT EXISTS tipo_plaza (" +
            "  nombre TEXT PRIMARY KEY," +
            "  funcion TEXT" +
            ");",

            "CREATE TABLE IF NOT EXISTS plaza (" +
            "  codigo TEXT PRIMARY KEY," +
            "  nombre TEXT NOT NULL," +
            "  salario REAL NOT NULL," +
            "  codigo_plaza_supervisora TEXT," +
            "  informe_supervision TEXT," +
            "  nombre_tipo_plaza TEXT NOT NULL," +
            "  FOREIGN KEY (codigo_plaza_supervisora) REFERENCES plaza(codigo) ON DELETE SET NULL," +
            "  FOREIGN KEY (nombre_tipo_plaza) REFERENCES tipo_plaza(nombre) ON DELETE RESTRICT" +
            ");"
        };

        try (Statement st = getConnection().createStatement()) {
            for (String sql : ddl) {
                st.execute(sql);
            }
        } catch (SQLException e) {
            throw new DAOException("Error inicializando el esquema de la base de datos.", e);
        }
    }
}
