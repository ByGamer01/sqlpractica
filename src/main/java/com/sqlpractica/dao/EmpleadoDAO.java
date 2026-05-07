package com.sqlpractica.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
 *   - En vez de lanzar excepciones, los métodos devuelven false/null
 *     cuando hay un error; el mensaje queda en mensajeError.
 *
 * Doc PreparedStatement:
 *   https://docs.oracle.com/javase/tutorial/jdbc/basics/prepared.html
 */
public class EmpleadoDAO {

    // Último error producido. La UI lo lee con getMensajeError().
    private String mensajeError = "";

    /** Devuelve el último mensaje de error registrado. */
    public String getMensajeError() {
        return mensajeError;
    }

    /**
     * Crea un empleado nuevo en la tabla.
     *  1. Preparamos la sentencia INSERT con 5 huecos '?'
     *  2. Rellenamos los huecos con los datos del objeto
     *  3. Llamamos a executeUpdate(), que ejecuta el INSERT
     *  4. Si SQLite devuelve un error (p.ej. NSS duplicado), lo
     *     capturamos, guardamos el mensaje y devolvemos false.
     */
    public boolean insertar(Empleado empleado) {
        String sql = "INSERT INTO empleado(nss, nombre, apellidos, email, iban) VALUES (?, ?, ?, ?, ?)";
        Connection conn = Database.obtenerConexion();
        if (conn == null) {
            mensajeError = "No hay conexión con la base de datos.";
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
     * Actualiza los datos de un empleado existente (identificado por NSS).
     * executeUpdate() devuelve el número de filas afectadas.
     * Si es 0 -> no había ningún empleado con ese NSS: guardamos el error.
     */
    public boolean actualizar(Empleado empleado) {
        String sql = "UPDATE empleado SET nombre = ?, apellidos = ?, email = ?, iban = ? WHERE nss = ?";
        Connection conn = Database.obtenerConexion();
        if (conn == null) {
            mensajeError = "No hay conexión con la base de datos.";
            return false;
        }
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, empleado.getNombre());
            ps.setString(2, empleado.getApellidos());
            ps.setString(3, empleado.getEmail());
            ps.setString(4, empleado.getIban());
            ps.setString(5, empleado.getNss());
            int filas = ps.executeUpdate();
            if (filas == 0) {
                mensajeError = "No existe ningún empleado con NSS '" + empleado.getNss() + "'.";
                return false;
            }
            return true;
        } catch (SQLException ex) {
            mensajeError = "No se ha podido actualizar el empleado: " + ex.getMessage();
            return false;
        }
    }

    /**
     * Borra el empleado con el NSS indicado.
     * Por las FK con ON DELETE CASCADE, también se borrarán sus
     * filas en 'ocupa' y 'nomina'.
     */
    public boolean eliminar(String nss) {
        String sql = "DELETE FROM empleado WHERE nss = ?";
        Connection conn = Database.obtenerConexion();
        if (conn == null) {
            mensajeError = "No hay conexión con la base de datos.";
            return false;
        }
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nss);
            int filas = ps.executeUpdate();
            if (filas == 0) {
                mensajeError = "No existe ningún empleado con NSS '" + nss + "'.";
                return false;
            }
            return true;
        } catch (SQLException ex) {
            mensajeError = "No se ha podido eliminar el empleado: " + ex.getMessage();
            return false;
        }
    }

    /**
     * Devuelve TODOS los empleados, ordenados por apellidos y nombre.
     * Devuelve null si hay un error (el mensaje queda en mensajeError).
     *
     *  1. Ejecutamos SELECT con executeQuery() (devuelve un ResultSet)
     *
     *  2. Recorremos el ResultSet con while(rs.next()), creando un
     *     objeto Empleado por cada fila y añadiéndolo a la lista
     *
     *  3. try-with-resources cierra el PreparedStatement y el ResultSet
     *     automáticamente al salir del bloque
     */
    public List<Empleado> obtenerTodos() {
        String sql = "SELECT nss, nombre, apellidos, email, iban FROM empleado ORDER BY apellidos, nombre";
        Connection conn = Database.obtenerConexion();
        if (conn == null) {
            mensajeError = "No hay conexión con la base de datos.";
            return null;
        }
        List<Empleado> resultado = new ArrayList<>();
        try (
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
        ) {
            while (rs.next()) {
                resultado.add(
                    new Empleado(
                        rs.getString("nss"),
                        rs.getString("nombre"),
                        rs.getString("apellidos"),
                        rs.getString("email"),
                        rs.getString("iban")
                    )
                );
            }
            return resultado;
        } catch (SQLException ex) {
            mensajeError = "Error leyendo la lista de empleados: " + ex.getMessage();
            return null;
        }
    }
}
