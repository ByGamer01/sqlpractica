package com.sqlpractica.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sqlpractica.DAOException;
import com.sqlpractica.Database;
import com.sqlpractica.model.Empleado;

/**
 * DAO (Data Access Object) de la tabla 'empleado'.
 *
 * LÓGICA / IDEA GENERAL:
 *   - El DAO es la "frontera" entre el resto del programa y JDBC.
 *   - Métodos típicos del CRUD: insertar / actualizar / eliminar /
 *     obtenerTodos.
 *   - Usa SIEMPRE PreparedStatement (con '?') en vez de concatenar SQL,
 *     para evitar SQL injection y para que JDBC se encargue de poner
 *     comillas, escapar caracteres, etc.
 *
 * Doc PreparedStatement:
 *   https://docs.oracle.com/javase/tutorial/jdbc/basics/prepared.html
 */
public class EmpleadoDAO {

    /**
     * Crea un empleado nuevo en la tabla.
     *  1. Preparamos la sentencia INSERT con 5 huecos '?'.
     *  2. Rellenamos los huecos con los datos del objeto.
     *  3. Llamamos a executeUpdate(), que ejecuta el INSERT.
     *  4. Si SQLite devuelve un error (p.ej. NSS duplicado), lo
     *     capturamos y lanzamos DAOException con un mensaje legible.
     */
    public void insertar(Empleado e) throws DAOException {
        String sql = "INSERT INTO empleado(nss, nombre, apellidos, email, iban) VALUES (?, ?, ?, ?, ?)";
        Connection conn = Database.obtenerConexion();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.getNss());
            ps.setString(2, e.getNombre());
            ps.setString(3, e.getApellidos());
            ps.setString(4, e.getEmail());
            ps.setString(5, e.getIban());
            ps.executeUpdate();
        } catch (SQLException ex) {
            throw new DAOException("No se ha podido crear el empleado: " + ex.getMessage(), ex);
        }
    }

    /**
     * Actualiza los datos de un empleado existente (identificado por NSS).
     * executeUpdate() devuelve el número de filas afectadas.
     * Si es 0 -> no había ningún empleado con ese NSS: avisamos al usuario.
     */
    public void actualizar(Empleado e) throws DAOException {
        String sql = "UPDATE empleado SET nombre = ?, apellidos = ?, email = ?, iban = ? WHERE nss = ?";
        Connection conn = Database.obtenerConexion();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.getNombre());
            ps.setString(2, e.getApellidos());
            ps.setString(3, e.getEmail());
            ps.setString(4, e.getIban());
            ps.setString(5, e.getNss());
            int filas = ps.executeUpdate();
            if (filas == 0) {
                throw new DAOException("No existe ningún empleado con NSS '" + e.getNss() + "'.");
            }
        } catch (SQLException ex) {
            throw new DAOException("No se ha podido actualizar el empleado: " + ex.getMessage(), ex);
        }
    }

    /**
     * Borra el empleado con el NSS indicado.
     * Por las FK con ON DELETE CASCADE, también se borrarán sus
     * filas en 'ocupa' y 'nomina'.
     */
    public void eliminar(String nss) throws DAOException {
        String sql = "DELETE FROM empleado WHERE nss = ?";
        Connection conn = Database.obtenerConexion();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nss);
            int filas = ps.executeUpdate();
            if (filas == 0) {
                throw new DAOException("No existe ningún empleado con NSS '" + nss + "'.");
            }
        } catch (SQLException ex) {
            throw new DAOException("No se ha podido eliminar el empleado: " + ex.getMessage(), ex);
        }
    }

    /**
     * Devuelve TODOS los empleados, ordenados por apellidos+nombre.
     *  1. Ejecutamos SELECT con executeQuery() (devuelve un ResultSet).
     *  2. Recorremos el ResultSet con while(rs.next()), creando un
     *     objeto Empleado por cada fila y añadiéndolo a la lista.
     *  3. try-with-resources cierra el PreparedStatement y el ResultSet
     *     automáticamente al salir del bloque.
     */
    public List<Empleado> obtenerTodos() throws DAOException {
        String sql = "SELECT nss, nombre, apellidos, email, iban FROM empleado ORDER BY apellidos, nombre";
        Connection conn = Database.obtenerConexion();
        List<Empleado> resultado = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                resultado.add(new Empleado(
                        rs.getString("nss"),
                        rs.getString("nombre"),
                        rs.getString("apellidos"),
                        rs.getString("email"),
                        rs.getString("iban")
                ));
            }
        } catch (SQLException ex) {
            throw new DAOException("Error leyendo la lista de empleados: " + ex.getMessage(), ex);
        }
        return resultado;
    }
}
