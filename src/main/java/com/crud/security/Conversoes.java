package main.java.com.crud.security;

public class Conversoes {

    public static byte[] converteHexStringToByteArray(String string) {
        int tamanho = string.length() / 2;
        byte[] array = new byte[tamanho];
        for (int i = 0; i < tamanho; i++) {
            array[i] = (byte) Integer.parseInt(string.substring(i * 2, i * 2 + 2), 16);
        }
        return array;
    }

    public static String converteByteArrayToString(byte[] array) {
        StringBuilder sb = new StringBuilder();
        for (byte b : array) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public static byte[] converteAsciiToArray(String s, boolean padding) {
        int tamanho = 0;
        if (padding) {
            tamanho = s.length() % 16;
            tamanho = (tamanho == 0) ? 16 : 0;
        }
        byte[] array = new byte[s.length() + tamanho];
        for (int i = 0; i < s.length(); i++) {
            array[i] = (byte) s.charAt(i);
        }
        byte pad = (byte) tamanho;
        for (int i = s.length(); i < s.length() + tamanho; i++) {
            array[i] = pad;
        }
        return array;
    }
}
