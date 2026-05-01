package com.sqlpractica.model;

import java.util.Objects;

/**
 * Modelo de la tabla 'ocupa' (relación N:M entre empleado y plaza).
 *
 * LÓGICA:
 *   - Indica QUÉ empleado ocupa QUÉ plaza, y en qué fechas.
 *   - PK COMPUESTA: (nssEmpleado, codigoPlaza). Ningún empleado puede
 *     ocupar la misma plaza dos veces a la vez.
 *   - fechaInicio obligatoria, fechaFin opcional (null = sigue ocupándola).
 *   - Las fechas se guardan como TEXT en SQLite con formato yyyy-MM-dd
 *     (lo más portable; la validación se hace en la UI con LocalDate.parse).
 */
public class Ocupa {

    private String nssEmpleado;   // FK a empleado (parte de la PK)
    private String codigoPlaza;   // FK a plaza   (parte de la PK)
    private String fechaInicio;   // yyyy-MM-dd
    private String fechaFin;      // yyyy-MM-dd o null

    public Ocupa() {
    }

    public Ocupa(String nssEmpleado, String codigoPlaza, String fechaInicio, String fechaFin) {
        this.nssEmpleado = nssEmpleado;
        this.codigoPlaza = codigoPlaza;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public String obtenerNssEmpleado() { return nssEmpleado; }
    public void asignarNssEmpleado(String nssEmpleado) { this.nssEmpleado = nssEmpleado; }

    public String obtenerCodigoPlaza() { return codigoPlaza; }
    public void asignarCodigoPlaza(String codigoPlaza) { this.codigoPlaza = codigoPlaza; }

    public String obtenerFechaInicio() { return fechaInicio; }
    public void asignarFechaInicio(String fechaInicio) { this.fechaInicio = fechaInicio; }

    public String obtenerFechaFin() { return fechaFin; }
    public void asignarFechaFin(String fechaFin) { this.fechaFin = fechaFin; }

    // La PK es compuesta -> usamos las DOS columnas en equals/hashCode.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ocupa)) return false;
        Ocupa otro = (Ocupa) o;
        return Objects.equals(nssEmpleado, otro.nssEmpleado)
                && Objects.equals(codigoPlaza, otro.codigoPlaza);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nssEmpleado, codigoPlaza);
    }
}
