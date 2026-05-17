package com.sqlpractica.dao;

import com.sqlpractica.model.Nomina;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO de la tabla 'nomina'
 *
 * NOVEDAD: el id es AUTOINCREMENT (lo genera SQLite). Por eso, al
 * insertar, pedimos a JDBC que nos devuelva la "clave generada" con
 * Statement.RETURN_GENERATED_KEYS y se la metemos al objeto Nomina
 * con setId(). De esta forma, después del insert, el objeto en
 * memoria queda con su ID real
 *
 * Doc claves generadas:
 *   https://docs.oracle.com/javase/tutorial/jdbc/basics/retrieving.html
 */
public class NominaDAO extends BaseDAO {

  public boolean insertar(Nomina n) {
    String sql = "INSERT INTO nomina(iban_pago, importe_pago, nss_empleado, "
                 + "codigo_plaza) "
                 + "VALUES (?, ?, ?, ?)";
    Connection conn = obtenerConexion();
    if (conn == null) {
      return false;
    }
    // Pedimos a JDBC que recuerde la clave que SQLite va a generar
    try (PreparedStatement ps =
             conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      ps.setString(1, n.getIbanPago());
      ps.setDouble(2, n.getImportePago());
      ps.setString(3, n.getNssEmpleado());
      ps.setString(4, n.getCodigoPlaza());
      ps.executeUpdate();

      // Recogemos el id generado y lo guardamos en el objeto
      try (ResultSet claves = ps.getGeneratedKeys()) {
        if (claves.next()) {
          n.setId(claves.getInt(1));
        }
      }
      return true;
    } catch (SQLException e) {
      mensajeError = "No se ha podido crear la nómina: " + e.getMessage();
      return false;
    }
  }

  /**
   * Para actualizar, necesitamos sí o sí el id (es la PK)
   * Si llega null lo tratamos como error de validación
   */
  public boolean actualizar(Nomina n) {
    if (n.getId() == null) {
      mensajeError = "Para actualizar una nómina hay que indicar el ID.";
      return false;
    }
    String sql = "UPDATE nomina SET iban_pago = ?, importe_pago = ?, "
                 + "nss_empleado = ?, codigo_plaza = ? WHERE id = ?";
    Connection conn = obtenerConexion();
    if (conn == null) {
      return false;
    }
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, n.getIbanPago());
      ps.setDouble(2, n.getImportePago());
      ps.setString(3, n.getNssEmpleado());
      ps.setString(4, n.getCodigoPlaza());
      ps.setInt(5, n.getId());
      int filas = ps.executeUpdate();
      return validarFilasAfectadas(filas, "No existe ninguna nómina con ID " +
                                              n.getId() + ".");
    } catch (SQLException e) {
      mensajeError = "No se ha podido actualizar la nómina: " + e.getMessage();
      return false;
    }
  }

  public boolean eliminar(int id) {
    String sql = "DELETE FROM nomina WHERE id = ?";
    Connection conn = obtenerConexion();
    if (conn == null) {
      return false;
    }
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setInt(1, id);
      int filas = ps.executeUpdate();
      return validarFilasAfectadas(filas, "No existe ninguna nómina con ID " +
                                              id + ".");
    } catch (SQLException e) {
      mensajeError = "No se ha podido eliminar la nómina: " + e.getMessage();
      return false;
    }
  }

  public List<Nomina> obtenerTodos() {
    // ORDER BY id DESC: las nóminas más nuevas (id mayor) primero
    String sql =
        "SELECT id, iban_pago, importe_pago, nss_empleado, codigo_plaza "
        + "FROM nomina ORDER BY id DESC";
    Connection conn = obtenerConexion();
    if (conn == null) {
      return null;
    }
    List<Nomina> resultado = new ArrayList<>();
    try (PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        resultado.add(new Nomina(rs.getInt("id"), rs.getString("iban_pago"),
                                 rs.getDouble("importe_pago"),
                                 rs.getString("nss_empleado"),
                                 rs.getString("codigo_plaza")));
      }
      return resultado;
    } catch (SQLException e) {
      mensajeError = "Error leyendo las nóminas: " + e.getMessage();
      return null;
    }
  }
}
