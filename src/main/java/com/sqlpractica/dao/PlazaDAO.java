package com.sqlpractica.dao;

import com.sqlpractica.db.Database;
import com.sqlpractica.exception.DAOException;
import com.sqlpractica.model.Plaza;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class PlazaDAO {

    public void insert(Plaza p) throws DAOException {
        String sql = "INSERT INTO plaza(codigo, nombre, salario, codigo_plaza_supervisora, " +
                     "informe_supervision, nombre_tipo_plaza) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = Database.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getCodigo());
            ps.setString(2, p.getNombre());
            ps.setDouble(3, p.getSalario());
            setNullableString(ps, 4, p.getCodigoPlazaSupervisora());
            setNullableString(ps, 5, p.getInformeSupervision());
            ps.setString(6, p.getNombreTipoPlaza());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("No se ha podido crear la plaza: " + e.getMessage(), e);
        }
    }

    public void update(Plaza p) throws DAOException {
        String sql = "UPDATE plaza SET nombre = ?, salario = ?, codigo_plaza_supervisora = ?, " +
                     "informe_supervision = ?, nombre_tipo_plaza = ? WHERE codigo = ?";
        Connection conn = Database.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setDouble(2, p.getSalario());
            setNullableString(ps, 3, p.getCodigoPlazaSupervisora());
            setNullableString(ps, 4, p.getInformeSupervision());
            ps.setString(5, p.getNombreTipoPlaza());
            ps.setString(6, p.getCodigo());
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new DAOException("No existe ninguna plaza con código '" + p.getCodigo() + "'.");
            }
        } catch (SQLException e) {
            throw new DAOException("No se ha podido actualizar la plaza: " + e.getMessage(), e);
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
