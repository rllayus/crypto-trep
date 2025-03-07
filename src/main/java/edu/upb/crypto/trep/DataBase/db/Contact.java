/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.upb.crypto.trep.DataBase.db;

import lombok.*;

import java.io.Serializable;

/**
 *
 * @author rlaredo
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Contact implements Serializable, Model {
    public static final String ME_CODE = "af3bc20a-766c-4cd4-813d-b1067a01fa9a";


    public static class Column{
        public static String ID= "id";
        public static String CODE ="code";
        public static String NAME ="name";
        public static String IP ="ip";

    }
    @Override
    public void setId(long id) {
        this.id = id;
    }
    private long id;
    private String code;
    private String name;
    private String ip;
    private boolean stateConnect = false;
    
    public String roomCode(){
        return ME_CODE + code;
    }
    

}


