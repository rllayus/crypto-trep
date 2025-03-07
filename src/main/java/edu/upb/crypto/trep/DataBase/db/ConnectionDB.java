/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.upb.crypto.trep.DataBase.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author rlaredo
 */
public class ConnectionDB {
    private final String url;
    
    //Instancia a sí misma
    public static final ConnectionDB instance = new ConnectionDB();
    
    //Constructor privado
    private ConnectionDB(){
        this.url = "jdbc:sqlite:chat_upb.sqlite";
    }
    
    public Connection getConnection(){
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:chat_upb.sqlite");
            if (conn != null) {
                System.out.println("Conexión exitosa.");
            } else {
                System.out.println("Conexión fallida");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }catch(ClassNotFoundException e){
        
        }

        return conn;
    }
    
}
