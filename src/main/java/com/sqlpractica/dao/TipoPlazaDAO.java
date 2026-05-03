package com.sqlpractica.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sqlpractica.DAOException;
import com.sqlpractica.Database;
import com.sqlpractica.model.TipoPlaza;

/**
 * DAO de la tabla 'tipo_plaza'. Misma idea que EmpleadoDAO pero
 * con menos columnas: solo nombre (PK) y funcion.
 *
 * Si intentamos eliminar un tipo que está siendo usado por alguna
 * plaza, SQLite lanzará un error gracias al FOREIGN KEY ... ON DELETE
 * RESTRICT del esquema. Lo capturamos y lo convertimos en DAOException.
 */
public class TipoPlazaDAO {

    public void insertar(TipoPlaza t) throws DAOException {
        String sql = "INSERT INTO tipo_plaza(nombre, funcion) VALUES (?, ?)";
        Connection conn = Database.obtenerConexion();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, t.getNombre());
            ps.setString(2, t.getFuncion());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("No se ha podido crear el tipo de plaza: " + e.getMessage(), e);
        }
    }

    /**
     * Solo actualizamos la 'funcion'. El nombre es PK, no se puede
     * cambiar (haría falta borrar y volver a crear).
     */
    public void actualizar(TipoPlaza t) throws DAOException {
        String sql = "UPDATE tipo_plaza SET funcion = ? WHERE nombre = ?";
        Connection conn = Database.obtenerConexion();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, t.getFuncion());
            ps.setString(2, t.getNombre());
            int filas = ps.executeUpdate();
            if (filas == 0) {
                throw new DAOException("No existe ningún tipo de plaza con nombre '" + t.getNombre() + "'.");
            }
        } catch (SQLException e) {
            throw new DAOException("No se ha podido actualizar el tipo de plaza: " + e.getMessage(), e);
        }
    }

    public void eliminar(String nombre) throws DAOException {
        String sql = "DELETE FROM tipo_plaza WHERE nombre = ?";
        Connection conn = Database.obtenerConexion();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nombre);
            int filas = ps.executeUpdate();
            if (filas == 0) {
                throw new DAOException("No existe ningún tipo de plaza con nombre '" + nombre + "'.");
            }
        } catch (SQLException e) {
            // Aquí suele caer el error "FOREIGN KEY constraint failed"
            // si hay plazas usando este tipo (ON DELETE RESTRICT).
            throw new DAOException("No se ha podido eliminar el tipo de plaza: " + e.getMessage(), e);
        }
    }

    public List<TipoPlaza> obtenerTodos() throws DAOException {
        String sql = "SELECT nombre, funcion FROM tipo_plaza ORDER BY nombre";
        Connection conn = Database.obtenerConexion();
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
