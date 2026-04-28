package com.sqlpractica.model;

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
}
