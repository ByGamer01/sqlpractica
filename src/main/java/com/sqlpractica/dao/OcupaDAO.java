package com.sqlpractica.dao;

import com.sqlpractica.db.Database;
import com.sqlpractica.exception.DAOException;
import com.sqlpractica.model.Ocupa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class OcupaDAO {

    public void insert(Ocupa o) throws DAOException {
        String sql = "INSERT INTO ocupa(nss_empleado, codigo_plaza, fecha_inicio, fecha_fin) VALUES (?, ?, ?, ?)";
        Connection conn = Database.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, o.getNssEmpleado());
            ps.setString(2, o.getCodigoPlaza());
            ps.setString(3, o.getFechaInicio());
            setNullableString(ps, 4, o.getFechaFin());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("No se ha podido crear la ocupación: " + e.getMessage(), e);
        }
    }

    private void setNullableString(PreparedStatement ps, int idx, String value) throws SQLException {
        if (value == null || value.isBlank()) {
            ps.setNull(idx, Types.VARCHAR);
        } else {
            ps.setString(idx, value);
        }
    }
}
