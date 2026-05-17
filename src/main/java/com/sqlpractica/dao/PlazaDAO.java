package com.sqlpractica.dao;

import com.sqlpractica.model.Plaza;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO de la tabla 'plaza'
 *
 * NOVEDAD respecto a EmpleadoDAO: dos columnas pueden ser NULL
 * (codigo_plaza_supervisora e informe_supervision). Por eso usamos
 * el helper asignarTextoOpcional(): si el campo viene vacío, en vez
 * de meter "" en la BD, mete NULL de verdad con ps.setNull(...)
 */
public class PlazaDAO extends BaseDAO {

  public boolean insertar(Plaza p) {
    String sql =
        "INSERT INTO plaza(codigo, nombre, salario, codigo_plaza_supervisora, "
        + "informe_supervision, nombre_tipo_plaza) VALUES (?, ?, ?, ?, ?, ?)";
    Connection conn = obtenerConexion();
    if (conn == null) {
      return false;
    }
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, p.getCodigo());
      ps.setString(2, p.getNombre());
      ps.setDouble(3, p.getSalario());
      asignarTextoOpcional(ps, 4, p.getCodigoPlazaSupervisora());
      asignarTextoOpcional(ps, 5, p.getInformeSupervision());
      ps.setString(6, p.getNombreTipoPlaza());
      ps.executeUpdate();
      return true;
    } catch (SQLException e) {
      mensajeError = "No se ha podido crear la plaza: " + e.getMessage();
      return false;
    }
  }

  public boolean actualizar(Plaza p) {
    // No tocamos la PK 'codigo' (va en el WHERE), sí cambian el resto
    String sql =
        "UPDATE plaza SET nombre = ?, salario = ?, codigo_plaza_supervisora "
        + "= ?, "
        + "informe_supervision = ?, nombre_tipo_plaza = ? WHERE codigo = ?";
    Connection conn = obtenerConexion();
    if (conn == null) {
      return false;
    }
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, p.getNombre());
      ps.setDouble(2, p.getSalario());
      asignarTextoOpcional(ps, 3, p.getCodigoPlazaSupervisora());
      asignarTextoOpcional(ps, 4, p.getInformeSupervision());
      ps.setString(5, p.getNombreTipoPlaza());
      ps.setString(6, p.getCodigo());
      int filas = ps.executeUpdate();
      return validarFilasAfectadas(
          filas, "No existe ninguna plaza con código '" + p.getCodigo() + "'.");
    } catch (SQLException e) {
      mensajeError = "No se ha podido actualizar la plaza: " + e.getMessage();
      return false;
    }
  }

  public boolean eliminar(String codigo) {
    String sql = "DELETE FROM plaza WHERE codigo = ?";
    Connection conn = obtenerConexion();
    if (conn == null) {
      return false;
    }
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, codigo);
      int filas = ps.executeUpdate();
      return validarFilasAfectadas(
          filas, "No existe ninguna plaza con código '" + codigo + "'.");
    } catch (SQLException e) {
      mensajeError = "No se ha podido eliminar la plaza: " + e.getMessage();
      return false;
    }
  }

  public List<Plaza> obtenerTodos() {
    String sql =
        "SELECT codigo, nombre, salario, codigo_plaza_supervisora, "
        + "informe_supervision, nombre_tipo_plaza FROM plaza ORDER BY codigo";
    Connection conn = obtenerConexion();
    if (conn == null) {
      return null;
    }
    List<Plaza> resultado = new ArrayList<>();
    try (PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        // rs.getString devuelve null si la celda es NULL, así
        // que se mapea limpio al objeto Plaza
        resultado.add(new Plaza(rs.getString("codigo"), rs.getString("nombre"),
                                rs.getDouble("salario"),
                                rs.getString("codigo_plaza_supervisora"),
                                rs.getString("informe_supervision"),
                                rs.getString("nombre_tipo_plaza")));
      }
      return resultado;
    } catch (SQLException e) {
      mensajeError = "Error leyendo las plazas: " + e.getMessage();
      return null;
    }
  }
}
