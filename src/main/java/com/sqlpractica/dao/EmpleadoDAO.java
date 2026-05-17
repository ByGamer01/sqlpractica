package com.sqlpractica.dao;

import com.sqlpractica.model.Empleado;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO (Data Access Object) de la tabla 'empleado'
 *
 * IDEA GENERAL:
 *   - Pensamos en el DAO como la frontera entre el programa y JDBC
 *   - Métodos típicos del CRUD: insertar / actualizar / eliminar / obtenerTodos
 *   - usamos SIEMPRE PreparedStatement (con '?') en vez de concatenar SQL,
 *     para evitar SQL injection (aunque dudo mucho que aqu? inyectemos SQL)
 *     y para que JDBC se encargue de poner comillas, escapar caracteres, etc
 *   - En vez de lanzar excepciones hacia la UI, los m?todos devuelven
 *     false/null cuando hay un error; el mensaje queda en mensajeError
 *
 * Doc PreparedStatement:
 *   https://docs.oracle.com/javase/tutorial/jdbc/basics/prepared.html
 */
public class EmpleadoDAO extends BaseDAO {

  /**
   * Creamos un empleado nuevo en la tabla
   *  1. Preparamos la sentencia INSERT con 5 huecos '?'
   *  2. Rellenamos los huecos con los datos del objeto
   *  3. Llamamos a executeUpdate(), que ejecuta el INSERT
   *  4. Si SQLite devuelve un error (p.ej. NSS duplicado), lo
   *     capturamos, guardamos el mensaje y devolvemos false
   */
  public boolean insertar(Empleado empleado) {
    String sql = "INSERT INTO empleado(nss, nombre, apellidos, email, iban) "
                 + "VALUES (?, ?, ?, ?, ?)";
    Connection conn = obtenerConexion();
    if (conn == null) {
      return false;
    }
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, empleado.getNss());
      ps.setString(2, empleado.getNombre());
      ps.setString(3, empleado.getApellidos());
      ps.setString(4, empleado.getEmail());
      ps.setString(5, empleado.getIban());
      ps.executeUpdate();
      return true;
    } catch (SQLException ex) {
      mensajeError = "No se ha podido crear el empleado: " + ex.getMessage();
      return false;
    }
  }

  /**
   * Actualizamos los datos de un empleado existente (identificado por NSS)
   * executeUpdate() devuelve el número de filas afectadas
   * Si es 0 -> no había ningún empleado con ese NSS: guardamos el error
   */
  public boolean actualizar(Empleado empleado) {
    String sql = "UPDATE empleado SET nombre = ?, apellidos = ?, email = ?, "
                 + "iban = ? WHERE nss = ?";
    Connection conn = obtenerConexion();
    if (conn == null) {
      return false;
    }
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, empleado.getNombre());
      ps.setString(2, empleado.getApellidos());
      ps.setString(3, empleado.getEmail());
      ps.setString(4, empleado.getIban());
      ps.setString(5, empleado.getNss());
      int filas = ps.executeUpdate();
      return validarFilasAfectadas(filas,
                                   "No existe ningún empleado con NSS '" +
                                       empleado.getNss() + "'.");
    } catch (SQLException ex) {
      mensajeError =
          "No se ha podido actualizar el empleado: " + ex.getMessage();
      return false;
    }
  }

  /**
   * Borramos el empleado con el NSS indicado
   * Por las FK con ON DELETE CASCADE, también se borrarán sus
   * filas en 'ocupa' y 'nomina'
   */
  public boolean eliminar(String nss) {
    String sql = "DELETE FROM empleado WHERE nss = ?";
    Connection conn = obtenerConexion();
    if (conn == null) {
      return false;
    }
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, nss);
      int filas = ps.executeUpdate();
      return validarFilasAfectadas(
          filas, "No existe ningún empleado con NSS '" + nss + "'.");
    } catch (SQLException ex) {
      mensajeError = "No se ha podido eliminar el empleado: " + ex.getMessage();
      return false;
    }
  }

  /**
   * Devolvemos TODOS los empleados, ordenados por apellidos y nombre
   * Devolvemos null si hay un error (el mensaje queda en mensajeError)
   *
   *  1. Ejecutamos SELECT con executeQuery() (devuelve un ResultSet)
   *
   *  2. Recorremos el ResultSet con while(rs.next()), creamos un
   *     objeto Empleado por cada fila y añadiéndolo a la lista
   *
   *  3. try-with-resources cierra el PreparedStatement y el ResultSet
   *     automáticamente al salir del bloque
   */
  public List<Empleado> obtenerTodos() {
    String sql = "SELECT nss, nombre, apellidos, email, iban FROM empleado "
                 + "ORDER BY apellidos, nombre";
    Connection conn = obtenerConexion();
    if (conn == null) {
      return null;
    }
    List<Empleado> resultado = new ArrayList<>();
    try (PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery();) {
      while (rs.next()) {
        resultado.add(new Empleado(rs.getString("nss"), rs.getString("nombre"),
                                   rs.getString("apellidos"),
                                   rs.getString("email"),
                                   rs.getString("iban")));
      }
      return resultado;
    } catch (SQLException ex) {
      mensajeError = "Error leyendo la lista de empleados: " + ex.getMessage();
      return null;
    }
  }
}
