package edu.upb.crypto.trep.bl;

import edu.upb.crypto.trep.DataBase.models.Voto;
import lombok.Getter;
import lombok.Setter;

import java.util.regex.Pattern;

@Getter
@Setter
public class Comando010 extends Comando {
    public static String CODIGO_COMANDO = "0010";
    private String idVoto;
    private boolean estadoConfirmacion;

    public Comando010(Voto voto, boolean estadoConfirmacion) {
        this.setCodigoComando(CODIGO_COMANDO);
        this.idVoto = voto.getId();
        this.estadoConfirmacion = estadoConfirmacion;
    }

    public Comando010(String ip) {
        super();
        this.setCodigoComando(CODIGO_COMANDO);
        setIp(ip);
        this.idVoto = null;
        this.estadoConfirmacion = false;
    }


    @Override
    public void parsear(String comando) {
        String[] tokens = comando.split(Pattern.quote("|"));
        if (tokens.length == 2) {
            setCodigoComando(tokens[0]);
            String[] votoArray = tokens[1].split(",");
            this.idVoto = votoArray[0];
            this.estadoConfirmacion = Boolean.parseBoolean(votoArray[1]);
        }
    }

    @Override
    public String getComando() {
        return getCodigoComando() + "|" + idVoto + "," + estadoConfirmacion + System.lineSeparator();
    }

    @Override
    public String toString() {
        return getCodigoComando() + "|" + idVoto + "," + estadoConfirmacion + System.lineSeparator();
    }
}
