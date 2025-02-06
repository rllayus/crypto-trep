package edu.upb.crypto.trep.httpserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author rlaredo
 */
public class EchoGetHandler implements HttpHandler {

    @Override

    public void handle(HttpExchange he) throws IOException {
        // parse request
        Map<String, Object> parameters = new HashMap<>();
        URI requestedUri = he.getRequestURI();
        String query = requestedUri.getRawQuery();
        RootHandler.parseQuery(query, parameters);

        // send response
        String response = "";
        for (String key : parameters.keySet())
            response += key + " = " + parameters.get(key) + "\n";
        he.sendResponseHeaders(200, response.length());
        OutputStream os = he.getResponseBody();
        os.write(response.toString().getBytes());

        os.close();
    }
}