package com.sqlpractica.model;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TipoPlaza)) return false;
        TipoPlaza t = (TipoPlaza) o;
        return Objects.equals(nombre, t.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre);
    }
}
