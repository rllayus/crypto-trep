package edu.upb.crypto.trep.httpserver;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import edu.upb.crypto.trep.DataBase.Functions;
import edu.upb.crypto.trep.DataBase.models.Voto;
import edu.upb.crypto.trep.StringUtil;
import edu.upb.crypto.trep.bl.Comando09;
import edu.upb.crypto.trep.modsincronizacion.PlanificadorMensajesSalida;
import edu.upb.crypto.trep.modsincronizacion.PlanificadorPresi;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.UUID;

public class VotarHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String xSignature = exchange.getRequestHeaders().get("X-Signature").get(0);
        try (InputStream is = exchange.getRequestBody();
             OutputStream os = exchange.getResponseBody()) {

            // Read request body
            Scanner scanner = new Scanner(is, StandardCharsets.UTF_8.name());
            String requestBody = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";

// Parse JSON
            JsonObject jsonRequest = new com.google.gson.JsonParser().parse(requestBody).getAsJsonObject();
            String codigoVotante = jsonRequest.get("codigo_votante").getAsString();
            String privateKey = Functions.getLlavePrivada(codigoVotante);
            if (StringUtil.isNull(privateKey)) {

            }
            String hmac = new HmacUtils(HmacAlgorithms.HMAC_SHA_256,
                    privateKey.getBytes(StandardCharsets.UTF_8))
                    .hmacHex(requestBody.getBytes(StandardCharsets.UTF_8));
            JsonObject jsonResponse = new JsonObject();
            if (!xSignature.equals(hmac)) {
                jsonResponse.addProperty("status", "NOK");
                jsonResponse.addProperty("message", "La firma no es correcta");
                String response = jsonResponse.toString();

                exchange.getResponseHeaders().add("Content-Type", "application/json");
                exchange.sendResponseHeaders(400, response.getBytes(StandardCharsets.UTF_8).length);
                os.write(response.getBytes(StandardCharsets.UTF_8));
                return;
            }
            String codigoCandidato = jsonRequest.get("codigo_candidato").getAsString();

            Voto voto = new Voto(UUID.randomUUID().toString(), System.currentTimeMillis(), codigoVotante, codigoCandidato);
            Comando09 comando09 = new Comando09(voto, xSignature);
            PlanificadorPresi.addVoto(comando09);
            PlanificadorMensajesSalida.addVoto(comando09);
            jsonResponse.addProperty("status", "OK");
            jsonResponse.addProperty("message", "Votación correcta");
            String response = jsonResponse.toString();

            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, response.getBytes(StandardCharsets.UTF_8).length);
            os.write(response.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
            exchange.sendResponseHeaders(500, 0); // Send error response
        } finally {
            exchange.close(); // Close the exchange explicitly
        }
    }
}