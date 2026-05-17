package com.sqlpractica.dao;

import com.sqlpractica.model.Ocupa;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO de la tabla 'ocupa' (relación N:M empleado-plaza)
 *
 * IMPORTANTE: la PK es COMPUESTA, así que para identificar una fila
 * (en update y delete) necesitamos las DOS claves: nss_empleado y
 * codigo_plaza
 */
public class OcupaDAO extends BaseDAO {

  public boolean insertar(Ocupa o) {
    String sql = "INSERT INTO ocupa(nss_empleado, codigo_plaza, "
                 + "fecha_inicio, fecha_fin) VALUES (?, ?, ?, ?)";
    Connection conn = obtenerConexion();
    if (conn == null) {
      return false;
    }
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, o.getNssEmpleado());
      ps.setString(2, o.getCodigoPlaza());
      ps.setString(3, o.getFechaInicio());
      asignarTextoOpcional(
          ps, 4, o.getFechaFin()); // puede ser null, ya que es opcional
      ps.executeUpdate();
      return true;
    } catch (SQLException e) {
      mensajeError = "No se ha podido crear la ocupación: " + e.getMessage();
      return false;
    }
  }

  /**
   * Solo se pueden cambiar las fechas; las claves (NSS y codigo_plaza)
   * van en el WHERE para localizar la fila
   */
  public boolean actualizar(Ocupa o) {
    String sql = "UPDATE ocupa SET fecha_inicio = ?, fecha_fin = ? WHERE "
                 + "nss_empleado = ? AND codigo_plaza = ?";
    Connection conn = obtenerConexion();
    if (conn == null) {
      return false;
    }
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, o.getFechaInicio());
      asignarTextoOpcional(ps, 2, o.getFechaFin());
      ps.setString(3, o.getNssEmpleado());
      ps.setString(4, o.getCodigoPlaza());
      int filas = ps.executeUpdate();
      return validarFilasAfectadas(
          filas, "No existe ninguna ocupación para este empleado y plaza.");
    } catch (SQLException e) {
      mensajeError =
          "No se ha podido actualizar la ocupación: " + e.getMessage();
      return false;
    }
  }

  /**
   * eliminar recibe LAS DOS partes de la clave compuesta
   */
  public boolean eliminar(String nssEmpleado, String codigoPlaza) {
    String sql =
        "DELETE FROM ocupa WHERE nss_empleado = ? AND codigo_plaza = ?";
    Connection conn = obtenerConexion();
    if (conn == null) {
      return false;
    }
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, nssEmpleado);
      ps.setString(2, codigoPlaza);
      int filas = ps.executeUpdate();
      return validarFilasAfectadas(
          filas, "No existe ninguna ocupación con esa combinación.");
    } catch (SQLException e) {
      mensajeError = "No se ha podido eliminar la ocupación: " + e.getMessage();
      return false;
    }
  }

  public List<Ocupa> obtenerTodos() {
    // Ordenamos por fecha_inicio descendente: las más recientes arriba
    String sql =
        "SELECT nss_empleado, codigo_plaza, fecha_inicio, fecha_fin FROM ocupa "
        + "ORDER BY fecha_inicio DESC";
    Connection conn = obtenerConexion();
    if (conn == null) {
      return null;
    }
    List<Ocupa> resultado = new ArrayList<>();
    try (PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        resultado.add(new Ocupa(
            rs.getString("nss_empleado"), rs.getString("codigo_plaza"),
            rs.getString("fecha_inicio"), rs.getString("fecha_fin")));
      }
      return resultado;
    } catch (SQLException e) {
      mensajeError = "Error leyendo las ocupaciones: " + e.getMessage();
      return null;
    }
  }
}
