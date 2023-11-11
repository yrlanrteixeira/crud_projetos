package main.java.com.crud.application;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Classe que representa um nó na arvore de Huffman
 */
class HuffmanNode implements Comparable<HuffmanNode> {
    char data;
    int frequency;
    HuffmanNode left;
    HuffmanNode right;

    public int compareTo(HuffmanNode node) {
        return this.frequency - node.frequency;
    }
}

/**
 * Classe de codificação e decodificação de Huffman
 */
public class Huffman {

    /**
     * Função que realiza a compressão do arquivo db
     * 
     * @param versao Versao da compressão
     */
    public static void compress(String versao) {

        // Começa a contar o tempo de execução da compressão
        long tempoInicio = System.currentTimeMillis();

        // Define o caminho do arquivo de saída comprimido
        String outputFile = "src\\main\\java\\com\\crud\\db\\ProjetosCompressao" + versao + ".db";

        try {
            // Abre o arquivo de entrada para leitura
            FileInputStream fis = new FileInputStream("src\\main\\java\\com\\crud\\db\\Projetos.db");

            // Cria os fluxos de saída para o arquivo comprimido
            FileOutputStream fos = new FileOutputStream(outputFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            // Ler o conteúdo do arquivo de entrada para um buffer de bytes
            byte[] inputBuffer = new byte[fis.available()];
            fis.read(inputBuffer);
            fis.close();

            // Construir a tabela de frequência dos caracteres
            Map<Character, Integer> frequencyMap = new HashMap<>();
            for (byte b : inputBuffer) {
                char c = (char) b;
                frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
            }

            // Construir a árvore de Huffman usando uma fila de prioridade
            PriorityQueue<HuffmanNode> priorityQueue = new PriorityQueue<>();
            for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
                HuffmanNode node = new HuffmanNode();
                node.data = entry.getKey();
                node.frequency = entry.getValue();
                node.left = null;
                node.right = null;
                priorityQueue.add(node);
            }

            while (priorityQueue.size() > 1) {
                // Combina os dois nós de menor frequência para formar um novo nó
                HuffmanNode left = priorityQueue.poll();
                HuffmanNode right = priorityQueue.poll();
                HuffmanNode parent = new HuffmanNode();
                parent.frequency = left.frequency + right.frequency;
                parent.left = left;
                parent.right = right;
                priorityQueue.add(parent);
            }

            // Obtém a raiz da árvore de Huffman
            HuffmanNode root = priorityQueue.peek();

            // Construir a tabela de códigos de Huffman
            Map<Character, String> huffmanCodes = new HashMap<>();
            buildHuffmanCodes(root, "", huffmanCodes);

            // Escrever a tabela de códigos no arquivo de saída
            oos.writeObject(huffmanCodes);

            // Comprime o arquivo e escrever no arquivo de saída usando bits
            BitOutputStream bos = new BitOutputStream(fos);
            for (byte b : inputBuffer) {
                char c = (char) b;
                String code = huffmanCodes.get(c);
                for (char bit : code.toCharArray()) {
                    bos.writeBit(bit == '1');
                }
            }

            // Fecha os fluxos de saída
            bos.close();
            oos.close();

            System.out.println("Compressão concluída.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Calcula e imprime o tempo de execução da compressão
        long tempoExecucao = System.currentTimeMillis() - tempoInicio;
        System.out.println(
                "Tempo de execução: " + (float) tempoExecucao / 1000 + " segundos\n");

        /**
         * Calcula o ganho ou perda da compressão
         */
        try {
            // Abre os arquivos original e comprimido para comparar tamanhos
            RandomAccessFile arqOriginal = new RandomAccessFile("src\\main\\java\\com\\crud\\db\\Projetos.db", "r");
            RandomAccessFile arqComprimido = new RandomAccessFile(
                    "src\\main\\java\\com\\crud\\db\\ProjetosCompressao" + versao + ".db", "r");

            // Calcula e imprime o ganho de compressão
            double ganho = arqOriginal.length() * 1.0 / arqComprimido.length();
            System.out.println("Tamanho dos arquivos: \n");
            System.out.print("Original: " + arqOriginal.length() + " bytes | Comprimido: " + arqComprimido.length()
                    + " bytes | ");
            if (ganho == 1)
                System.out.println("Não houve ganho nem perda.");
            else if (ganho > 1)
                System.out.printf("Houve ganho de %.2f%%\n", ((ganho - 1) * 100));
            else
                System.out.printf("Houve uma perda de %.2f%%\n", ganho * 100);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Método recursivo para construir os códigos de Huffman
     * 
     * @param root         Nó raiz
     * @param code
     * @param huffmanCodes
     */
    static void buildHuffmanCodes(HuffmanNode root, String code, Map<Character, String> huffmanCodes) {
        // Se o nó for vazio retorna
        if (root == null) {
            return;
        }

        // Se o nó é uma folha, adiciona o código ao mapa
        if (root.left == null && root.right == null) {
            huffmanCodes.put(root.data, code);
        }

        // Percorre a árvore recursivamente
        buildHuffmanCodes(root.left, code + "0", huffmanCodes);
        buildHuffmanCodes(root.right, code + "1", huffmanCodes);
    }

    /**
     * Função para descomprimir um arquivo
     * 
     * @param versao Versão do arquivo que deseja descomprimir
     */
    public static void decompress(String versao) {
        // Registra o tempo de início da descompressão
        long tempoInicio = System.currentTimeMillis();

        // Define os caminhos dos arquivos comprimido e de saída
        String compressedFile = "src\\main\\java\\com\\crud\\db\\ProjetosCompressao" + versao + ".db";
        String outputFile = "src\\main\\java\\com\\crud\\db\\Projetos.db";

        try {
            // Abre o arquivo comprimido para leitura
            FileInputStream fis = new FileInputStream(compressedFile);

            // Cria os fluxos de saída para o arquivo descomprimido
            FileOutputStream fos = new FileOutputStream(outputFile);
            ObjectInputStream ois = new ObjectInputStream(fis);

            // Ler a tabela de códigos de Huffman do arquivo comprimido
            Map<Character, String> huffmanCodes = (Map<Character, String>) ois.readObject();

            // Construir a árvore de Huffman a partir da tabela de códigos
            HuffmanNode root = buildHuffmanTree(huffmanCodes);

            // Realiza a descompressão
            BitInputStream bis = new BitInputStream(fis);
            HuffmanNode current = root;
            while (true) {
                int bit = bis.readBit();
                if (bit == -1) {
                    break; // Fim do arquivo
                }

                // Navega pela árvore com base nos bits
                if (bit == 0) {
                    current = current.left;
                } else {
                    current = current.right;
                }

                // Se chegar a uma folha, escreve o caractere no arquivo de saída
                if (current.left == null && current.right == null) {
                    fos.write(current.data);
                    current = root;
                }
            }

            // Fecha os fluxos de entrada e saída
            bis.close();
            ois.close();
            fos.close();

            System.out.println("Descompressão concluída.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Calcula e imprime o tempo de execução do código
        long tempoExecucao = System.currentTimeMillis() - tempoInicio;
        System.out.println(
                "Tempo de execução: " + (float) tempoExecucao / 1000 + " segundos\n");
    }

    /**
     * Método para construir a árvore de Huffman a partir da tabela de códigos
     * 
     * @param huffmanCodes Codigos Huffman
     * @return
     */
    static HuffmanNode buildHuffmanTree(Map<Character, String> huffmanCodes) {
        HuffmanNode root = new HuffmanNode();
        for (Map.Entry<Character, String> entry : huffmanCodes.entrySet()) {
            char c = entry.getKey();
            String code = entry.getValue();
            HuffmanNode current = root;

            // Percorrer a árvore para criar os nós necessários
            for (char bit : code.toCharArray()) {
                if (bit == '0') {
                    if (current.left == null) {
                        current.left = new HuffmanNode();
                    }
                    current = current.left;
                } else if (bit == '1') {
                    if (current.right == null) {
                        current.right = new HuffmanNode();
                    }
                    current = current.right;
                }
            }

            // Atribuí o caractere ao nó folha
            current.data = c;
        }
        return root;
    }
}

/**
 * Classe auxiliar para escrever bits em um arquivo de saída
 */
class BitOutputStream {
    private FileOutputStream fos;
    private int currentByte;
    private int numBitsFilled;

    public BitOutputStream(FileOutputStream fos) {
        this.fos = fos;
        this.currentByte = 0;
        this.numBitsFilled = 0;
    }

    /**
     * Método que escreve um bit no arquivo
     * 
     * @param bit
     * @throws IOException
     */
    public void writeBit(boolean bit) throws IOException {
        if (bit) {
            currentByte = (currentByte << 1) | 1;
        } else {
            currentByte = currentByte << 1;
        }

        numBitsFilled++;

        // Se o o byte está cheio, escreve o arquivo
        if (numBitsFilled == 8) {
            fos.write(currentByte);
            currentByte = 0;
            numBitsFilled = 0;
        }
    }

    /**
     * Fecha fluxo de saída
     * 
     * @throws IOException
     */
    public void close() throws IOException {
        while (numBitsFilled != 0) {
            writeBit(false);
        }
        fos.close();
    }
}

/**
 * Classe auxiliar para ler bits de um arquivo de entrada
 */
class BitInputStream {
    private FileInputStream fis;
    private int currentByte;
    private int numBitsFilled;

    public BitInputStream(FileInputStream fis) {
        this.fis = fis;
        this.currentByte = 0;
        this.numBitsFilled = 0;
    }

    /**
     * Função para ler um único bit do arquivo
     * 
     * @return
     * @throws IOException
     */
    public int readBit() throws IOException {
        if (numBitsFilled == 0) {
            currentByte = fis.read();
            if (currentByte == -1) {
                return -1; // Fim do arquivo
            }
            numBitsFilled = 8;
        }

        int bit = (currentByte >> (numBitsFilled - 1)) & 1;
        numBitsFilled--;
        return bit;
    }

    /**
     * Fecha fluxo de entrada
     * 
     * @throws IOException
     */
    public void close() throws IOException {
        fis.close();
    }
}
