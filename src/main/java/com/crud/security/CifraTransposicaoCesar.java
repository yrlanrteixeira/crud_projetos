package main.java.com.crud.security;

public class CifraTransposicaoCesar {
    private static final int CIFRA_CESAR_SHIFT = 128;

    /**
     * Criptografa uma mensagem aplicando cifra de César seguida por cifra de
     * transposição.
     * 
     * @param mensagem          Mensagem a ser criptografada
     * @param chaveTransposicao Chave para a cifra de transposição
     * @return Mensagem criptografada
     */
    public static String criptografar(String mensagem, int chaveTransposicao) {
        String mensagemCifradaCesar = cifraCesar(mensagem, CIFRA_CESAR_SHIFT);
        return cifraTransposicao(mensagemCifradaCesar, chaveTransposicao);
    }

    /**
     * Descriptografa uma mensagem aplicando decifra de transposição seguida por
     * decifra de César.
     * 
     * @param mensagemCriptografada Mensagem criptografada
     * @param chaveTransposicao     Chave para a decifra de transposição
     * @return Mensagem descriptografada
     */
    public static String descriptografar(String mensagemCriptografada, int chaveTransposicao) {
        String mensagemDecifradaTransposicao = decifraTransposicao(mensagemCriptografada, chaveTransposicao);
        return decifraCesar(mensagemDecifradaTransposicao, CIFRA_CESAR_SHIFT);
    }

    /**
     * Aplica cifra de César a uma mensagem.
     * 
     * @param mensagem Mensagem a ser cifrada
     * @param shift    Valor de deslocamento na cifra de César
     * @return Mensagem cifrada
     */
    private static String cifraCesar(String mensagem, int shift) {
        StringBuilder mensagemCifrada = new StringBuilder();
        for (char caractere : mensagem.toCharArray()) {
            int codigoAscii = (int) caractere + shift;
            mensagemCifrada.append((char) codigoAscii);
        }

        return mensagemCifrada.toString();
    }

    /**
     * Aplica decifra de César a uma mensagem.
     * 
     * @param mensagemCifrada Mensagem cifrada
     * @param shift           Valor de deslocamento na cifra de César
     * @return Mensagem decifrada
     */
    private static String decifraCesar(String mensagemCifrada, int shift) {
        StringBuilder mensagemDecifrada = new StringBuilder();
        for (char caractere : mensagemCifrada.toCharArray()) {
            int codigoAscii = (int) caractere - shift;
            mensagemDecifrada.append((char) codigoAscii);
        }
        System.out.println("Texto Cifrado Cesar: " + mensagemCifrada);
        System.out.println("Texto Decifrado Cesar para texto original: " + mensagemDecifrada.toString());
        return mensagemDecifrada.toString();

    }

    /**
     * Aplica cifra de transposição a uma mensagem.
     * 
     * @param mensagem Mensagem a ser cifrada por transposição
     * @param chave    Chave para a cifra de transposição
     * @return Mensagem cifrada por transposição
     */
    private static String cifraTransposicao(String mensagem, int chave) {
        char[] caracteres = mensagem.toCharArray();
        int comprimento = caracteres.length;

        // Calcula o número de linhas na matriz de transposição
        int linhas = (int) Math.ceil((double) comprimento / chave);

        // Cria a matriz para a cifra de transposição
        char[][] matrizTransposicao = new char[linhas][chave];
        int indice = 0;

        // Preenche a matriz com os caracteres da mensagem
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < chave; j++) {
                if (indice < comprimento) {
                    matrizTransposicao[i][j] = caracteres[indice++];
                } else {
                    matrizTransposicao[i][j] = ' ';
                }
            }
        }

        // Constrói a mensagem cifrada a partir da matriz de transposição
        StringBuilder mensagemCifrada = new StringBuilder();
        for (int j = 0; j < chave; j++) {
            for (int i = 0; i < linhas; i++) {
                mensagemCifrada.append(matrizTransposicao[i][j]);
            }
        }
        return mensagemCifrada.toString();
    }

    /**
     * Aplica decifra de transposição a uma mensagem.
     * 
     * @param mensagemCifradaTransposicao Mensagem cifrada por transposição
     * @param chave                       Chave para a decifra de transposição
     * @return Mensagem decifrada por transposição
     */
    private static String decifraTransposicao(String mensagemCifradaTransposicao, int chave) {
        System.out.println("Texto Cifrado Transp.: " + mensagemCifradaTransposicao);
        char[] caracteres = mensagemCifradaTransposicao.toCharArray();
        int comprimento = caracteres.length;

        // Calcula o número de linhas na matriz de transposição
        int linhas = (int) Math.ceil((double) comprimento / chave);

        // Cria a matriz para a cifra de transposição
        char[][] matrizTransposicao = new char[linhas][chave];
        int indice = 0;

        // Preenche a matriz com os caracteres da mensagem cifrada por transposição
        for (int j = 0; j < chave; j++) {
            for (int i = 0; i < linhas; i++) {
                if (indice < comprimento) {
                    matrizTransposicao[i][j] = caracteres[indice++];
                } else {
                    matrizTransposicao[i][j] = ' '; // ou outro caractere padrão, dependendo da lógica desejada
                }
            }
        }

        // Constrói a mensagem decifrada a partir da matriz de transposição
        StringBuilder mensagemDecifrada = new StringBuilder();
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < chave; j++) {
                mensagemDecifrada.append(matrizTransposicao[i][j]);
            }
        }

        System.out.println("Texto Decifrado Transp. para Cesar: " + mensagemDecifrada.toString());
        return mensagemDecifrada.toString().trim(); // Remove espaços adicionados durante a cifra
    }
}
