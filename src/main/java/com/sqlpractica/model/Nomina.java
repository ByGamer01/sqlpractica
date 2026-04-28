package com.sqlpractica.model;

/**
 * Nómina pagada a un empleado por el trabajo de una plaza concreta.
 * El ID es autoincremental.
 */
public class Nomina {

    private Integer id;
    private String ibanPago;
    private double importePago;
    private String nssEmpleado;
    private String codigoPlaza;

    public Nomina() {
    }
}
