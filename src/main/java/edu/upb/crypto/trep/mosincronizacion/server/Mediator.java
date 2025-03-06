package edu.upb.crypto.trep.server;

import edu.upb.crypto.trep.server.model.SocketMessage;
import org.apache.log4j.Logger;
import java.util.HashMap;
import java.util.Map;

public class Mediator {
    private static final Map<String, SocketClient> clients = new HashMap<>();
    private static final Logger logger = Logger.getLogger(Mediator.class);

    public static void addClient(String clientId, SocketClient client) {
        synchronized(clients) {
            clients.put(clientId, client);
        }
        logger.info("Client connected: " + clientId);
    }

    public static void removeClient(String clientId) {
        synchronized(clients) {
            clients.remove(clientId);
        }
        logger.info("Client disconnected: " + clientId);
    }

    public static void broadcast(SocketMessage message) {
        synchronized(clients) {
            clients.values().forEach(client -> {
                try {
                    client.send(message);
                } catch (Exception e) {
                    logger.error("Error broadcasting to " + client.getClientId(), e);
                }
            });
        }
    }

    public static void sendToClient(String clientId, SocketMessage message) {
        synchronized(clients) {
            SocketClient client = clients.get(clientId);
            if (client != null) {
                try {
                    client.send(message);
                } catch (Exception e) {
                    logger.error("Error sending to " + clientId, e);
                }
            }
        }
    }
}