package com.sqlpractica.dao;

import com.sqlpractica.db.Database;
import com.sqlpractica.exception.DAOException;
import com.sqlpractica.model.Nomina;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class NominaDAO {

    public void insert(Nomina n) throws DAOException {
        String sql = "INSERT INTO nomina(iban_pago, importe_pago, nss_empleado, codigo_plaza) " +
                     "VALUES (?, ?, ?, ?)";
        Connection conn = Database.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, n.getIbanPago());
            ps.setDouble(2, n.getImportePago());
            ps.setString(3, n.getNssEmpleado());
            ps.setString(4, n.getCodigoPlaza());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    n.setId(keys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("No se ha podido crear la nómina: " + e.getMessage(), e);
        }
    }
}
