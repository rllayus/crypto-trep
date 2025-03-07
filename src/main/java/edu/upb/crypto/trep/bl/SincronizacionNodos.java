package edu.upb.crypto.trep.bl;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

@Getter
@Setter
public class SincronizacionNodos extends Comando{
    private List<String> ips;
    public SincronizacionNodos(List<String> ips) {
        this.setCodigoComando("0001");
        this.ips = ips;
    }
    public SincronizacionNodos(String ip){
        super();
        this.setCodigoComando("0001");
        setIp(ip);
    }


    @Override
    public void parsear(String comando) {
        String[] tokens = comando.split(Pattern.quote("|"));
        if(tokens.length == 2){
            setCodigoComando(tokens[0]);
            ips = new ArrayList<>(Arrays.asList(tokens[1].split(";")));
        }
    }

    @Override
    public String getComando() {
        return getCodigoComando()+"|"+ipsToString() + System.lineSeparator();
    }
    private String ipsToString(){
        if(ips.isEmpty()) return "";

        StringBuilder str = new StringBuilder();
        str.append(ips.getFirst());
        for (String ip : ips){
            str.append(";").append(ip);
            str.append(ip);
        }
        return str.toString();
    }
}
