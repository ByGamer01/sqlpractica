package com.sqlpractica.model;

import java.util.Objects;

/**
 * Modelo que representa una FILA de la tabla 'empleado'.
 *
 * LÓGICA:
 *   - Es una clase POJO (Plain Old Java Object): solo tiene atributos
 *     privados y métodos para leer/escribirlos.
 *   - No habla con la base de datos: eso es trabajo de EmpleadoDAO.
 *   - Sirve para "viajar" entre el DAO y la UI: el DAO lee filas y
 *     devuelve Empleado, y la UI rellena un Empleado y se lo da al DAO.
 *
 * Campos según el diagrama:
 *   nss        -> clave primaria
 *   nombre, apellidos, email, iban -> datos del empleado
 */
public class Empleado {

    private String nss;
    private String nombre;
    private String apellidos;
    private String email;
    private String iban;

    // Constructor con todos los campos: el que usamos al leer filas
    // de la BD o al crear un empleado nuevo desde el formulario
    public Empleado(String nss, String nombre, String apellidos, String email, String iban) {
        this.nss = nss;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.iban = iban;
    }

    // getters y setters
    public String getNss() { return nss; }
    public void setNss(String nss) { this.nss = nss; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getIban() { return iban; }
    public void setIban(String iban) { this.iban = iban; }

    // equals + hashCode usan el NSS porque es la clave primaria:
    // dos empleados con el mismo NSS son el mismo empleado.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Empleado)) return false;

        return Objects.equals(nss, ((Empleado) o).nss);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nss);
    }
}
