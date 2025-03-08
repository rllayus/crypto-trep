package edu.upb.crypto.trep.bl;

import edu.upb.crypto.trep.DataBase.models.Candidato;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@Getter
@Setter
public class SincronizacionCandidatos extends Comando{
    private List<Candidato> candidatoes;
    public SincronizacionCandidatos(List<Candidato> candidatos) {
        this.setCodigoComando("0002");
        this.candidatoes = candidatos;
    }

    public SincronizacionCandidatos(String ip){
        super();
        this.setCodigoComando("0002");
        setIp(ip);
        this.candidatoes = new ArrayList<>();
    }


    @Override
    public void parsear(String comando) {
        String[] tokens = comando.split(Pattern.quote("|"));
        if(tokens.length == 2){
            setCodigoComando(tokens[0]);
            String[] contAry = tokens[1].split(";");
            for (String s : contAry) {
                this.candidatoes.add(new Candidato(s));
            }
        }
    }

    @Override
    public String getComando() {
        return getCodigoComando()+"|"+ipsToString() + System.lineSeparator();
    }
    private String ipsToString(){
        if(candidatoes.isEmpty()) return "";

        StringBuilder str = new StringBuilder();
        str.append(candidatoes.getFirst().toString());
        for (int i = 1; i < candidatoes.size(); i++) {
            str.append(";");
            str.append(candidatoes.get(i).toString());
        }

        return str.toString();
    }
}
