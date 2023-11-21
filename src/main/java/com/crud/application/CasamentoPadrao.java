package main.java.com.crud.application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.text.Normalizer;

public class CasamentoPadrao {

    /**
     * Lê o conteúdo de um arquivo e o retorna como uma única string
     * 
     * @return Conteúdo do arquivo como string
     * @throws IOException Exceção de E/S se ocorrer um erro ao ler o arquivo
     */
    public static String lerArquivo() throws IOException {
        StringBuilder conteudo = new StringBuilder();
        String nomeArquivo = "src\\main\\java\\com\\crud\\db\\Projetos.db";

        try (BufferedReader br = new BufferedReader(new FileReader(nomeArquivo))) {
            String linha;

            // Lê cada linha do arquivo e adiciona ao conteúdo
            while ((linha = br.readLine()) != null) {
                conteudo.append(linha);
            }
        }

        return conteudo.toString();
    }

    /**
     * Função que realiza a busca de padrão usando o algoritmo de Boyer-Moore
     * 
     * @param texto  Texto em que você deseja encontrar o padrão
     * @param padrao Padrão que você deseja encontrar
     * @return
     */
    public static int buscarPadrao(String texto, String padrao) {
        long tempoInicio = System.currentTimeMillis();

        // Tamanho das strings de texto e padrão
        int m = padrao.length();
        int n = texto.length();

        // Inicialização da heuristica de bad character
        Map<Character, Integer> badChar = badCharHeuristic(padrao);

        // Inicialização da posição de inicio da busca no texto
        int s = 0;
        int operacoes = 0; // Variável para contar número de operações

        while (s <= (n - m)) {
            int j = m - 1;

            // Compara os caracteres do padrão com os caracteres no texto
            while (j >= 0 && padrao.charAt(j) == texto.charAt(s + j)) {
                j--;
                operacoes++;
            }

            // Se o padrão é encontrado, retorna a posição no texto
            if (j < 0) {
                long tempoExecucao = System.currentTimeMillis() - tempoInicio;
                System.out.println("\nPADRÃO ENCONTRADO!\n");
                System.out.println("Número de operações: " + operacoes);
                System.out.println(
                        "Tempo de execução: " + (float) tempoExecucao / 1000 + " segundos\n");
                return s; // Padrão encontrado
            } else {
                // Move a posição de início da busca com base na heuristica de bad character
                char charAtual = texto.charAt(s + j);
                String normalizedChar = Normalizer.normalize(String.valueOf(charAtual), Normalizer.Form.NFD)
                        .replaceAll("[^\\p{ASCII}]", "");
                int badCharShift = badChar.getOrDefault(normalizedChar, m);
                s += Math.max(1, j - badCharShift);
            }
        }
        System.out.println("\nPADRÃO NÃO ENCONTRADO!\n");
        System.out.println("Número de operações: " + operacoes);
        long tempoExecucao = System.currentTimeMillis() - tempoInicio;
        System.out.println(
                "Tempo de execução: " + (float) tempoExecucao / 1000 + " segundos\n");
        return -1; // Padrão não encontrado
    }

    /**
     * Função que cria a tabela de heuristica de bad character
     * 
     * @param padrao
     * @return
     */
    static Map<Character, Integer> badCharHeuristic(String padrao) {
        int m = padrao.length();
        Map<Character, Integer> badChar = new HashMap<>();

        // Preenche a tabela com os deslocamentos dos caracteres ruins
        for (int i = 0; i < m - 1; i++) {
            badChar.put(padrao.charAt(i), m - 1 - i);
        }
        return badChar;
    }
}