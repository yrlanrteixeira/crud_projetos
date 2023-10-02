package main.java.com.crud.application;

import java.io.RandomAccessFile;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import main.java.com.crud.dao.Indice;
import main.java.com.crud.dao.RegistroDAO;

import main.java.com.crud.model.Registro;

public class CRUD<T extends RegistroDAO> {
    Constructor<T> construtor;

    public CRUD(Constructor<T> construtor) {
        this.construtor = construtor;
    }

    private final String indiceFileName = "src\\main\\java\\com\\crud\\db\\Registro.db";
    private final String dbFileName = "src\\main\\java\\com\\crud\\db\\Projetos.db";

    /**
     * Função paracriação de um novo registro no DB
     * 
     * @param novoRegistro Registro digitado pelo usuário
     */
    public void Create(Registro novoRegistro) {
        byte[] b;

        try {
            Indice indice = new Indice();
            RandomAccessFile arq = new RandomAccessFile(
                    dbFileName, "rw"); // Abre o arquivo ou cria caso não exista
            byte id = 1;
            arq.seek(0);

            /**
             * Se o arquivo estiver vazio escreve o id 1 no cabeçalho
             * senão lê o ultimo id criado e escreve o id + 1
             */
            if (arq.length() == 0) {
                arq.writeByte(id);
            } else {
                id = arq.readByte();
                id++;
                arq.seek(0);
                arq.writeByte(id);
            }

            arq.seek(arq.length()); // Vai para a última posição do arquivo
            long posAtual = arq.getFilePointer();
            arq.writeByte(' '); // Escreve a lápide
            b = novoRegistro.toByteArray(); // Array de bytes que será o registro
            arq.writeInt(b.length); // escreve o tamanho do arquivo
            arq.writeByte(id);// Escreve o id
            arq.write(b);// escreve o registro
            arq.close(); // fecha o registro
            indice.insereRegistro(id, posAtual);// Adiciona no index
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Função para ler um registro no DB
     * 
     * @param id ID do registro a ser lido
     * @return Resgistro Lido
     */
    public Registro Read(byte id) {
        byte[] b;
        Registro registroProcurado = new Registro();
        int tam;
        Indice indice = new Indice();
        try {
            RandomAccessFile arq = new RandomAccessFile(
                    dbFileName, "rw");
            long pos = indice.lerRegistro(id);
            if (pos == -1) {
                arq.close();
                registroProcurado.idProjeto = -1;
                return registroProcurado;
            }
            arq.seek(pos);
            /**
             * Se a lápide for válida, lê o registro e o retorna.
             * Senão retorna que é um registro inválido
             */
            if (arq.readByte() == ' ') {
                tam = arq.readInt();
                b = new byte[tam + 1];
                arq.read(b);
                registroProcurado.fromByteArray(b);

                if (registroProcurado.idProjeto == id) {
                    arq.close();
                    return registroProcurado;
                }
            } else {
                arq.close();
                registroProcurado.idProjeto = -1;
                return registroProcurado;
            }

            /**
             * Não encontrou o registro, então retorna
             */
            arq.close();
            registroProcurado.idProjeto = -1;
            return registroProcurado;
        } catch (Exception e) {
            e.printStackTrace();
            Registro lixo = new Registro();
            lixo.idProjeto = -1;
            return lixo;
        }
    }

    /**
     * Função para realizar o update de algum registro no DB
     * 
     * @param registro Dados do novo registro
     */
    public void Update(Registro registro) {
        byte[] b; // registro no arquivo
        byte[] b2; // registro atualizado
        Registro registroProcurado = new Registro();
        int tam;
        long p; // Ponteiro para posição inicial do arquivo
        long pLap; // Ponteiro para lápide do arquivo
        Indice index = new Indice();

        try {
            RandomAccessFile arq = new RandomAccessFile(
                    dbFileName, "rw");
            arq.seek(0);
            arq.readByte();
            long pos = index.lerRegistro(registro.idProjeto);
            System.out.println(pos);

            if (pos == -1) {
                arq.close();
                registroProcurado.idProjeto = -1;
            } else { // se encontrar no indice
                arq.seek(pos);
                pLap = arq.getFilePointer(); // armazena posição da lápide
                if (arq.readByte() == ' ') {
                    tam = arq.readInt(); // armazena o tamanho do registro
                    p = arq.getFilePointer(); // armazena posição inicial do registro
                    b = new byte[tam + 1];
                    arq.read(b);
                    registroProcurado.fromByteArray(b);

                    /**
                     * Se achar o registro verifica se é maior ou menor que o registro atual.
                     * se for menor somente sobreescreve o registro, se for maior, marca como
                     * excluído e escreve no final do aquivo
                     */
                    if (registroProcurado.idProjeto == registro.idProjeto) {
                        b2 = registro.toByteArray();
                        if (tam >= b2.length) {
                            arq.seek(p);
                            arq.writeByte(registro.idProjeto);
                            arq.write(b2);
                        } else {
                            arq.seek(pLap);
                            arq.writeByte('*');
                            arq.seek(arq.length());
                            pLap = arq.getFilePointer();
                            arq.writeByte(' ');
                            arq.writeInt(b2.length);
                            arq.writeByte(registro.idProjeto);
                            arq.write(b2);
                            index.atualizaRegistro(registro.idProjeto, pLap);
                        }
                    }
                }
            }

            while (arq.getFilePointer() < arq.length()) { // Enquanto não chega na posição final
                pLap = arq.getFilePointer(); // armazena a posição da lápide
                if (arq.readByte() == ' ') {
                    tam = arq.readInt(); // armazena o tamanho do registro
                    p = arq.getFilePointer();// armazena a posição inicial
                    b = new byte[tam + 1];
                    arq.read(b);
                    registroProcurado.fromByteArray(b);

                    /**
                     * Se achar o registro verifica se é maior ou menor que o registro atual.
                     * se for menor somente sobreescreve o registro, se for maior, marca como
                     * excluído e escreve no final do aquivo
                     */
                    if (registroProcurado.idProjeto == registro.idProjeto) {
                        b2 = registro.toByteArray();
                        if (tam >= b2.length) {
                            arq.seek(p);
                            arq.writeByte(registro.idProjeto);
                            arq.write(b2);
                        } else {
                            arq.seek(pLap);
                            arq.writeByte('*');
                            arq.seek(arq.length());
                            arq.writeByte(' ');
                            arq.writeInt(b2.length);
                            arq.writeByte(registro.idProjeto);
                            arq.write(b2);
                        }
                        break;
                    }
                } else { // se inidicar que é um arquivo excluido
                    tam = arq.readInt(); // lê o tamanho do aquivo
                    b = new byte[tam + 1];
                    arq.read(b);
                }
            }
            arq.close();

            /**
             * Se obteve sucesso ao alterar o arquivc informa na tela
             * senão mostra que hove erro ao atualizar o registro
             */
            if (registroProcurado.idProjeto == registro.idProjeto) {
                System.out.println("REGISTRO ALTERADO");
            } else
                System.out.println("REGISTRO NÃO ENCONTRADO");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("REGISTRO NÃO ENCONTRADO");
        }
    }

    /**
     * Função para excluir um registro
     * 
     * @param id ID do registro a ser excluido
     * @return o registro excluido
     */
    public Registro Delete(byte id) {
        byte[] b;
        Registro registroProcurado = new Registro();
        int tam;
        long p; // Ponteiro de lápide
        Indice index = new Indice();
        try {
            RandomAccessFile arq = new RandomAccessFile(
                    dbFileName, "rw");
            arq.seek(0);
            /**
             * Verifica se o registro existe no arquivo
             */
            if (arq.readByte() < id) {
                arq.close();
                throw new Exception();
            }
            long pos = index.lerRegistro(id);
            System.out.println(pos);
            if (pos == -1) {
                arq.close();
                registroProcurado.idProjeto = -1;
                return registroProcurado;
            }
            arq.seek(pos);
            p = arq.getFilePointer();

            /**
             * Se a lápide for válida, verifica o registro que o usuário deseja deletar,
             * se o registro for válido marca a lápide como excluido e fecha o arquivo.
             * Se a lápide indicar que o arquivo é exluido retorna.
             */
            if (arq.readByte() == ' ') {
                tam = arq.readInt();
                b = new byte[tam + 1];
                arq.read(b);
                registroProcurado.fromByteArray(b);
                System.out.println(registroProcurado.idProjeto);
                if (registroProcurado.idProjeto == id) {
                    arq.seek(p);
                    arq.writeByte('*');
                    arq.close();
                    index.deletaRegistro(id);
                    return registroProcurado;
                }
            } else {
                arq.close();
                registroProcurado.idProjeto = -1;
                return registroProcurado;
            }

            arq.close();
            registroProcurado.idProjeto = -1;
            return registroProcurado;

        } catch (Exception e) {
            e.printStackTrace();
            Registro lixo = new Registro();
            lixo.idProjeto = -1;
            return lixo;
        }
    }

    public List<Registro> listarTodosRegistros() {
        List<Registro> registros = new ArrayList<>();
        byte[] b;
        Registro registroProcurado;
        int tam;
        long p;

        try {
            RandomAccessFile arq = new RandomAccessFile(
                    dbFileName, "rw");
            arq.seek(0);
            byte id = arq.readByte();

            while (arq.getFilePointer() < arq.length()) {
                p = arq.getFilePointer();
                if (arq.readByte() == ' ') {
                    tam = arq.readInt();
                    b = new byte[tam + 1];
                    arq.read(b);
                    registroProcurado = new Registro();
                    registroProcurado.setIdProjeto(id);
                    registroProcurado.fromByteArray(b);

                    if (registroProcurado.idProjeto != -1) {
                        registros.add(registroProcurado);
                    }
                } else {
                    tam = arq.readInt();
                    b = new byte[tam + 1];
                    arq.read(b);
                }
            }

            arq.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return registros;
    }

}
