package edu.upb.crypto.trep.server;

import com.google.gson.Gson;
import edu.upb.crypto.trep.DataBase.Functions;
import edu.upb.crypto.trep.server.event.SocketEvent;
import edu.upb.crypto.trep.server.model.SocketMessage;
import org.apache.log4j.Logger;

import javax.swing.event.EventListenerList;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

public class SocketClient extends Thread {
    private static final Gson gson = new Gson();
    private final EventListenerList listenerList = new EventListenerList();
    private final Socket socket;
    private final PrintWriter out;
    private final BufferedReader in;
    private final Logger logger = Logger.getLogger(SocketClient.class);
    private final String clientId;

    public SocketClient(Socket socket) throws IOException {
        this.socket = socket;
        this.clientId = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run() {
        Mediator.addClient(clientId, this);
        try {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                logger.info("Received from " + clientId + ": " + inputLine);
                SocketMessage request = gson.fromJson(inputLine, SocketMessage.class);
                SocketMessage response = processRequest(request);
                send(response);
            }
        } catch (IOException e) {
            logger.error("Error handling client connection: " + clientId, e);
        } finally {
            Mediator.removeClient(clientId);
            try {
                socket.close();
            } catch (IOException e) {
                logger.error("Error closing socket for: " + clientId, e);
            }
        }
    }

    private SocketMessage processRequest(SocketMessage request) {
        SocketMessage response = new SocketMessage();
        response.setAction(request.getAction());

        try {
            switch (request.getAction()) {
                case "insertCandidato":
                    Map<String, String> candidatoData = (Map<String, String>) request.getData();
                    Functions.insertCandidato(
                            candidatoData.get("id"),
                            candidatoData.get("nombre")
                    );
                    response.setSuccess(true);
                    response.setMessage("Candidato inserted successfully");
                    break;

                case "insertVotante":
                    Map<String, String> votanteData = (Map<String, String>) request.getData();
                    String llavePrivada = Functions.insertVotante(votanteData.get("codigo"));
                    response.setSuccess(true);
                    response.setMessage("Votante inserted successfully");
                    response.setData(Map.of("llave_privada", llavePrivada));
                    break;

                case "registerVote":
                    Map<String, String> voteData = (Map<String, String>) request.getData();
                    Functions.insertBloqueData(
                            voteData.get("id"),
                            voteData.get("codigo_volante"),
                            voteData.get("codigo_candidato")
                    );
                    response.setSuccess(true);
                    response.setMessage("Vote registered successfully");
                    break;

                case "getAllVotantes":
                    response.setData(Functions.getAllVotantes());
                    response.setSuccess(true);
                    break;

                case "getAllCandidatos":
                    response.setData(Functions.getAllCandidatos());
                    response.setSuccess(true);
                    break;

                case "getAllBloques":
                    response.setData(Functions.getAllBloques());
                    response.setSuccess(true);
                    break;

                default:
                    response.setSuccess(false);
                    response.setMessage("Unknown action");
                    break;
            }
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Error: " + e.getMessage());
            logger.error("Error processing request from " + clientId, e);
        }

        return response;
    }

    public synchronized void send(SocketMessage message) {
        String jsonResponse = gson.toJson(message);
        out.println(jsonResponse);
        logger.info("Sent to " + clientId + ": " + jsonResponse);
    }

    public void addSocketEventListener(SocketEvent messageEvent) {
        listenerList.add(SocketEvent.class, messageEvent);
    }

    public String getClientId() {
        return clientId;
    }
}