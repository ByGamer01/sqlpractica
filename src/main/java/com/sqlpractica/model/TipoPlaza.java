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

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getFuncion() { return funcion; }
    public void setFuncion(String funcion) { this.funcion = funcion; }

    @Override
    public String toString() {
        return nombre;
    }
}
