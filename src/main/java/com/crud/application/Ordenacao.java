package main.java.com.crud.application;

import java.io.RandomAccessFile;
import java.util.ArrayList;

import main.java.com.crud.dao.Indice;
import main.java.com.crud.model.Registro;

public class Ordenacao {
    static String numArq;
    final String dbFileName = "src\\main\\java\\com\\crud\\db\\Projetos.db";
    final String indiceFileName = "src\\main\\java\\com\\crud\\db\\Registro.db";

    // com base no mod ,de 3 podemos receber até 4 valores, e cada um deles
    // significa um arquivo
    public RandomAccessFile qualArquivo(int i, RandomAccessFile arq1, RandomAccessFile arq2, RandomAccessFile arq3,
            RandomAccessFile arq4) {
        switch (i % 3) {
            case 0:
                return arq1;
            case 1:
                return arq2;
            case 2:
                return arq3;
            default:
                return arq4;
        }
    }

    public void qualArquivoString(int i) {
        switch (i % 3) {
            case 0:
                numArq = "arqTemp1.db";
                break;
            case 1:
                numArq = "arqTemp2.db";
                break;
            case 2:
                numArq = "arqTemp3.db";
                break;
            default:
                numArq = "arqTemp4.db";
                break;
        }
    }

    public void ordenaArquivo() {
        byte maxId; // maximo id do arquivo
        int cont = 0;
        int tam = 0;
        byte[] b;
        Registro r = new Registro();
        Boolean aux = true;

        ArrayList<Registro> list = new ArrayList<>();

        try {
            RandomAccessFile arq = new RandomAccessFile(dbFileName, "rw"); // Abre arquivo de dados

            RandomAccessFile arq1 = new RandomAccessFile("arqTemp1.db", "rw"); // Abre arquivo temporário 1
            arq1.setLength(0); // reinicia arquivo 1

            RandomAccessFile arq2 = new RandomAccessFile("arqTemp2.db", "rw"); // Abre arquivo temporário 2
            arq2.setLength(0); // reinicia arquivo 2

            RandomAccessFile arq3 = new RandomAccessFile("arqTemp3.db", "rw"); // Abre arquivo temporário 3
            arq3.setLength(0); // reinicia arquivo 3

            RandomAccessFile arq4 = new RandomAccessFile("arqTemp4.db", "rw"); // Abre arquivo temporário 4
            arq4.setLength(0); // reinicia arquivo 4

            // 2 caminhos e 10 registros por vez

            // distribuição
            arq.seek(0);
            maxId = arq.readByte();
            while (arq.getFilePointer() < arq.length()) {
                Registro rAux = new Registro();
                byte lap = arq.readByte(); // Lápide

                if (lap == ' ') {
                    tam = arq.readInt();
                    b = new byte[tam + 1]; // cria um array de bytes para ler o arquivo
                    arq.read(b); // lê o tamanho exato do registro e armazena em b
                    rAux.fromByteArray(b);
                    rAux.tam = tam;
                    list.add(rAux);
                    cont++;
                    if (cont >= 10 || arq.getFilePointer() >= arq.length()) {
                        cont = 0;
                        list.sort(
                                (o1, o2) -> o1.idProjeto == o2.idProjeto ? 0 : (o1.idProjeto > o2.idProjeto ? 1 : -1));
                        if (aux) {
                            for (int j = 0; j < list.size(); j++) {
                                r = list.get(j);
                                arq1.writeInt(r.tam);
                                arq1.writeByte(r.idProjeto);
                                arq1.write(r.toByteArray());
                            }
                        } else {
                            for (int j = 0; j < list.size(); j++) {
                                r = list.get(j);
                                arq2.writeInt(r.tam);
                                arq2.writeByte(r.idProjeto);
                                arq2.write(r.toByteArray());
                            }
                        }
                        aux = !aux;
                        list.clear();
                    }
                } else {
                    tam = arq.readInt();
                    b = new byte[tam + 1];
                    arq.read(b);
                }
            }
            list.clear();
            cont = 1;
            int numDoArq = 0;
            arq1.seek(0);
            arq2.seek(0);

            while (true) {
                int i = 0;
                RandomAccessFile arqAux = qualArquivo(numDoArq, arq1, arq2, arq3, arq4);
                while (arqAux.getFilePointer() < arqAux.length() && i < 10 * cont) {
                    Registro rAux = new Registro();
                    tam = arqAux.readInt();
                    b = new byte[tam + 1];
                    arqAux.read(b);
                    rAux.fromByteArray(b);
                    rAux.tam = tam;
                    list.add(rAux);
                    i++;
                }
                i = 0;

                RandomAccessFile arqAux2 = qualArquivo(numDoArq + 1, arq1, arq2, arq3, arq4);
                while (arqAux2.getFilePointer() < arqAux2.length() && i < 10 * cont) {
                    Registro rAux = new Registro();
                    tam = arqAux2.readInt();
                    b = new byte[tam + 1]; // cria um array de bytes para ler o arquivo
                    arqAux2.read(b); // lê o tamanho exato do registro e armazena em b
                    rAux.fromByteArray(b);
                    rAux.tam = tam;
                    list.add(rAux);
                    i++;
                }

                // ordena lista
                list.sort((o1, o2) -> o1.idProjeto == o2.idProjeto ? 0 : (o1.idProjeto > o2.idProjeto ? 1 : -1));

                // escreve no arquivo de saída
                RandomAccessFile arqAux3 = qualArquivo(numDoArq + 2, arq1, arq2, arq3, arq4);
                for (i = 0; i < 10 * cont * 2 && i < list.size(); i++) {
                    r = list.get(i);
                    arqAux3.writeInt(r.tam);
                    arqAux3.writeByte(r.idProjeto);
                    arqAux3.write(r.toByteArray());
                }

                // coube tudo no arquivo 3, então acabou
                if (arqAux.getFilePointer() >= arqAux.length() && arqAux2.getFilePointer() >= arqAux2.length()) {
                    qualArquivoString(numDoArq + 2);
                    break;
                }

                // se não couber ele continua e vai pegar mais dos arquivos de entrada
                list.clear();
                while (arqAux.getFilePointer() < arqAux.length() && i < 10 * cont) {
                    Registro rAux = new Registro();
                    tam = arqAux.readInt();
                    b = new byte[tam + 1]; // cria um array de bytes para ler o arquivo
                    arqAux.read(b); // lê o tamanho exato do registro e armazena em b
                    rAux.fromByteArray(b);
                    rAux.tam = tam;
                    list.add(rAux);
                    i++;
                }
                i = 0;

                while (arqAux2.getFilePointer() < arqAux2.length() && i < 10) {
                    Registro rAux = new Registro();
                    tam = arqAux2.readInt();
                    b = new byte[tam + 1]; // cria um array de bytes para ler o arquivo
                    arqAux2.read(b); // lê o tamanho exato do registro e armazena em b
                    rAux.fromByteArray(b);
                    rAux.tam = tam;
                    list.add(rAux);
                    i++;
                }

                // ordena lista
                list.sort((o1, o2) -> o1.idProjeto == o2.idProjeto ? 0 : (o1.idProjeto > o2.idProjeto ? 1 : -1));

                // escreve no arquivo 4 o restante
                RandomAccessFile arqAux4 = qualArquivo(numDoArq + 3, arq1, arq2, arq3, arq4);
                for (i = 0; i < 10 * cont * 2 && i < list.size(); i++) {
                    r = list.get(i);
                    arqAux4.writeInt(r.tam);
                    arqAux4.writeByte(r.idProjeto);
                    arqAux4.write(r.toByteArray());
                }

                // coube tudo no arquivo 4, então acabou
                if (arqAux.getFilePointer() >= arqAux.length() && arqAux2.getFilePointer() >= arqAux2.length()) {
                    qualArquivoString(numDoArq + 3);
                    break;
                }
                // se não couber tudo ele vai pra mais uma roda e agora são trocados os arquivos
                // de leitura e escrita
                numDoArq += 2;
            }

            // fecha todos os arquivos
            arq.close();
            arq1.close();
            arq2.close();
            arq3.close();
            arq4.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reset() {
        int ultimoId = 0;
        byte[] b;
        Indice index = new Indice();
        System.out.println("ARQ - " + Ordenacao.numArq);

        try {
            RandomAccessFile arq = new RandomAccessFile(dbFileName, "rw");
            arq.setLength(0);
            arq.seek(0);
            RandomAccessFile arqAux = new RandomAccessFile(Ordenacao.numArq, "rw");
            RandomAccessFile arqIndex = new RandomAccessFile(indiceFileName, "rw");

            arqIndex.setLength(0);
            arqIndex.seek(0);
            arqAux.seek(0);
            arq.writeByte(0);

            while (arqAux.getFilePointer() < arqAux.length()) {
                Registro r = new Registro();
                r.tam = arqAux.readInt();
                b = new byte[r.tam + 1];
                arqAux.read(b);
                r.fromByteArray(b);
                ultimoId = r.idProjeto;

                index.insereRegistro(r.idProjeto, arq.getFilePointer());

                arq.writeByte(' ');
                arq.writeInt(r.tam);
                arq.writeByte(r.idProjeto);
                arq.write(r.toByteArray());
            }
            arq.seek(0);
            arq.writeByte(ultimoId);

            // fecha todos os arquivos
            arq.close();
            arqAux.close();
            arqIndex.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}