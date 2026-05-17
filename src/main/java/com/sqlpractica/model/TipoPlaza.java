package com.sqlpractica.model;

import java.util.Objects;

/**
 * Modelo de la tabla 'tipo_plaza'
 *
 * LÓGICA:
 *   - Catálogo de categorías laborales (p.ej. "Conserje", "Profesor")
 *   - PK = nombre. Es la tabla a la que apunta plaza.nombre_tipo_plaza
 *   - POJO simple: atributos + obtener/asignar
 */
public class TipoPlaza {

  private String nombre;  // PK
  private String funcion; // descripción del trabajo

  public TipoPlaza(String nombre, String funcion) {
    this.nombre = nombre;
    this.funcion = funcion;
  }

  public String getNombre() { return nombre; }
  public void setNombre(String nombre) { this.nombre = nombre; }

  public String getFuncion() { return funcion; }
  public void setFuncion(String funcion) { this.funcion = funcion; }

  // Comparamos por la PK (nombre)
  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof TipoPlaza))
      return false;

    return Objects.equals(nombre, ((TipoPlaza)o).nombre);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nombre);
  }
}
