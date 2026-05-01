package com.sqlpractica.dao;

import com.sqlpractica.DAOException;
import com.sqlpractica.Database;
import com.sqlpractica.model.Nomina;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO de la tabla 'nomina'.
 *
 * NOVEDAD: el id es AUTOINCREMENT (lo genera SQLite). Por eso, al
 * insertar, pedimos a JDBC que nos devuelva la "clave generada" con
 * Statement.RETURN_GENERATED_KEYS y se la metemos al objeto Nomina
 * con asignarId(). De esta forma, después del insert, el objeto en
 * memoria queda con su ID real.
 *
 * Doc claves generadas:
 *   https://docs.oracle.com/javase/tutorial/jdbc/basics/retrieving.html
 */
public class NominaDAO {

    public void insertar(Nomina n) throws DAOException {
        String sql = "INSERT INTO nomina(iban_pago, importe_pago, nss_empleado, codigo_plaza) " +
                     "VALUES (?, ?, ?, ?)";
        Connection conn = Database.obtenerConexion();
        // Pedimos a JDBC que recuerde la clave que SQLite va a generar.
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, n.obtenerIbanPago());
            ps.setDouble(2, n.obtenerImportePago());
            ps.setString(3, n.obtenerNssEmpleado());
            ps.setString(4, n.obtenerCodigoPlaza());
            ps.executeUpdate();

            // Recogemos el id generado y lo guardamos en el objeto.
            try (ResultSet claves = ps.getGeneratedKeys()) {
                if (claves.next()) {
                    n.asignarId(claves.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new DAOException("No se ha podido crear la nómina: " + e.getMessage(), e);
        }
    }

    /**
     * Para actualizar, necesitamos sí o sí el id (es la PK).
     * Si llega null lo tratamos como error de programación.
     */
    public void actualizar(Nomina n) throws DAOException {
        if (n.obtenerId() == null) {
            throw new DAOException("Para actualizar una nómina hay que indicar el ID.");
        }
        String sql = "UPDATE nomina SET iban_pago = ?, importe_pago = ?, " +
                     "nss_empleado = ?, codigo_plaza = ? WHERE id = ?";
        Connection conn = Database.obtenerConexion();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, n.obtenerIbanPago());
            ps.setDouble(2, n.obtenerImportePago());
            ps.setString(3, n.obtenerNssEmpleado());
            ps.setString(4, n.obtenerCodigoPlaza());
            ps.setInt(5, n.obtenerId());
            int filas = ps.executeUpdate();
            if (filas == 0) {
                throw new DAOException("No existe ninguna nómina con ID " + n.obtenerId() + ".");
            }
        } catch (SQLException e) {
            throw new DAOException("No se ha podido actualizar la nómina: " + e.getMessage(), e);
        }
    }

    public void eliminar(int id) throws DAOException {
        String sql = "DELETE FROM nomina WHERE id = ?";
        Connection conn = Database.obtenerConexion();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int filas = ps.executeUpdate();
            if (filas == 0) {
                throw new DAOException("No existe ninguna nómina con ID " + id + ".");
            }
        } catch (SQLException e) {
            throw new DAOException("No se ha podido eliminar la nómina: " + e.getMessage(), e);
        }
    }

    public List<Nomina> obtenerTodos() throws DAOException {
        // ORDER BY id DESC: las nóminas más nuevas (id mayor) primero.
        String sql = "SELECT id, iban_pago, importe_pago, nss_empleado, codigo_plaza " +
                     "FROM nomina ORDER BY id DESC";
        Connection conn = Database.obtenerConexion();
        List<Nomina> resultado = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                resultado.add(new Nomina(
                        rs.getInt("id"),
                        rs.getString("iban_pago"),
                        rs.getDouble("importe_pago"),
                        rs.getString("nss_empleado"),
                        rs.getString("codigo_plaza")
                ));
            }
        } catch (SQLException e) {
            throw new DAOException("Error leyendo las nóminas: " + e.getMessage(), e);
        }
        return resultado;
    }
}
