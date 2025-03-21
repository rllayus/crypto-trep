package edu.upb.crypto.trep.modsincronizacion;

import edu.upb.crypto.trep.DataBase.models.Candidato;
import edu.upb.crypto.trep.bl.Comando;
import edu.upb.crypto.trep.bl.Comando09;
import edu.upb.crypto.trep.bl.SincronizacionCandidatos;
import edu.upb.crypto.trep.bl.SincronizacionNodos;
import edu.upb.crypto.trep.config.MyProperties;
import edu.upb.crypto.trep.modsincronizacion.server.SocketClient;
import edu.upb.crypto.trep.modsincronizacion.server.event.SocketEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
public class PlanificadorMensajesSalida extends Thread implements SocketEvent {

    private static final Queue<Comando> messages = new ConcurrentLinkedQueue<>();
    private static final Map<String, SocketClient> nodos = new HashMap<>();

    public PlanificadorMensajesSalida() {

    }

    @Override
    public void run() {
        while (true) {
            synchronized (messages) {
                if (messages.isEmpty()) {
                    try {
                        messages.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                if (!messages.isEmpty()) {
                    sendMessage(messages.poll());
                }
            }
        }
    }

    /**
     * Método para enviar un voto a los demás nodos
     *
     * @param comando
     */
    public static void addVoto(Comando09 comando) {
        synchronized (messages) {
            messages.add(comando);
            messages.notify();
        }
    }


    /**
     * Método para enviar el mensaje a los demás nodos
     *
     * @param comando
     */
    public static void addMessage(Comando comando) {
        synchronized (messages) {
            messages.add(comando);
            messages.notify();
        }
    }

    public static void sendCommand(String ip, Comando comando) {
        SocketClient client = nodos.get(ip);
        if (client != null) {
            client.send(comando);
        }
    }

    private void sendMessage(Comando comando) {
        for (SocketClient nodo : nodos.values()) {
            if (!nodo.isConnected()) {
                log.info("Eliminando nodo porque no esta conectado: {}", nodo.getIp());
                nodos.remove(nodo.getIp());
                return;
            }

            try {
                nodo.send(comando);
                log.info("Comando [ {} ] Enviado a IP:{}", comando.getComando(), nodo.getIp());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static void addNode(SocketClient nodo) {
        synchronized (nodos) {
            nodos.put(nodo.getIp(), nodo);
        }
    }

    @Override
    public void onNewNodo(SocketClient client) {
        synchronized (nodos) {
            if (nodos.containsKey(client.getIp())) {
                log.info("Ya existía el nodo, por lo que se reemplaza");
                nodos.replace(client.getIp(), client);
            } else {
                nodos.put(client.getIp(), client);
            }
        }

        log.info("Nuevo nodo agregado:" + client.getIp());
        if (MyProperties.IS_NODO_PRINCIPAL) {
            Comando comando = new SincronizacionNodos(new ArrayList<>(nodos.keySet()));
            try {
                client.send(comando.getComando());
            } catch (Exception e) {
                e.printStackTrace();
            }
            // enviar candidatos
            List<Candidato> candidatoes = new ArrayList<>();
            candidatoes.add(new Candidato("1", "Alejandra"));
            candidatoes.add(new Candidato("2", "Casita"));
            candidatoes.add(new Candidato("3", "Lucas"));
            comando = new SincronizacionCandidatos(candidatoes);
            try {
                client.send(comando.getComando());
            } catch (Exception e) {
                e.printStackTrace();
            }
            // enviar votantes

            // enviar bloques
        }
    }

    public static int getCantidadNodos() {
        log.info("Cantidad de nodos: {}", nodos.size());
        return nodos.size();
    }

    public static void removeCliente(String ip) {
        synchronized (nodos) {
            nodos.remove(ip);
            log.info("Eliminando nodo: {}", ip);
        }
    }

    @Override
    public void onCloseNodo(SocketClient client) {

    }

    @Override
    public void onMessage(Comando comando) {
        // no implementar
    }
}
