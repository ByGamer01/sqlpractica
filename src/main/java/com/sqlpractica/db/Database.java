package com.sqlpractica.db;

import com.sqlpractica.exception.DAOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
}
