package main.java.com.crud.security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

public class Criptografia {
    public static final SecretKey CHAVE_SECRETA = gerarChaveSecreta();

    private static SecretKey gerarChaveSecreta() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(256); // Tamanho da chave em bits

            return keyGenerator.generateKey();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static IvParameterSpec geraIv() {
        byte[] vetorInicializacao = new byte[16];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(vetorInicializacao);
        return new IvParameterSpec(vetorInicializacao);
    }

    public static String criptografar(String entrada, SecretKey chave, IvParameterSpec iv) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CFB8/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, chave, iv);
        byte[] textoCifrado = cipher.doFinal(entrada.getBytes(StandardCharsets.UTF_8));

        return Base64.getEncoder().encodeToString(textoCifrado);
    }

    public static String descriptografar(String textoCifrado, SecretKey chave, IvParameterSpec iv) throws Exception {
        try {
            byte[] textoCifradoBytes = Base64.getDecoder().decode(textoCifrado);
            Cipher cipher = Cipher.getInstance("AES/CFB8/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, chave, iv);
            byte[] textoAberto = cipher.doFinal(textoCifradoBytes);

            return new String(textoAberto, StandardCharsets.UTF_8);
        } catch (IllegalArgumentException e) {
            // Lidar com a exceção, por exemplo, imprimir a string problemática
            System.err.println("Erro: String base64 inválida: " + textoCifrado);
            throw e; // Re-lança a exceção para o chamador
        }
    }

}