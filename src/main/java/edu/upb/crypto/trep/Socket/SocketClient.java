/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.upb.crypto.trep.Socket;

import edu.upb.crypto.trep.Socket.event.SocketEvent;
import lombok.Getter;
import org.apache.log4j.Logger;

import javax.swing.event.EventListenerList;
import java.io.*;
import java.net.Socket;

/**
 * @author lucas
 */
public class SocketClient extends Thread {
    private final EventListenerList listenerList = new EventListenerList();
    private static final String C_INVITACION = "001";
    private static final String C_ACEPTACION = "002";
    private static final String C_CHAT = "003";
    private static final String C_CHATV2 = "003V2";
    private static final String C_EDITAR_MENSAJE = "004";
    private static final String C_CAMBIAR_TEMA = "005";
    private static final String C_BORRAR_HISTORIAL = "006";
    private static final String C_PASAR_CONTACTO = "007";
    private static final String C_BUSQEDA = "008";
    private static final String C_ZUMBIDO = "009";
    private static final String C_RECHAZO_SOLICITUD = "010";
    String fileName = null;
    private Socket socket;
    @Getter
    private String ip;
    private final DataOutputStream dout;
    Logger logger = Logger.getLogger(SocketClient.class);

    public SocketClient(Socket socket) throws IOException {
        this.socket = socket;
        this.ip = socket.getInetAddress().getHostAddress();
        dout = new DataOutputStream(socket.getOutputStream());
    }

    public SocketClient(String ip) throws IOException {
        this.socket = new Socket(ip, 1900);
        this.ip = ip;
        dout = new DataOutputStream(socket.getOutputStream());
    }


    @Override
    public void run() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            String message;
            logger.info(SocketClient.class.getSimpleName() + "Socket running");

            while ((message = br.readLine()) != null) {
                String tipoMensaje = message.substring(0, 3);

                logger.info(SocketClient.class.getSimpleName() + ": Received message from: " + ip);
                logger.info("Message received: " + message);
            }
        } catch (Exception e) {
            logger.error(SocketClient.class.getSimpleName() + ": Not able to read received message");
        }
    }

    public void addSocketEventListener(SocketEvent messageEvent) {
        listenerList.add(SocketEvent.class, messageEvent);
    }
}
