/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.upb.crypto.trep.db;

import java.util.ArrayList;

/**
 *
 * @author rlaredo
 */
public class ContactCollection<T> implements ContactIterator<T>{
    private ArrayList<T> list = new ArrayList<>();
    private int index = 0;
    
    public void add(T contact){
        list.add(contact);
    }

    @Override
    public boolean hasNext() {
        return index < list.size();
    }

    @Override
    public T getNext() {
        if(!hasNext())
            return null;
        T obj = list.get(index);
        index++;
        return obj;
    }
    
}
