package edu.upb.crypto.trep.DataBase.models;

public class Votante {
    private String codigo;
    private String llavePrivada;

    public Votante(String codigo, String llavePrivada) {
        this.codigo = codigo;
        this.llavePrivada = llavePrivada;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getLlavePrivada() {
        return llavePrivada;
    }
}