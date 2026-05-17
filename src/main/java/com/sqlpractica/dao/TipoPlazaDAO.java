package com.sqlpractica.dao;

import com.sqlpractica.model.TipoPlaza;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO de la tabla 'tipo_plaza'. Misma idea que EmpleadoDAO pero
 * con menos columnas: solo nombre (PK) y funcion
 *
 * Si intentamos eliminar un tipo que está siendo usado por alguna
 * plaza, SQLite lanzará un error gracias al FOREIGN KEY ... ON DELETE
 * RESTRICT del esquema. Lo capturamos y lo guardamos en mensajeError
 */
public class TipoPlazaDAO extends BaseDAO {

  public boolean insertar(TipoPlaza t) {
    String sql = "INSERT INTO tipo_plaza(nombre, funcion) VALUES (?, ?)";
    Connection conn = obtenerConexion();
    if (conn == null) {
      return false;
    }
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, t.getNombre());
      ps.setString(2, t.getFuncion());
      ps.executeUpdate();
      return true;
    } catch (SQLException e) {
      mensajeError =
          "No se ha podido crear el tipo de plaza: " + e.getMessage();
      return false;
    }
  }

  /**
   * Solo actualizamos la 'funcion'. El nombre es PK, no se puede
   * cambiar (haría falta borrar y volver a crear)
   */
  public boolean actualizar(TipoPlaza t) {
    String sql = "UPDATE tipo_plaza SET funcion = ? WHERE nombre = ?";
    Connection conn = obtenerConexion();
    if (conn == null) {
      return false;
    }
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, t.getFuncion());
      ps.setString(2, t.getNombre());
      int filas = ps.executeUpdate();
      return validarFilasAfectadas(
          filas,
          "No existe ningún tipo de plaza con nombre '" + t.getNombre() + "'.");
    } catch (SQLException e) {
      mensajeError =
          "No se ha podido actualizar el tipo de plaza: " + e.getMessage();
      return false;
    }
  }

  public boolean eliminar(String nombre) {
    String sql = "DELETE FROM tipo_plaza WHERE nombre = ?";
    Connection conn = obtenerConexion();
    if (conn == null) {
      return false;
    }
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, nombre);
      int filas = ps.executeUpdate();
      return validarFilasAfectadas(
          filas, "No existe ningún tipo de plaza con nombre '" + nombre + "'.");
    } catch (SQLException e) {
      // Aquí suele caer el error "FOREIGN KEY constraint failed"
      // si hay plazas usando este tipo (ON DELETE RESTRICT)
      mensajeError =
          "No se ha podido eliminar el tipo de plaza: " + e.getMessage();
      return false;
    }
  }

  public List<TipoPlaza> obtenerTodos() {
    String sql = "SELECT nombre, funcion FROM tipo_plaza ORDER BY nombre";
    Connection conn = obtenerConexion();
    if (conn == null) {
      return null;
    }
    List<TipoPlaza> resultado = new ArrayList<>();
    try (PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        resultado.add(
            new TipoPlaza(rs.getString("nombre"), rs.getString("funcion")));
      }
      return resultado;
    } catch (SQLException e) {
      mensajeError = "Error leyendo los tipos de plaza: " + e.getMessage();
      return null;
    }
  }
}
