package edu.upb.crypto.trep.modsincronizacion;

import edu.upb.crypto.trep.bl.Comando;
import edu.upb.crypto.trep.bl.Comando011;
import edu.upb.crypto.trep.bl.Comando09;
import edu.upb.crypto.trep.bl.Comando010;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class PlanificadorPresi implements Runnable {
    private static final Map<String, Comando> messages = new HashMap<>();

    public PlanificadorPresi() {

    }

    @Override
    public void run() {
        synchronized (messages) {
            for (Comando comando : messages.values()) {

                if (comando.getCodigoComando().equals(Comando09.CODIGO_COMANDO)) {
                    Comando09 vc = (Comando09) comando;
                    log.info("Comando ID: " + ((Comando09) comando).getVoto().getId());
                    if (vc.getCantidadConfirmaciones() == PlanificadorMensajesSalida.getCantidadNodos()) {
                        // ya estan todas las confirmaciones, se debe enviar comando 10 con OK
                        log.info("Ya está con las confirmaciones necesarios. Se envia mensaje a todos los nodos");
                        removeItem(vc.getVoto().getId());
                        PlanificadorMensajesSalida.addMessage(new Comando011(vc.getVoto().getId()));
                        // Insert en base de datos
                        continue;
                    }
                    if (System.currentTimeMillis() - vc.getVoto().getTimestamp() > 10000) {
                        // ya finalizó su tiempo de espera, se debe generar comando 10 con rechazo
                        log.info("Tiempo de espera finalizado");
                        removeItem(vc.getVoto().getId());
                    }
                }
            }
        }
    }

    private void removeItem(String key) {
        messages.remove(key);
        messages.notify();
    }

    public static void addVoto(Comando09 comando) {
        synchronized (messages) {
            if (!messages.containsKey(comando.getVoto().getId())) {
                messages.put(comando.getVoto().getId(), comando);
                messages.notify();
            } else {
                log.info("Presi: Voto: " + comando.getVoto().getId());
            }
        }
    }

    public static void confirmarVoto(Comando010 confirmacion) {
        log.info("Cantidad votos: " + messages.size());
        log.info("Buscando voto con ID: " + confirmacion.getIdVoto());
        synchronized (messages) {
            Comando09 comando09 = (Comando09) messages.get(confirmacion.getIdVoto());
            comando09.setCantidadConfirmaciones(comando09.getCantidadConfirmaciones() + 1);
            messages.notify();
        }
    }
}
