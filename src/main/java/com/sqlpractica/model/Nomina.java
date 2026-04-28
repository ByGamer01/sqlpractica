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

    public Nomina(Integer id, String ibanPago, double importePago,
                  String nssEmpleado, String codigoPlaza) {
        this.id = id;
        this.ibanPago = ibanPago;
        this.importePago = importePago;
        this.nssEmpleado = nssEmpleado;
        this.codigoPlaza = codigoPlaza;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getIbanPago() { return ibanPago; }
    public void setIbanPago(String ibanPago) { this.ibanPago = ibanPago; }

    public double getImportePago() { return importePago; }
    public void setImportePago(double importePago) {
        this.importePago = importePago;
    }

    public String getNssEmpleado() { return nssEmpleado; }
    public void setNssEmpleado(String nssEmpleado) { this.nssEmpleado = nssEmpleado; }

    public String getCodigoPlaza() { return codigoPlaza; }
    public void setCodigoPlaza(String codigoPlaza) { this.codigoPlaza = codigoPlaza; }
}
