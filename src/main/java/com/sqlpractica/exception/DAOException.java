package com.sqlpractica.exception;

/**
 * Excepción propia para encapsular cualquier error que se produzca
 * en la capa de acceso a datos (DAO). Así la UI no tiene que tratar
 * directamente con {@link java.sql.SQLException}.
 */
public class DAOException extends Exception {

    public DAOException(String message) {
        super(message);
    }
}
