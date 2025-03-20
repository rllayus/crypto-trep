package edu.upb.crypto.trep;

public class StringUtil {
    public static String padRightSpace(String str, int length) {
        String s = str +"                                                                                                    ";
        return s.substring(0, length);
    }

    public static String padLeftZero(String str, int length) {
        String s = "000000"+str;
        return s.substring(s.length() - length);
    }

    public static boolean isNull(String str) {
        if (str == null) {
            return true;
        }
        return str.trim().isEmpty();
    }
    public static String trim(String valor) {
        if (valor == null)
            return null;

        return valor.trim();
    }
}
