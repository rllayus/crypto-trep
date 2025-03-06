/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.upb.crypto.trep.server;

import edu.upb.crypto.trep.server.event.SocketEvent;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * @author lucas
 */
public class FunctionsServer extends Thread {
    private static final int port = 1900;
    private ServerSocket server;
    private SocketEvent listener;
    Logger logger = Logger.getLogger(FunctionsServer.class);

    public FunctionsServer(SocketEvent listener) throws IOException {
        this.server = new ServerSocket(port);
        this.listener = listener;
    }

    @Override
    public void run() {
        logger.info("Chat server running");

        while (true) {
            try {
                SocketClient socketClient = new SocketClient(this.server.accept());
                socketClient.addSocketEventListener(listener);
                socketClient.start();
                logger.info("New connection");
            } catch (Exception e) {
                logger.error("not able to get connection");
            }
        }
    }

}
