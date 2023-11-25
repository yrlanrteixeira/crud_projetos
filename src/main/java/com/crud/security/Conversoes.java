package main.java.com.crud.security;

/**
 * A classe Conversoes fornece métodos para converter entre diferentes
 * representações de dados.
 */
public class Conversoes {

    /**
     * Converte uma string hexadecimal para um array de bytes.
     *
     * @param string A string hexadecimal a ser convertida.
     * @return Um array de bytes correspondente à representação hexadecimal.
     */
    public static byte[] converteHexStringParaByteArray(String string) {
        int tamanho = string.length() / 2;
        byte[] array = new byte[tamanho];
        for (int i = 0; i < tamanho; i++) {
            array[i] = (byte) Integer.parseInt(string.substring(i * 2, i * 2 + 2), 16);
        }
        return array;
    }

    /**
     * Converte um array de bytes para uma string hexadecimal.
     *
     * @param array O array de bytes a ser convertido.
     * @return Uma string hexadecimal correspondente ao array de bytes.
     */
    public static String converteByteArrayParaStringHexadecimal(byte[] array) {
        StringBuilder sb = new StringBuilder();
        for (byte b : array) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     * Converte uma string ASCII para um array de bytes, com opção de adicionar
     * padding.
     *
     * @param s       A string ASCII a ser convertida.
     * @param padding Indica se deve ser adicionado padding para atender a um
     *                múltiplo de 16 bytes.
     * @return Um array de bytes correspondente à string ASCII, opcionalmente com
     *         padding.
     */
    public static byte[] converteAsciiParaArray(String s, boolean padding) {
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
