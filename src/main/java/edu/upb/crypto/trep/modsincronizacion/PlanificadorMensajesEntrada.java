package edu.upb.crypto.trep.modsincronizacion;

import edu.upb.crypto.trep.bl.*;
import edu.upb.crypto.trep.modsincronizacion.server.SocketClient;
import edu.upb.crypto.trep.modsincronizacion.server.event.SocketEvent;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PlanificadorMensajesEntrada extends Thread implements SocketEvent {

    private static final ConcurrentLinkedQueue<Comando> messages = new ConcurrentLinkedQueue<>();

    public PlanificadorMensajesEntrada() {

    }

    @Override
    public void run() {
        while (true) {
            Comando comando;
            synchronized (messages) {
                if (messages.isEmpty()) {
                    try {
                        messages.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                comando = messages.poll();
            }
            System.out.println(comando.getCodigoComando());
            switch (comando.getCodigoComando()) {
                case "0001":
                    proceesarComando1((SincronizacionNodos) comando);
                    break;
                case "0009":
                    proceesarComando9((Comando09) comando);
                    break;
                case "0010":
                    proceesarComando10((Comando010) comando);
                    break;
                case "0011":
                    proceesarComando11((Comando011) comando);
                    break;

            }
        }
    }

    private void proceesarComando1(SincronizacionNodos comando) {
        // Conectarse a todos los clientes
        for (String ip : comando.getIps()) {
            try {
                if (!isMyIP(ip)) {
                    SocketClient client = new SocketClient(new Socket(ip, 1825));
                    client.start();
                    PlanificadorMensajesSalida.addNode(client);
                    System.out.println("PME - Conectado al nodo: " + ip);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void proceesarComando9(Comando09 comando) {
        //validar firma

        //Agregar al planificador de transacciones
        PlanificadorTransaccion.addVoto(comando);

        //Generar el comando confirmacion voto y enviarlo
        Comando010 comando010 = new Comando010(comando.getVoto(), true);
        PlanificadorMensajesSalida.sendCommand(comando.getIp(), comando010);

    }

    private void proceesarComando10(Comando010 comando) {
        PlanificadorPresi.confirmarVoto(comando);
    }

    private void proceesarComando11(Comando011 comando) {
        PlanificadorTransaccion.commitVoto(comando);

    }

    private boolean isMyIP(String ip) {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                if (iface.isLoopback() || !iface.isUp()) {
                    continue;
                }
                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    if (ip.equals(addr.getHostAddress())) {
                        return true;
                    }
                }
            }
        } catch (SocketException e) {
            System.err.println("Error al obtener las interfaces de red: " + e.getMessage());
        }
        return false;
    }


    @Override
    public void onNewNodo(SocketClient client) {
        // no implementar
    }

    @Override
    public void onCloseNodo(SocketClient client) {

    }

    @Override
    public void onMessage(Comando comando) {
        synchronized (messages) {
            messages.add(comando);
            messages.notify();
        }
    }
}
