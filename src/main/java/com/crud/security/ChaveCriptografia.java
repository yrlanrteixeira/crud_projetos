package main.java.com.crud.security;

import java.io.*;
import java.security.*;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.*;
import javax.crypto.spec.*;

/**
 * A classe ChaveCriptografia é responsável por gerenciar a chave de
 * criptografia usada no sistema.
 */
public class ChaveCriptografia {
    private static final String ARQUIVO_CHAVE = "src\\main\\java\\com\\crud\\security\\chave.key";
    private static final String ALGORITMO = "AES";
    private static final String SALT = "s@ltV@lu3";
    private static final int ITERACOES_PBKDF2 = 10000;
    private static final int TAMANHO_CHAVE = 256;

    private static MessageDigest sha;

    static {
        try {
            sha = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carrega a chave de criptografia do arquivo ou gera uma nova se o arquivo não
     * existir.
     *
     * @return A chave de criptografia.
     */
    public static Key carregarChave() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARQUIVO_CHAVE))) {
            return (Key) ois.readObject();
        } catch (FileNotFoundException e) {
            // Se o arquivo não existe, cria uma nova chave, a salva e a retorna
            Key chave = gerarChave();
            salvarChave(chave);
            return chave;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Key gerarChave() {
        try {
            // Adicionando um salt à senha
            String senha = "7m!F!F8!uOC3";
            String senhaComSalt = senha + SALT;

            // Utilizando PBKDF2 para derivar a chave da senha com salt
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            PBEKeySpec spec = new PBEKeySpec(senhaComSalt.toCharArray(), SALT.getBytes(), ITERACOES_PBKDF2,
                    TAMANHO_CHAVE);
            SecretKey key = skf.generateSecret(spec);

            // Convertendo a chave para o formato desejado
            return new SecretKeySpec(key.getEncoded(), ALGORITMO);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void salvarChave(Key chave) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARQUIVO_CHAVE))) {
            oos.writeObject(chave);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
