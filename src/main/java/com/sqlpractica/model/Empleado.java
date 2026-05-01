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

    // Constructor vacío: útil si en algún momento queremos crear el
    // objeto y rellenarlo después con los métodos asignar*.
    public Empleado() {
    }

    // Constructor con todos los campos: el que usamos al leer filas
    // de la BD o al crear un empleado nuevo desde el formulario.
    public Empleado(String nss, String nombre, String apellidos, String email, String iban) {
        this.nss = nss;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.iban = iban;
    }

    // Métodos "obtener" (getters) y "asignar" (setters) en español.
    public String obtenerNss() { return nss; }
    public void asignarNss(String nss) { this.nss = nss; }

    public String obtenerNombre() { return nombre; }
    public void asignarNombre(String nombre) { this.nombre = nombre; }

    public String obtenerApellidos() { return apellidos; }
    public void asignarApellidos(String apellidos) { this.apellidos = apellidos; }

    public String obtenerEmail() { return email; }
    public void asignarEmail(String email) { this.email = email; }

    public String obtenerIban() { return iban; }
    public void asignarIban(String iban) { this.iban = iban; }

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
