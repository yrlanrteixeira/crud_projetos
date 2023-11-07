package main.java.com.crud.application;

import java.io.RandomAccessFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class IndiceInvertido {
    private HashMap<String, ArrayList<Integer>> indiceInvertido;
    private final String dbFileName = "src\\main\\java\\com\\crud\\db\\Projetos.db";

    public IndiceInvertido() {
        indiceInvertido = new HashMap<>();
        criarIndiceInvertido();
    }

    private void criarIndiceInvertido() {
        try {
            RandomAccessFile arquivoProjetos = new RandomAccessFile(dbFileName, "rw");
            int numeroLinha = 0;

            while (arquivoProjetos.getFilePointer() < arquivoProjetos.length()) {
                String nomeProjeto = arquivoProjetos.readUTF();
                adicionarProjetoAoIndice(nomeProjeto, numeroLinha);
                arquivoProjetos.readByte(); // Pula o byte do ID
                // Pula os três bytes reservados
                arquivoProjetos.readByte();
                arquivoProjetos.readByte();
                arquivoProjetos.readByte();
                // Pula o valor long reservado
                arquivoProjetos.readLong();
                numeroLinha++;
            }

            arquivoProjetos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void adicionarProjetoAoIndice(String nomeProjeto, int numeroLinha) {
        String[] palavras = nomeProjeto.split(" ");

        for (String palavra : palavras) {
            palavra = palavra.toLowerCase(); // Converte a palavra para minúsculas
            if (!indiceInvertido.containsKey(palavra)) {
                indiceInvertido.put(palavra, new ArrayList<>());
            }
            indiceInvertido.get(palavra).add(numeroLinha);
        }
    }

    public void imprimirIndiceInvertido() {
        for (Map.Entry<String, ArrayList<Integer>> entrada : indiceInvertido.entrySet()) {
            System.out.println(entrada.getKey() + ": " + entrada.getValue());
        }
    }

    public ArrayList<Integer> buscarResponsavel(String responsavel) {
        String[] palavras = responsavel.split(" ");
        ArrayList<Integer> resultado = new ArrayList<>();

        for (String palavra : palavras) {
            palavra = palavra.toLowerCase(); // Converte a palavra para minúsculas
            if (indiceInvertido.containsKey(palavra)) {
                resultado.addAll(indiceInvertido.get(palavra));
            }
        }

        return resultado;
    }
}
