package main.java.com.crud.dao;

import java.io.IOException;
import java.io.RandomAccessFile;

public class Indice {
    private RandomAccessFile indexFile;

    public Indice() {
        try {
            indexFile = new RandomAccessFile("src\\utils\\ProjetosBase.db", "rw");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // insere no arquivo de índice
    public void insert(byte id, long pos) {
        try {
            long i = (id - 1) * 9;
            indexFile.seek(i);
            indexFile.writeByte(id);
            indexFile.writeLong(pos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // pesquisa no arquivo de índice a posição de um id
    public long search(int id) {
        try {
            long low = 0, high = indexFile.length() / 9, mid;
            byte idArq;
            // nosso índex sempre estará ordenado, então podemos ir para o meio do arquivo e começar a busca binária
            while (low <= high) {
                mid = (low + high) / 2;
                indexFile.seek(mid * 9);
                idArq = indexFile.readByte();
                if (id < idArq)
                    high = mid - 1;
                else if (id > idArq)
                    low = mid + 1;
                else {
                    return indexFile.readLong();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1; // Retorna -1 se o ID não for encontrado
    }

    // deleta um registro do arquivo de índice
    public void delete(byte id) {
        try {
            long posId;
            while (indexFile.getFilePointer() < indexFile.length()) {
                posId = indexFile.getFilePointer();
                byte idAux = indexFile.readByte();
                if (idAux == id) {
                    indexFile.seek(posId);
                    indexFile.writeByte(-1);
                }
                indexFile.readLong();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // atualiza um registro do arquivo de índice
    public void update(byte id, long pos) {
        try {
            while (indexFile.getFilePointer() < indexFile.length()) {
                byte idAux = indexFile.readByte();
                if (id == idAux) {
                    indexFile.writeLong(pos);
                    break;
                }
                indexFile.readLong();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Fecha o arquivo de índice
    public void close() {
        try {
            indexFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
