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

    public void update(TipoPlaza t) throws DAOException {
        String sql = "UPDATE tipo_plaza SET funcion = ? WHERE nombre = ?";
        Connection conn = Database.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, t.getFuncion());
            ps.setString(2, t.getNombre());
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new DAOException("No existe ningún tipo de plaza con nombre '" + t.getNombre() + "'.");
            }
        } catch (SQLException e) {
            throw new DAOException("No se ha podido actualizar el tipo de plaza: " + e.getMessage(), e);
        }
    }

    public void delete(String nombre) throws DAOException {
        String sql = "DELETE FROM tipo_plaza WHERE nombre = ?";
        Connection conn = Database.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new DAOException("No existe ningún tipo de plaza con nombre '" + nombre + "'.");
            }
        } catch (SQLException e) {
            throw new DAOException("No se ha podido eliminar el tipo de plaza (puede haber plazas que lo usen): " + e.getMessage(), e);
        }
    }

    public List<TipoPlaza> findAll() throws DAOException {
        String sql = "SELECT nombre, funcion FROM tipo_plaza ORDER BY nombre";
        Connection conn = Database.getConnection();
        List<TipoPlaza> resultado = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                resultado.add(new TipoPlaza(rs.getString("nombre"), rs.getString("funcion")));
            }
        } catch (SQLException e) {
            throw new DAOException("Error leyendo los tipos de plaza: " + e.getMessage(), e);
        }
        return resultado;
    }
}
