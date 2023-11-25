package main.java.com.crud.security;

import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;

public class Criptografia {
    private final String ALGORITMO = "AES/CTR/NoPadding";

    private Key chaveAES;
    private IvParameterSpec ivps;

    public Criptografia(String key, String iv) {
        byte[] ivArray = Conversoes.converteAsciiToArray(iv, false);
        ivps = new IvParameterSpec(ivArray);

        chaveAES = gerarChaveAES(key);
    }

    public static Key gerarChaveAES(String key) {
        // Certifique-se de que a chave tenha um comprimento válido (por exemplo, 32
        // caracteres para uma chave de 256 bits)
        while (key.length() < 32) {
            key += key;
        }

        byte[] keyArray = Conversoes.converteHexStringToByteArray(key);
        return new SecretKeySpec(keyArray, "AES");
    }

    public String criptografar(String string) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        Cipher c = Cipher.getInstance(ALGORITMO);
        c.init(Cipher.ENCRYPT_MODE, chaveAES, ivps);

        byte[] textoBytes = string.getBytes(); // Converte a string para bytes
        byte[] textoCriptografado = c.doFinal(textoBytes);
        return Conversoes.converteByteArrayToString(textoCriptografado);
    }

    public String descriptografar(String textoCifrado) throws Exception {
        Cipher c = Cipher.getInstance(ALGORITMO);
        c.init(Cipher.DECRYPT_MODE, chaveAES, ivps);

        byte[] textoCriptografado = Conversoes.converteHexStringToByteArray(textoCifrado);
        byte[] msg = c.doFinal(textoCriptografado);

        return new String(msg); // Converte os bytes de volta para uma string
    }

}
