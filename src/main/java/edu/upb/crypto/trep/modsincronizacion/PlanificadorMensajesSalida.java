package edu.upb.crypto.trep.modsincronizacion;

import edu.upb.crypto.trep.DataBase.models.Candidato;
import edu.upb.crypto.trep.bl.Comando;
import edu.upb.crypto.trep.bl.SincronizacionCandidatos;
import edu.upb.crypto.trep.bl.SincronizacionNodos;
import edu.upb.crypto.trep.config.MyProperties;
import edu.upb.crypto.trep.modsincronizacion.server.SocketClient;
import edu.upb.crypto.trep.modsincronizacion.server.event.SocketEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class PlanificadorMensajesSalida extends Thread implements SocketEvent {

    private static final ConcurrentLinkedQueue<Comando> messages = new ConcurrentLinkedQueue<>();
    private static final ConcurrentHashMap<String, SocketClient> nodos = new ConcurrentHashMap<>();
    ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
    private List<Future> list = new ArrayList<>();


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
               list.add(executor.submit(() -> {
                    Comando comando = messages.poll();
                    sendMessage(comando);
                }));
               
               if(list.size()>10){
                   for (Future future : list) {
                       try {
                           future.get();
                       } catch (Exception ex) {
                           ex.printStackTrace();
                       }
                   }
                   System.out.println("Sali: Dejando de dormir");
                   list.clear();
                   messages.notify();
               }
            }
        }
    }

    public static void addMessage(Comando comando) {
        synchronized (messages) {
            messages.add(comando);
            messages.notify();
        }
    }

    private void sendMessage(Comando comando) {
        for (SocketClient nodo : nodos.values()) {
            if( nodo == null || !nodo.isAlive()){
                nodos.remove(nodo.getIp());
                return;
            }
            try {
                nodo.send(comando.getComando());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onNewNodo(SocketClient client) {
        synchronized (nodos) {
            nodos.put(client.getIp(), client);
        }
        System.out.println("Nuevo Nodo " + client.getIp());
        if (MyProperties.IS_NODO_PRINCIPAL) {
            System.out.println("Es nodo pro " + client.getIp());
            // preparar el comando y enviar  a todo
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

    @Override
    public void onCloseNodo(SocketClient client) {

    }

    @Override
    public void onMessage(Comando comando) {
        // no implementar
    }
}
