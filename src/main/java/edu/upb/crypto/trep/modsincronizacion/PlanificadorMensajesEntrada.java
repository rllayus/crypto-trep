package edu.upb.crypto.trep.modsincronizacion;

import edu.upb.crypto.trep.bl.Comando;
import edu.upb.crypto.trep.bl.SincronizacionNodos;
import edu.upb.crypto.trep.modsincronizacion.server.SocketClient;
import edu.upb.crypto.trep.modsincronizacion.server.event.SocketEvent;

import java.net.InetAddress;
import java.net.NetworkInterface;
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
            System.out.println(comando.getComando());
            switch (comando.getCodigoComando()) {
                case "0001":
                    proceesarComando1((SincronizacionNodos) comando);
                    break;

            }
        }
    }

    private void proceesarComando1(SincronizacionNodos comando) {
        // Conectarse a todos los clientes
        for (String ip : comando.getIps()) {
            try {
                if (!isMyIP(ip)) {

                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }


    private boolean isMyIP(String ip){
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
                    if(ip.equals(addr.getHostAddress())){
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
