package edu.upb.crypto.trep.models;

import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class Bloque {
    private String tableName;
    private List<Map<String, String>> data;

    public Bloque(String tableName, List<Map<String, String>> data) {
        this.tableName = tableName;
        this.data = data;
    }

    public String getTableName() {
        return tableName;
    }

    public List<Map<String, String>> getData() {
        return data;
    }
}