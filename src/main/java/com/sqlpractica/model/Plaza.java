package com.sqlpractica.model;

import java.util.Objects;

/**
 * Modelo de la tabla 'plaza' (puesto de trabajo).
 *
 * LÓGICA / RELACIONES:
 *   - PK = codigo.
 *   - codigoPlazaSupervisora: AUTO-RELACIÓN -> apunta a otra plaza
 *     (la que la supervisa). Es opcional (puede ser null).
 *   - nombreTipoPlaza: FK obligatoria a tipo_plaza(nombre).
 *   - informeSupervision: texto libre, opcional.
 */
public class Plaza {

    private String codigo;                  // PK
    private String nombre;
    private double salario;
    private String codigoPlazaSupervisora;  // FK a plaza (opcional)
    private String informeSupervision;      // opcional
    private String nombreTipoPlaza;         // FK a tipo_plaza (obligatoria)

    public Plaza() {
    }

    public Plaza(String codigo,
                 String nombre,
                 double salario,
                 String codigoPlazaSupervisora,
                 String informeSupervision,
                 String nombreTipoPlaza) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.salario = salario;
        this.codigoPlazaSupervisora = codigoPlazaSupervisora;
        this.informeSupervision = informeSupervision;
        this.nombreTipoPlaza = nombreTipoPlaza;
    }

    public String obtenerCodigo() { return codigo; }
    public void asignarCodigo(String codigo) { this.codigo = codigo; }

    public String obtenerNombre() { return nombre; }
    public void asignarNombre(String nombre) { this.nombre = nombre; }

    public double obtenerSalario() { return salario; }
    public void asignarSalario(double salario) { this.salario = salario; }

    public String obtenerCodigoPlazaSupervisora() { return codigoPlazaSupervisora; }
    public void asignarCodigoPlazaSupervisora(String codigoPlazaSupervisora) {
        this.codigoPlazaSupervisora = codigoPlazaSupervisora;
    }

    public String obtenerInformeSupervision() { return informeSupervision; }
    public void asignarInformeSupervision(String informeSupervision) {
        this.informeSupervision = informeSupervision;
    }

    public String obtenerNombreTipoPlaza() { return nombreTipoPlaza; }
    public void asignarNombreTipoPlaza(String nombreTipoPlaza) {
        this.nombreTipoPlaza = nombreTipoPlaza;
    }

    // Igualdad por la PK (codigo).
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Plaza)) return false;
        return Objects.equals(codigo, ((Plaza) o).codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }
}
