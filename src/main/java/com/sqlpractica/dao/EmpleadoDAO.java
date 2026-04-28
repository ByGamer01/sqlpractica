package com.sqlpractica.dao;

import com.sqlpractica.db.Database;
import com.sqlpractica.exception.DAOException;
import com.sqlpractica.model.Empleado;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Acceso a datos para la tabla 'empleado'. Utiliza PreparedStatement
 * para evitar SQL injection y envuelve cada error SQL en {@link DAOException}.
 */
public class EmpleadoDAO {

    public void insert(Empleado e) throws DAOException {
        String sql = "INSERT INTO empleado(nss, nombre, apellidos, email, iban) VALUES (?, ?, ?, ?, ?)";
        Connection conn = Database.getConnection();
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

    public void update(Empleado e) throws DAOException {
        String sql = "UPDATE empleado SET nombre = ?, apellidos = ?, email = ?, iban = ? WHERE nss = ?";
        Connection conn = Database.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, e.getNombre());
            ps.setString(2, e.getApellidos());
            ps.setString(3, e.getEmail());
            ps.setString(4, e.getIban());
            ps.setString(5, e.getNss());
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new DAOException("No existe ningún empleado con NSS '" + e.getNss() + "'.");
            }
        } catch (SQLException ex) {
            throw new DAOException("No se ha podido actualizar el empleado: " + ex.getMessage(), ex);
        }
    }

    public void delete(String nss) throws DAOException {
        String sql = "DELETE FROM empleado WHERE nss = ?";
        Connection conn = Database.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nss);
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new DAOException("No existe ningún empleado con NSS '" + nss + "'.");
            }
        } catch (SQLException ex) {
            throw new DAOException("No se ha podido eliminar el empleado: " + ex.getMessage(), ex);
        }
    }

    public Empleado findByNss(String nss) throws DAOException {
        String sql = "SELECT nss, nombre, apellidos, email, iban FROM empleado WHERE nss = ?";
        Connection conn = Database.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nss);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
                return null;
            }
        } catch (SQLException ex) {
            throw new DAOException("Error consultando el empleado: " + ex.getMessage(), ex);
        }
    }

    private Empleado mapRow(ResultSet rs) throws SQLException {
        return new Empleado(
                rs.getString("nss"),
                rs.getString("nombre"),
                rs.getString("apellidos"),
                rs.getString("email"),
                rs.getString("iban")
        );
    }
}
