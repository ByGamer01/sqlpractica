package com.sqlpractica.model;

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
}
