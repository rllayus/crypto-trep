package edu.upb.crypto.trep.bl;

import edu.upb.crypto.trep.DataBase.models.Candidato;
import lombok.Getter;
import lombok.Setter;

import java.util.regex.Pattern;

@Getter
@Setter
public class AltaCandidato extends Comando {

    private Candidato candidato;

    public AltaCandidato(Candidato candidato) {
        this.setCodigoComando("0003");
        this.candidato = candidato;
    }

    public AltaCandidato(String ip) {
        super();
        this.setCodigoComando("0003");
        setIp(ip);
        this.candidato = new Candidato();
    }

    @Override
    public void parsear(String comando) {
        String[] tokens = comando.split(Pattern.quote("|"));
        if (tokens.length == 2) {
            setCodigoComando(tokens[0]);
            candidato = new Candidato(tokens[1]);
        }
    }

    @Override
    public String getComando() {
        return getCodigoComando() + "|" + candidato.toString() + System.lineSeparator();
    }

    @Override
    public String toString() {
        return getCodigoComando() + "|" + candidato.toString() + System.lineSeparator();
    }
}
