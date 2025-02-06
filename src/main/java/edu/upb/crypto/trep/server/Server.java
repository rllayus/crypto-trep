/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.upb.crypto.trep.server;

import edu.upb.crypto.trep.httpserver.ApacheServer;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author rlaredo
 */
public class Server extends Thread {

    private final ServerSocket serverSocket;

    public Server() throws IOException {
        this.serverSocket = new ServerSocket(1825);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket socket = this.serverSocket.accept();
                new SocketClient(socket).start();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
    
    public static void main(String[] args) throws IOException {
        new Server().start();
        new ApacheServer().start();
    }

}
