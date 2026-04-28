package com.sqlpractica.model;

import java.util.Objects;

/**
 * Plaza (puesto de trabajo). Tiene una referencia opcional a otra plaza
 * que actúa como supervisora (auto-relación) y una referencia obligatoria
 * a un tipo de plaza.
 */
public class Plaza {

    private String codigo;
    private String nombre;
    private double salario;
    private String codigoPlazaSupervisora;
    private String informeSupervision;
    private String nombreTipoPlaza;

    public Plaza() {
    }

    public Plaza(String codigo, String nombre, double salario,
                 String codigoPlazaSupervisora, String informeSupervision,
                 String nombreTipoPlaza) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.salario = salario;
        this.codigoPlazaSupervisora = codigoPlazaSupervisora;
        this.informeSupervision = informeSupervision;
        this.nombreTipoPlaza = nombreTipoPlaza;
    }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public double getSalario() { return salario; }
    public void setSalario(double salario) { this.salario = salario; }

    public String getCodigoPlazaSupervisora() { return codigoPlazaSupervisora; }
    public void setCodigoPlazaSupervisora(String codigoPlazaSupervisora) {
        this.codigoPlazaSupervisora = codigoPlazaSupervisora;
    }

    public String getInformeSupervision() { return informeSupervision; }
    public void setInformeSupervision(String informeSupervision) {
        this.informeSupervision = informeSupervision;
    }

    public String getNombreTipoPlaza() { return nombreTipoPlaza; }
    public void setNombreTipoPlaza(String nombreTipoPlaza) {
        this.nombreTipoPlaza = nombreTipoPlaza;
    }

    @Override
    public String toString() {
        return codigo + " - " + nombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Plaza)) return false;
        Plaza p = (Plaza) o;
        return Objects.equals(codigo, p.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }
}
