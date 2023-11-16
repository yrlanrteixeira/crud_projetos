package main.java.com.crud.application;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.java.com.crud.model.Registro;

/**
 * Classe responsável por manter um índice invertido associado a responsáveis e
 * IDs de projetos.
 */
public class IndiceInvertido {
    // Mapa que associa cada responsável a uma lista de IDs de projetos
    private Map<String, List<Integer>> indiceInvertido;

    // Contador de ocorrências para cada responsável
    private Map<String, Integer> contadorOcorrencias;

    /**
     * Construtor padrão da classe IndiceInvertido.
     * Inicializa as estruturas de dados necessárias.
     */
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
        // Converte o responsável para minúsculas para garantir consistência
        responsavel = responsavel.toLowerCase();

        // Adiciona o ID do projeto à lista associada ao responsável no índice invertido
        List<Integer> listaIds = indiceInvertido.computeIfAbsent(responsavel, key -> {
            contadorOcorrencias.put(key, 1); // Inicia o contador em 1 ao adicionar o primeiro índice
            return new ArrayList<>();
        });

        listaIds.add(idProjeto);

        // Incrementa o contador de ocorrências para o responsável apenas se for a
        // primeira vez que o responsável é adicionado
        if (listaIds.size() == 1) {
            contadorOcorrencias.put(responsavel, contadorOcorrencias.getOrDefault(responsavel, 0) + 1);
        }
    }

    /**
     * Realiza uma busca no índice invertido para um responsável especificado.
     *
     * @param responsavel Responsável a ser buscado no índice.
     * @return Lista de IDs de projetos associados ao responsável.
     */
    public ArrayList<Integer> buscarResponsavel(String responsavel) {
        String[] palavras = responsavel.split(" ");
        ArrayList<Integer> resultado = new ArrayList<>();

        for (String palavra : palavras) {
            palavra = palavra.toLowerCase();
            if (indiceInvertido.containsKey(palavra)) {
                resultado.addAll(indiceInvertido.get(palavra));
            }
        }

        // Adicionar resultados da busca nos valores existentes no arquivo
        resultado.addAll(buscarNosValoresExistentes(responsavel));

        return resultado;
    }

    /**
     * Realiza uma busca nos valores existentes no arquivo de dados por um
     * responsável especificado.
     *
     * @param responsavel Responsável a ser buscado nos valores existentes.
     * @return Lista de IDs de projetos associados ao responsável nos valores
     *         existentes.
     */
    public ArrayList<Integer> buscarNosValoresExistentes(String responsavel) {
        ArrayList<Integer> resultado = new ArrayList<>();

        // Realizar a busca nos valores existentes no arquivo de dados
        try {
            Constructor<Registro> registroConstructor = Registro.class.getConstructor();
            CRUD<Registro> arquivoDeRegistros = new CRUD<>(registroConstructor);
            List<Registro> registros = arquivoDeRegistros.listarTodosRegistros();

            for (Registro registro : registros) {
                // Verifique se o responsável está presente no registro
                if (registro.getResponsavel().equalsIgnoreCase(responsavel)) {
                    resultado.add((int) registro.getIdProjeto());
                } else {
                    // Verifique se alguma data contém o responsável
                    if (registro.getDataAtivacao().toString().equalsIgnoreCase(responsavel) ||
                            registro.getDataInicio().toString().equalsIgnoreCase(responsavel) ||
                            registro.getDataTermino().toString().equalsIgnoreCase(responsavel)) {
                        resultado.add((int) registro.getIdProjeto());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultado;
    }

    /**
     * Retorna o número de ocorrências para um responsável.
     *
     * @param responsavel Responsável a ser consultado.
     * @return Número de ocorrências do responsável.
     */
    public int obterContadorOcorrencias(String responsavel) {
        // Converte o responsável para minúsculas para garantir consistência
        responsavel = responsavel.toLowerCase();

        // Obtém o contador de ocorrências do responsável
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
