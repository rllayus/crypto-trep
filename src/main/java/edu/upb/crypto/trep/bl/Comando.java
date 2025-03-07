package edu.upb.crypto.trep.bl;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Comando {
    private String ip;
    private String codigoComando;
    public Comando(String ip) {
        this.ip = ip;
    }
    public Comando() {}

    public abstract void parsear(String comando);
    public abstract String getComando();
}
