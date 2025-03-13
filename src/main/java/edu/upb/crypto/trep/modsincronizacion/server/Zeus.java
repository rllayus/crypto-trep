/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.upb.crypto.trep.modsincronizacion.server;

import edu.upb.crypto.trep.modsincronizacion.server.event.SocketEvent;

import javax.swing.event.EventListenerList;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * @author rlaredo
 */
public class Zeus extends Thread {
    private static final EventListenerList listenerList = new EventListenerList();
    private final ServerSocket serverSocket;
    private SocketEvent planificadorEntrada;
    private List<Socket> sockets;

    public Zeus() throws IOException {
        this.serverSocket = new ServerSocket(1825);
        this.sockets = new ArrayList<Socket>();
    }


    @Override
    public void run() {
        while (true) {
            try {
                Socket socket = this.serverSocket.accept();
                if(sockets.size() > 5){
                    System.out.println("Conexion cerrada: "+sockets.size());
                    socket.close();
                    socket.shutdownInput();
                    socket.shutdownOutput();
                    return;
                }
                this.sockets.add(socket);
                SocketClient sc = new SocketClient(socket);
                sc.start();
                // que planificador de entrada se suscribe a los eventos del socketClient
                sc.addListerner(planificadorEntrada);
                notificarEvento(sc);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addListener(SocketEvent listener) {
        this.listenerList.add(SocketEvent.class, listener);
    }

    public void addPlanificadorEntrada(SocketEvent listener) {
        this.planificadorEntrada = listener;
    }

    public void notificarEvento(SocketClient socketClient) {
        for (SocketEvent listener : listenerList.getListeners(SocketEvent.class)) {
            listener.onNewNodo(socketClient);
        }
    }
}
