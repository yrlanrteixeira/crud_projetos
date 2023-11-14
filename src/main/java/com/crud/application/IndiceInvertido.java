package main.java.com.crud.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndiceInvertido {
    private Map<String, List<Integer>> indiceInvertido;
    private Map<String, Integer> contadorOcorrencias;

    public IndiceInvertido() {
        this.indiceInvertido = new HashMap<>();
        this.contadorOcorrencias = new HashMap<>();
    }

    /**
     * Adiciona um índice ao índice invertido para o responsável especificado.
     *
     * @param responsavel Responsável associado ao índice.
     * @param idProjeto   ID do projeto a ser adicionado ao índice.
     */
    public void adicionarIndice(String responsavel, int idProjeto) {
        // Se o responsável já estiver no índice, apenas adicione o ID ao seu valor
        if (indiceInvertido.containsKey(responsavel)) {
            indiceInvertido.get(responsavel).add(idProjeto);
        } else {
            // Se não, crie uma nova entrada no índice com uma lista contendo o ID do
            // projeto
            List<Integer> listaIds = new ArrayList<>();
            listaIds.add(idProjeto);
            indiceInvertido.put(responsavel, listaIds);
            contadorOcorrencias.put(responsavel, 1);
        }
    }

    /**
     * Realiza uma busca no índice invertido para um responsável especificado.
     *
     * @param responsavel Responsável a ser buscado no índice.
     * @return Lista de IDs de projetos associados ao responsável.
     */
    public ArrayList<Integer> buscarResponsavel(String responsavel) {
        if (indiceInvertido.containsKey(responsavel)) {
            contadorOcorrencias.put(responsavel, contadorOcorrencias.get(responsavel) + 1);
            return (ArrayList<Integer>) indiceInvertido.get(responsavel);
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * Retorna o número de ocorrências para um responsável.
     *
     * @param responsavel Responsável a ser consultado.
     * @return Número de ocorrências do responsável.
     */
    public int obterContadorOcorrencias(String responsavel) {
        return contadorOcorrencias.getOrDefault(responsavel, 0);
    }

    /**
     * Imprime o índice invertido para fins de depuração.
     */
    public void imprimirIndiceInvertido() {
        System.out.println("Índice Invertido:");
        for (Map.Entry<String, List<Integer>> entry : indiceInvertido.entrySet()) {
            System.out.print(entry.getKey() + ": ");
            System.out.println(entry.getValue());
        }
    }
}
