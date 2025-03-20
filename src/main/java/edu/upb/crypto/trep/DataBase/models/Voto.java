package edu.upb.crypto.trep.DataBase.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Voto {
    private String id;
    private long timestamp;
    private String codigoVotante;
    private String codigoCandidato;

    public Voto(String id, long timestamp, String codigoVotante, String codigoCandidato) {
        this.id = id;
        this.timestamp = timestamp;
        this.codigoVotante = codigoVotante;
        this.codigoCandidato = codigoCandidato;
    }
    @Override
    public String toString() {
        return id + "," + timestamp + "," + codigoVotante + "," + codigoCandidato;
    }
}
