/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package edu.upb.crypto.trep;

import edu.upb.crypto.trep.httpserver.ApacheServer;
import edu.upb.crypto.trep.server.Server;


/**
 *
 * @author rlaredo
 */
public class CryptoTrep {

    public static void main(String[] args) throws java.io.IOException {
        new Server().start();
        new ApacheServer().start();
    }
}
