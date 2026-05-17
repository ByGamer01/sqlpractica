package com.sqlpractica.dao;

import com.sqlpractica.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Base com?n para los DAO
 *
 * Centraliza la gesti?n de errores repetida: conexi?n, comprobaci?n de filas
 * afectadas y valores opcionales que deben guardarse como NULL en SQLite
 */
abstract class BaseDAO {

  protected String mensajeError = "";

  public String getMensajeError() { return mensajeError; }

  protected Connection obtenerConexion() {
    Connection conn = Database.obtenerConexion();

    if (conn == null) {
      mensajeError = "No hay conexi?n con la base de datos";
    }

    return conn;
  }

  protected boolean validarFilasAfectadas(int filas, String mensajeSiNoExiste) {
    if (filas == 0) {
      mensajeError = mensajeSiNoExiste;
      return false;
    }

    return true;
  }

  protected void asignarTextoOpcional(PreparedStatement ps, int indice,
                                      String valor) throws SQLException {
    if (valor == null || valor.isBlank()) {
      ps.setNull(indice, Types.VARCHAR);
      return;
    }

    ps.setString(indice, valor);
  }
}
