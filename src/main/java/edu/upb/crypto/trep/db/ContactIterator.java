/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package edu.upb.crypto.trep.db;

/**
 *
 * @author rlaredo
 */
public interface ContactIterator<T> {
    boolean hasNext();
    T getNext();
}
