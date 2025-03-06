/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.upb.crypto.trep.db;

import lombok.*;

import java.util.Date;

/**
 *
 * @author rlaredo
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message implements Model{
    public static final String TYPE_LEFT= "L";
    public static final String TYPE_RIGHT= "R";
    @Override
    public void setId(long id) {
        this.id = id;
    }

    public static class Column{
        public static String ID= "id";
        public static String codeMessage ="code_message";
        public static String recipientCode ="recipient_code";
        public static String senderCode ="sender_code";
        public static String createdDate ="created_date";
        public static String message ="message";
        public static String type ="type";
        public static String roomCode ="room_code";
        public static String senderName ="name";

    }
    private long id;
    private String codeMessage;
    private String recipientCode;
    private String senderCode;
    private String senderName;
    private String message;
    private String type;
    private Date date;
    private String roomCode;

}
