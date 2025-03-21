/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package edu.upb.crypto.trep;

import edu.upb.crypto.trep.config.MyProperties;
import edu.upb.crypto.trep.httpserver.ApacheServer;
import edu.upb.crypto.trep.modsincronizacion.PlanificadorMensajesEntrada;
import edu.upb.crypto.trep.modsincronizacion.PlanificadorMensajesSalida;
import edu.upb.crypto.trep.modsincronizacion.PlanificadorPresi;
import edu.upb.crypto.trep.modsincronizacion.PlanificadorTransaccion;
import edu.upb.crypto.trep.modsincronizacion.server.Zeus;
import edu.upb.crypto.trep.modsincronizacion.server.SocketClient;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author rlaredo
 */
public class CryptoTrep {

    public static void main(String[] args) throws IOException {
        System.out.println(":::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
        System.out.println(":::::::::::::::: Iniciando Crypto Trep ::::::::::::::::::");
        System.out.println(":::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
        PlanificadorMensajesSalida ps = new PlanificadorMensajesSalida();
        ps.start();
        PlanificadorMensajesEntrada pe = new PlanificadorMensajesEntrada();
        pe.start();

        PlanificadorPresi pp = new PlanificadorPresi();
        PlanificadorTransaccion trans = new PlanificadorTransaccion();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
        scheduler.scheduleAtFixedRate(trans, 0, 1, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(pp, 0, 1, TimeUnit.SECONDS);


        //if (MyProperties.IS_NODO_PRINCIPAL) {

        //}
        Zeus server = new Zeus();
        server.start();
        server.addListener(ps);// Planificador de salida se suscribe a los eventos del server
        server.addPlanificadorEntrada(pe);
        ApacheServer apacheServer = new ApacheServer();
        apacheServer.start();

        System.out.println(":::::::::::::::: Crypto Trep Iniciando ::::::::::::::::::");
        System.out.println(":::::::::::::::: NODO PRINCIPAL: " + MyProperties.IS_NODO_PRINCIPAL + " :::::::::::::::::::");
        System.out.println(":::::::::::::::: IP NODO PRINCIPAL: " + MyProperties.IP_NODO_PRINCIPAL + " :::::::::");
        if (!MyProperties.IS_NODO_PRINCIPAL) {
            SocketClient socketClient = new SocketClient(new Socket(MyProperties.IP_NODO_PRINCIPAL, 1825));
            socketClient.start();
            ps.onNewNodo(socketClient);
            socketClient.addListerner(pe);

        }

    }
}
