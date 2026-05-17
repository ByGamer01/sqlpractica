package com.sqlpractica.model;

import java.util.Objects;

/**
 * Modelo de la tabla 'nomina'
 *
 * LÓGICA:
 *   - Cada nómina es un pago hecho a un empleado por una plaza concreta
 *   - PK = id (entero AUTOINCREMENT generado por SQLite)
 *   - Como el id lo da la base de datos, al CREAR una nómina nueva
 *     pasamos id = null y el DAO lo rellena después
 *   - FK nssEmpleado -> empleado(nss)
 *   - FK codigoPlaza -> plaza(codigo)
 */
public class Nomina {

  // Integer (no int) para poder valer null cuando aun no se ha insertado
  private Integer id;
  private String ibanPago;
  private double importePago;
  private String nssEmpleado;
  private String codigoPlaza;

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
  public void setNssEmpleado(String nssEmpleado) {
    this.nssEmpleado = nssEmpleado;
  }

  public String getCodigoPlaza() { return codigoPlaza; }
  public void setCodigoPlaza(String codigoPlaza) {
    this.codigoPlaza = codigoPlaza;
  }

  // Comparamos por la PK (id)
  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof Nomina))
      return false;
    return Objects.equals(id, ((Nomina)o).id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
