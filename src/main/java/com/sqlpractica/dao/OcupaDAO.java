package com.sqlpractica.dao;

import com.sqlpractica.DAOException;
import com.sqlpractica.Database;
import com.sqlpractica.model.Ocupa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO de la tabla 'ocupa' (relación N:M empleado-plaza).
 *
 * IMPORTANTE: la PK es COMPUESTA, así que para identificar una fila
 * (en update y delete) necesitamos las DOS claves: nss_empleado y
 * codigo_plaza.
 */
public class OcupaDAO {

    public void insertar(Ocupa o) throws DAOException {
        String sql = "INSERT INTO ocupa(nss_empleado, codigo_plaza, fecha_inicio, fecha_fin) VALUES (?, ?, ?, ?)";
        Connection conn = Database.obtenerConexion();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, o.obtenerNssEmpleado());
            ps.setString(2, o.obtenerCodigoPlaza());
            ps.setString(3, o.obtenerFechaInicio());
            // fecha_fin es opcional -> puede ir como NULL.
            asignarTextoOpcional(ps, 4, o.obtenerFechaFin());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("No se ha podido crear la ocupación: " + e.getMessage(), e);
        }
    }

    /**
     * Solo se pueden cambiar las fechas; las claves (NSS y codigo_plaza)
     * van en el WHERE para localizar la fila.
     */
    public void actualizar(Ocupa o) throws DAOException {
        String sql = "UPDATE ocupa SET fecha_inicio = ?, fecha_fin = ? WHERE nss_empleado = ? AND codigo_plaza = ?";
        Connection conn = Database.obtenerConexion();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, o.obtenerFechaInicio());
            asignarTextoOpcional(ps, 2, o.obtenerFechaFin());
            ps.setString(3, o.obtenerNssEmpleado());
            ps.setString(4, o.obtenerCodigoPlaza());
            int filas = ps.executeUpdate();
            if (filas == 0) {
                throw new DAOException("No existe ninguna ocupación para este empleado y plaza.");
            }
        } catch (SQLException e) {
            throw new DAOException("No se ha podido actualizar la ocupación: " + e.getMessage(), e);
        }
    }

    /**
     * eliminar recibe LAS DOS partes de la clave compuesta.
     */
    public void eliminar(String nssEmpleado, String codigoPlaza) throws DAOException {
        String sql = "DELETE FROM ocupa WHERE nss_empleado = ? AND codigo_plaza = ?";
        Connection conn = Database.obtenerConexion();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nssEmpleado);
            ps.setString(2, codigoPlaza);
            int filas = ps.executeUpdate();
            if (filas == 0) {
                throw new DAOException("No existe ninguna ocupación con esa combinación.");
            }
        } catch (SQLException e) {
            throw new DAOException("No se ha podido eliminar la ocupación: " + e.getMessage(), e);
        }
    }

    public List<Ocupa> obtenerTodos() throws DAOException {
        // Ordenamos por fecha_inicio descendente: las más recientes arriba.
        String sql = "SELECT nss_empleado, codigo_plaza, fecha_inicio, fecha_fin FROM ocupa " +
                     "ORDER BY fecha_inicio DESC";
        Connection conn = Database.obtenerConexion();
        List<Ocupa> resultado = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                resultado.add(new Ocupa(
                        rs.getString("nss_empleado"),
                        rs.getString("codigo_plaza"),
                        rs.getString("fecha_inicio"),
                        rs.getString("fecha_fin")
                ));
            }
        } catch (SQLException e) {
            throw new DAOException("Error leyendo las ocupaciones: " + e.getMessage(), e);
        }
        return resultado;
    }

    private void asignarTextoOpcional(PreparedStatement ps, int idx, String valor) throws SQLException {
        if (valor == null || valor.isBlank()) {
            ps.setNull(idx, Types.VARCHAR);
        } else {
            ps.setString(idx, valor);
        }
    }
}
