package edu.upb.crypto.trep.modsincronizacion;

import edu.upb.crypto.trep.bl.Comando;
import edu.upb.crypto.trep.bl.Comando011;
import edu.upb.crypto.trep.bl.Comando09;
import edu.upb.crypto.trep.bl.Comando010;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class PlanificadorTransaccion implements Runnable {
    private static final Map<String, Comando> messages = new HashMap<>();

    public PlanificadorTransaccion() {

    }

    @Override
    public void run() {
        for (Comando comando : messages.values()) {

            if (comando.getCodigoComando().equals(Comando09.CODIGO_COMANDO)) {
                Comando09 vc = (Comando09) comando;
                log.info("Comando ID: " + 
                        ((Comando09) comando).getVoto().getId());
                if (System.currentTimeMillis() - vc.getVoto().getTimestamp() > 10000) {
                    // ya finalizó su tiempo de espera, se debe generar comando 10 con rechazo
                    log.info("Tiempo de espera finalizado");
                    removeItem(vc.getVoto().getId());
                }
            }
        }
    }

    private void removeItem(String key) {
        synchronized (messages) {
            messages.remove(key);
            messages.notify();
        }
    }

    public static void addVoto(Comando09 comando) {
        synchronized (messages) {
            messages.put(comando.getVoto().getId(), comando);
            messages.notify();
        }
    }

    public static void commitVoto(Comando011 confirmacion) {
        synchronized (messages) {
            Comando09 comando09 = (Comando09) messages.remove(confirmacion.getIdVoto());
            log.info(String.format("Voto :%s Listo para registrar a base de datos", confirmacion.getIdVoto()));
            //Insert en base de datos
        }
    }
}
