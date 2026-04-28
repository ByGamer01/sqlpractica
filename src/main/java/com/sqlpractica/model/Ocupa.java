package com.sqlpractica.model;

/**
 * Relación "ocupa" entre un empleado y una plaza, con fechas de inicio y fin.
 * La clave primaria es compuesta: (NSS empleado, código plaza).
 */
public class Ocupa {

    private String nssEmpleado;
    private String codigoPlaza;
    private String fechaInicio;
    private String fechaFin;

    public Ocupa() {
    }
}
