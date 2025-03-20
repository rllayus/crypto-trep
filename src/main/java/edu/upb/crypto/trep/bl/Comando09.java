package edu.upb.crypto.trep.bl;

import edu.upb.crypto.trep.DataBase.models.Voto;
import lombok.Getter;
import lombok.Setter;

import java.util.regex.Pattern;

@Getter
@Setter
public class Comando09 extends Comando {
    public static String CODIGO_COMANDO = "0009";
    private Voto voto;
    private String firma;
    private int cantidadConfirmaciones;

    public Comando09(Voto voto, String firma) {
        this.setCodigoComando(CODIGO_COMANDO);
        this.voto = voto;
        this.cantidadConfirmaciones = 0;
        this.firma = firma;
    }

    public Comando09(String ip) {
        super();
        this.setCodigoComando(CODIGO_COMANDO);
        setIp(ip);
        this.voto = null;
        this.cantidadConfirmaciones = 0;
    }


    @Override
    public void parsear(String comando) {
        String[] tokens = comando.split(Pattern.quote("|"));
        System.out.println("Longitud :" + tokens.length);
        if (tokens.length >= 2) {
            System.out.println("Comando09");
            setCodigoComando(tokens[0]);
            String[] votoArray = tokens[1].split(",");
            if(votoArray.length == 3) {
                this.firma = tokens[2];
            }
            this.voto = new Voto(votoArray[0], Long.parseLong(votoArray[1]), votoArray[2], votoArray[3]);
        }
    }

    @Override
    public String getComando() {
        return getCodigoComando() + "|" + voto.toString() + "|" + firma + System.lineSeparator();
    }

}
