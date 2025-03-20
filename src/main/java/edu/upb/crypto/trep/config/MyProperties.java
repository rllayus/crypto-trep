package edu.upb.crypto.trep.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class MyProperties {
    public static boolean IS_NODO_PRINCIPAL = false;
    public static String IP_NODO_PRINCIPAL;
    public static String SECRET_KEY;
    static {
        Properties prop = new Properties();
        try {
            prop.load(new InputStreamReader(new FileInputStream("etc/trep.properties"), "UTF-8"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        IS_NODO_PRINCIPAL = Boolean.parseBoolean(prop.getProperty("nodo.principal"));
        IP_NODO_PRINCIPAL = prop.getProperty("nodo.principal.ip");
        SECRET_KEY = prop.getProperty("secret-key");
    }
    private MyProperties() {}
}
