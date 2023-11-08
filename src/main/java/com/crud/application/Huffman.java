package main.java.com.crud.application;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

class HuffmanNode implements Comparable<HuffmanNode> {
    char data;
    int frequency;
    HuffmanNode left;
    HuffmanNode right;

    public int compareTo(HuffmanNode node) {
        return this.frequency - node.frequency;
    }
}

public class Huffman {

    public static void compress() {
        String inputFile = "src\\main\\java\\com\\crud\\db\\Projetos.db";
        String outputFile = "src\\main\\java\\com\\crud\\db\\ProjetosCompressed.db";

        try {
            FileInputStream fis = new FileInputStream("src\\main\\java\\com\\crud\\db\\Projetos.db");
            FileOutputStream fos = new FileOutputStream(outputFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            // Ler o conteúdo do arquivo de entrada
            byte[] inputBuffer = new byte[fis.available()];
            fis.read(inputBuffer);
            fis.close();

            // Construir a tabela de frequência dos caracteres
            Map<Character, Integer> frequencyMap = new HashMap<>();
            for (byte b : inputBuffer) {
                char c = (char) b;
                frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
            }

            // Construir a árvore de Huffman
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
                HuffmanNode left = priorityQueue.poll();
                HuffmanNode right = priorityQueue.poll();
                HuffmanNode parent = new HuffmanNode();
                parent.frequency = left.frequency + right.frequency;
                parent.left = left;
                parent.right = right;
                priorityQueue.add(parent);
            }

            HuffmanNode root = priorityQueue.peek();

            // Construir a tabela de códigos de Huffman
            Map<Character, String> huffmanCodes = new HashMap<>();
            buildHuffmanCodes(root, "", huffmanCodes);

            // Escrever a tabela de códigos no arquivo de saída
            oos.writeObject(huffmanCodes);

            // Comprimir o arquivo e escrever no arquivo de saída
            BitOutputStream bos = new BitOutputStream(fos);
            for (byte b : inputBuffer) {
                char c = (char) b;
                String code = huffmanCodes.get(c);
                for (char bit : code.toCharArray()) {
                    bos.writeBit(bit == '1');
                }
            }

            bos.close();
            oos.close();

            System.out.println("Compressão concluída.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void buildHuffmanCodes(HuffmanNode root, String code, Map<Character, String> huffmanCodes) {
        if (root == null) {
            return;
        }

        if (root.left == null && root.right == null) {
            huffmanCodes.put(root.data, code);
        }

        buildHuffmanCodes(root.left, code + "0", huffmanCodes);
        buildHuffmanCodes(root.right, code + "1", huffmanCodes);
    }

    public static void decompress() {
        String compressedFile = "src\\main\\java\\com\\crud\\db\\ProjetosCompressed.db";
        String outputFile = "src\\main\\java\\com\\crud\\db\\ProjetosDecompressed.db";

        try {
            FileInputStream fis = new FileInputStream(compressedFile);
            FileOutputStream fos = new FileOutputStream(outputFile);
            ObjectInputStream ois = new ObjectInputStream(fis);

            // Ler a tabela de códigos de Huffman do arquivo comprimido
            Map<Character, String> huffmanCodes = (Map<Character, String>) ois.readObject();

            // Construir a árvore de Huffman a partir da tabela de códigos
            HuffmanNode root = buildHuffmanTree(huffmanCodes);

            // Realizar a descompressão
            BitInputStream bis = new BitInputStream(fis);
            HuffmanNode current = root;
            while (true) {
                int bit = bis.readBit();
                if (bit == -1) {
                    break; // Fim do arquivo
                }

                if (bit == 0) {
                    current = current.left;
                } else {
                    current = current.right;
                }

                if (current.left == null && current.right == null) {
                    fos.write(current.data);
                    current = root;
                }
            }

            bis.close();
            ois.close();
            fos.close();

            System.out.println("Descompressão concluída.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    static HuffmanNode buildHuffmanTree(Map<Character, String> huffmanCodes) {
        HuffmanNode root = new HuffmanNode();
        for (Map.Entry<Character, String> entry : huffmanCodes.entrySet()) {
            char c = entry.getKey();
            String code = entry.getValue();
            HuffmanNode current = root;

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

            current.data = c;
        }
        return root;
    }
}

class BitOutputStream {
    private FileOutputStream fos;
    private int currentByte;
    private int numBitsFilled;

    public BitOutputStream(FileOutputStream fos) {
        this.fos = fos;
        this.currentByte = 0;
        this.numBitsFilled = 0;
    }

    public void writeBit(boolean bit) throws IOException {
        if (bit) {
            currentByte = (currentByte << 1) | 1;
        } else {
            currentByte = currentByte << 1;
        }

        numBitsFilled++;

        if (numBitsFilled == 8) {
            fos.write(currentByte);
            currentByte = 0;
            numBitsFilled = 0;
        }
    }

    public void close() throws IOException {
        while (numBitsFilled != 0) {
            writeBit(false);
        }
        fos.close();
    }
}

class BitInputStream {
    private FileInputStream fis;
    private int currentByte;
    private int numBitsFilled;

    public BitInputStream(FileInputStream fis) {
        this.fis = fis;
        this.currentByte = 0;
        this.numBitsFilled = 0;
    }

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

    public void close() throws IOException {
        fis.close();
    }
}
