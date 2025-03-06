package edu.upb.crypto.trep.models;

public class Candidato {
    private String id;
    private String nombre;

    public Candidato(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }
}