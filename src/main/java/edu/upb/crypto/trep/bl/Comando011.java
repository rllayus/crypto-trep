package edu.upb.crypto.trep.bl;

import edu.upb.crypto.trep.DataBase.models.Voto;
import lombok.Getter;
import lombok.Setter;

import java.util.regex.Pattern;

@Getter
@Setter
public class Comando011 extends Comando {
    public static String CODIGO_COMANDO = "0011";
    private String idVoto;


    public Comando011(String idVoto) {
        this.setCodigoComando(CODIGO_COMANDO);
        this.idVoto = idVoto;
    }

    public Comando011(String ip, String idVoto) {
        super();
        this.setCodigoComando(CODIGO_COMANDO);
        setIp(ip);
        this.idVoto = null;
    }


    @Override
    public void parsear(String comando) {
        String[] tokens = comando.split(Pattern.quote("|"));
        if (tokens.length == 2) {
            setCodigoComando(tokens[0]);
            this.idVoto = tokens[1];
        }
    }

    @Override
    public String getComando() {
        return getCodigoComando() + "|" + idVoto + System.lineSeparator();
    }

    @Override
    public String toString() {
        return getCodigoComando() + "|" + idVoto + System.lineSeparator();
    }
}
