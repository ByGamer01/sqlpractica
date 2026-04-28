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
}
