package com.sqlpractica.model;

/**
 * Tipo de plaza (categoría laboral): nombre (PK) y función.
 */
public class TipoPlaza {

    private String nombre;
    private String funcion;

    public TipoPlaza() {
    }

    public TipoPlaza(String nombre, String funcion) {
        this.nombre = nombre;
        this.funcion = funcion;
    }
}
