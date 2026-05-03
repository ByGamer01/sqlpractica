package com.sqlpractica.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.sqlpractica.DAOException;
import com.sqlpractica.Database;
import com.sqlpractica.model.Plaza;

/**
 * DAO de la tabla 'plaza'.
 *
 * NOVEDAD respecto a EmpleadoDAO: dos columnas pueden ser NULL
 * (codigo_plaza_supervisora e informe_supervision). Por eso usamos
 * el helper asignarTextoOpcional(): si el campo viene vacío, en vez
 * de meter "" en la BD, mete NULL de verdad con ps.setNull(...).
 */
public class PlazaDAO {

    public void insertar(Plaza p) throws DAOException {
        String sql = "INSERT INTO plaza(codigo, nombre, salario, codigo_plaza_supervisora, " +
                     "informe_supervision, nombre_tipo_plaza) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = Database.obtenerConexion();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getCodigo());
            ps.setString(2, p.getNombre());
            ps.setDouble(3, p.getSalario());
            asignarTextoOpcional(ps, 4, p.getCodigoPlazaSupervisora());
            asignarTextoOpcional(ps, 5, p.getInformeSupervision());
            ps.setString(6, p.getNombreTipoPlaza());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("No se ha podido crear la plaza: " + e.getMessage(), e);
        }
    }

    public void actualizar(Plaza p) throws DAOException {
        // No tocamos la PK 'codigo' (va en el WHERE), sí cambian el resto.
        String sql = "UPDATE plaza SET nombre = ?, salario = ?, codigo_plaza_supervisora = ?, " +
                     "informe_supervision = ?, nombre_tipo_plaza = ? WHERE codigo = ?";
        Connection conn = Database.obtenerConexion();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, p.getNombre());
            ps.setDouble(2, p.getSalario());
            asignarTextoOpcional(ps, 3, p.getCodigoPlazaSupervisora());
            asignarTextoOpcional(ps, 4, p.getInformeSupervision());
            ps.setString(5, p.getNombreTipoPlaza());
            ps.setString(6, p.getCodigo());
            int filas = ps.executeUpdate();
            if (filas == 0) {
                throw new DAOException("No existe ninguna plaza con código '" + p.getCodigo() + "'.");
            }
        } catch (SQLException e) {
            throw new DAOException("No se ha podido actualizar la plaza: " + e.getMessage(), e);
        }
    }

    public void eliminar(String codigo) throws DAOException {
        String sql = "DELETE FROM plaza WHERE codigo = ?";
        Connection conn = Database.obtenerConexion();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, codigo);
            int filas = ps.executeUpdate();
            if (filas == 0) {
                throw new DAOException("No existe ninguna plaza con código '" + codigo + "'.");
            }
        } catch (SQLException e) {
            throw new DAOException("No se ha podido eliminar la plaza: " + e.getMessage(), e);
        }
    }

    public List<Plaza> obtenerTodos() throws DAOException {
        String sql = "SELECT codigo, nombre, salario, codigo_plaza_supervisora, " +
                     "informe_supervision, nombre_tipo_plaza FROM plaza ORDER BY codigo";
        Connection conn = Database.obtenerConexion();
        List<Plaza> resultado = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                // rs.getString devuelve null si la celda es NULL, así
                // que se mapea limpio al objeto Plaza.
                resultado.add(new Plaza(
                        rs.getString("codigo"),
                        rs.getString("nombre"),
                        rs.getDouble("salario"),
                        rs.getString("codigo_plaza_supervisora"),
                        rs.getString("informe_supervision"),
                        rs.getString("nombre_tipo_plaza")
                ));
            }
        } catch (SQLException e) {
            throw new DAOException("Error leyendo las plazas: " + e.getMessage(), e);
        }
        return resultado;
    }

    /**
     * Si el valor está vacío o es null, asignamos NULL en SQL.
     * Si no, lo asignamos como texto normal.
     * Esto evita que se guarden cadenas "" donde semánticamente
     * queremos NULL (p.ej. "esta plaza no tiene supervisora").
     */
    private void asignarTextoOpcional(PreparedStatement ps, int idx, String valor) throws SQLException {
        if (valor == null || valor.isBlank()) {
            ps.setNull(idx, Types.VARCHAR);
        } else {
            ps.setString(idx, valor);
        }
    }
}
