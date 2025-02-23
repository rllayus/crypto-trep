/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.upb.crypto.trep.Socket;

import org.apache.log4j.Logger;

import javax.swing.event.EventListenerList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 * @author lucas
 */
public class Mediator extends Thread {

    private static final Map<String, SocketClient> clients = new HashMap<>();
    private static final EventListenerList listenerList = new EventListenerList();
    Logger logger = Logger.getLogger(Mediator.class);

    @Override
    public void run() {

    }
}
