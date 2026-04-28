package com.sqlpractica.model;

import java.util.Objects;

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

    public Ocupa(String nssEmpleado, String codigoPlaza, String fechaInicio, String fechaFin) {
        this.nssEmpleado = nssEmpleado;
        this.codigoPlaza = codigoPlaza;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public String getNssEmpleado() { return nssEmpleado; }
    public void setNssEmpleado(String nssEmpleado) { this.nssEmpleado = nssEmpleado; }

    public String getCodigoPlaza() { return codigoPlaza; }
    public void setCodigoPlaza(String codigoPlaza) { this.codigoPlaza = codigoPlaza; }

    public String getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(String fechaInicio) { this.fechaInicio = fechaInicio; }

    public String getFechaFin() { return fechaFin; }
    public void setFechaFin(String fechaFin) { this.fechaFin = fechaFin; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ocupa)) return false;
        Ocupa other = (Ocupa) o;
        return Objects.equals(nssEmpleado, other.nssEmpleado)
                && Objects.equals(codigoPlaza, other.codigoPlaza);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nssEmpleado, codigoPlaza);
    }
}
