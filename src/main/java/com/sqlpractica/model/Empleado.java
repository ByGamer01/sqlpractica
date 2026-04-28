package com.sqlpractica.model;

import java.util.Objects;

/**
 * Representa un empleado de la empresa.
 * Campos según el diagrama: NSS (PK), Nombre, Apellidos, Email, IBAN.
 */
public class Empleado {

    private String nss;
    private String nombre;
    private String apellidos;
    private String email;
    private String iban;

    public Empleado() {
    }

    public Empleado(String nss, String nombre, String apellidos, String email, String iban) {
        this.nss = nss;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.iban = iban;
    }

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

    @Override
    public String toString() {
        return nombre + " " + apellidos + " (" + nss + ")";
    }
}
