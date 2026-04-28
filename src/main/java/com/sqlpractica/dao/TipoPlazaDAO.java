package com.sqlpractica.dao;

import com.sqlpractica.db.Database;
import com.sqlpractica.exception.DAOException;
import com.sqlpractica.model.TipoPlaza;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TipoPlazaDAO {

    public void insert(TipoPlaza t) throws DAOException {
        String sql = "INSERT INTO tipo_plaza(nombre, funcion) VALUES (?, ?)";
        Connection conn = Database.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, t.getNombre());
            ps.setString(2, t.getFuncion());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("No se ha podido crear el tipo de plaza: " + e.getMessage(), e);
        }
    }
}
