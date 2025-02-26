package edu.upb.crypto.trep.server;

import edu.upb.crypto.trep.DataBase.Functions;
import edu.upb.crypto.trep.httpserver.ApacheServer;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {
    private final ServerSocket serverSocket;

    public Server() throws IOException {
        this.serverSocket = new ServerSocket(1825);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                SocketClient client = new SocketClient(socket);
                Mediator.addClient(socket.getInetAddress().toString(), client);
                client.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        createTables();
        new Server().start();
        new ApacheServer().start();
    }

    private static void createTables() {
        Functions.createVotanteTable();
        Functions.createCandidatosTable();
        Functions.createInitialBloqueTable();
        System.out.println("Tables created successfully.");
    }
}