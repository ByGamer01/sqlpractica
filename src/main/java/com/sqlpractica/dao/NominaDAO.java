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

    public void update(Nomina n) throws DAOException {
        if (n.getId() == null) {
            throw new DAOException("Para actualizar una nómina hay que indicar el ID.");
        }
        String sql = "UPDATE nomina SET iban_pago = ?, importe_pago = ?, " +
                     "nss_empleado = ?, codigo_plaza = ? WHERE id = ?";
        Connection conn = Database.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, n.getIbanPago());
            ps.setDouble(2, n.getImportePago());
            ps.setString(3, n.getNssEmpleado());
            ps.setString(4, n.getCodigoPlaza());
            ps.setInt(5, n.getId());
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new DAOException("No existe ninguna nómina con ID " + n.getId() + ".");
            }
        } catch (SQLException e) {
            throw new DAOException("No se ha podido actualizar la nómina: " + e.getMessage(), e);
        }
    }

    public void delete(int id) throws DAOException {
        String sql = "DELETE FROM nomina WHERE id = ?";
        Connection conn = Database.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new DAOException("No existe ninguna nómina con ID " + id + ".");
            }
        } catch (SQLException e) {
            throw new DAOException("No se ha podido eliminar la nómina: " + e.getMessage(), e);
        }
    }
}
