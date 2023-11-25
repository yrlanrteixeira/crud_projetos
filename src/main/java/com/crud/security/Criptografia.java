package main.java.com.crud.security;

import java.io.*;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;

/**
 * A classe Criptografia realiza operações de criptografia e descriptografia
 * usando o algoritmo AES.
 */
public class Criptografia {
    // Algoritmo de criptografia utilizado
    private final String ALGORITMO = "AES/CTR/NoPadding";

    // Chave secreta utilizada para criptografia e descriptografia
    private Key chaveAES;

    // Parâmetro de especificação do vetor de inicialização (IV)
    private IvParameterSpec ivps;

    /**
     * Construtor da classe Criptografia.
     * Inicializa a chave AES carregando-a do arquivo ou gerando uma nova, e define
     * o IV.
     */
    public Criptografia() {
        this.chaveAES = ChaveCriptografia.carregarChave();
        this.ivps = new IvParameterSpec(new byte[16]);
    }

    /**
     * Criptografa um texto usando o algoritmo AES.
     *
     * @param texto O texto a ser criptografado.
     * @return Uma representação hexadecimal do texto cifrado.
     */
    public String criptografar(String texto) {
        try {
            // Inicializa a cifra para operação de criptografia
            Cipher cifra = Cipher.getInstance(ALGORITMO);
            cifra.init(Cipher.ENCRYPT_MODE, chaveAES, ivps);

            // Converte o texto cifrado para uma representação hexadecimal
            byte[] textoCifradoBytes = cifra.doFinal(texto.getBytes());
            return Conversoes.converteByteArrayParaStringHexadecimal(textoCifradoBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Descriptografa um texto cifrado usando o algoritmo AES.
     *
     * @param textoCifrado Uma representação hexadecimal do texto cifrado.
     * @return O texto original descriptografado.
     */
    public String descriptografar(String textoCifrado) {
        try {
            // Inicializa a cifra para operação de descriptografia
            Cipher cifra = Cipher.getInstance(ALGORITMO);
            cifra.init(Cipher.DECRYPT_MODE, chaveAES, ivps);

            System.out.println("Texto cifrado: " + textoCifrado);
            // Converte a representação hexadecimal para um array de bytes
            byte[] textoCifradoBytes = Conversoes.converteHexStringParaByteArray(textoCifrado);

            // Descriptografa o array de bytes
            byte[] textoOriginal = cifra.doFinal(textoCifradoBytes);

            // Converte o array de bytes para uma string
            return new String(textoOriginal);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
