package main.java.com.crud.model;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Constructor;

import main.java.com.crud.dao.Indice;
import main.java.com.crud.dao.RegistroDAOImpl;

public class Registro<T extends RegistroDAOImpl> {
    Constructor<T> construtor;

    public Registro(Constructor<T> construtor) {
        this.construtor = construtor;
    }

    public String Criptografa(String nome) {
        String nomeCriptografado = "";
        int cifra = 128;

        for (int i = 0; i < nome.length(); i++) {
            nomeCriptografado += (char) (nome.charAt(i) + cifra);
        }
        return nomeCriptografado;
    }

    public String Descriptografa(String nome) {
        String nomeDescriptografado = "";
        int cifra = 128;

        for (int i = 0; i < nome.length(); i++) {
            nomeDescriptografado += (char) (nome.charAt(i) - cifra);
        }
        return nomeDescriptografado;
    }

    public Registro(String codigoProjeto, String setor, double valorOrcado, double valorNegociado,
            double descontoConcedido,
            String dataAtivacaoLead, String dataInicio, String dataTermino, String responsavel, String status) {
    }

    public void Create(RegistroDAOImpl novoRegistro) {
        byte[] b;

        try {
            RandomAccessFile arq = new RandomAccessFile(
                    "src\\main\\java\\com\\crud\\utils\\" + construtor.getName() + ".db",
                    "rw");
            int id;
            arq.seek(0);
            if (arq.length() == 0) {
                id = 1;
                arq.writeInt(id);
            } else {
                id = arq.readInt() + 1;
                arq.seek(0);
                arq.writeInt(id);
            }
            arq.seek(arq.length());
            b = novoRegistro.toByteArray();
            arq.write(b);
            arq.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public RegistroDAOImpl Read(byte id) {
        byte[] b;
        RegistroDAOImpl registroProcurado = new RegistroDAOImpl();
        int tam;
        Indice index = new Indice();
        try {
            RandomAccessFile arq = new RandomAccessFile(
                    "src\\main\\java\\com\\crud\\utils\\" + construtor.getName() + ".db",
                    "rw");
            long pos = index.lerRegistro(id);
            if (pos == -1) {
                arq.close();
                registroProcurado.id = -1;
                return registroProcurado;
            }
            arq.seek(pos); // vamos para posição que está o registro
            if (arq.readByte() == ' ') { // verifica se a lápide é válida
                tam = arq.readInt(); // lê o tamanho do registro
                b = new byte[tam + 1]; // cria um array de bytes para ler o arquivo
                arq.read(b); // lê o tamanho exato do registro e armazena em b
                registroProcurado.fromByteArray(b); // cria um objeto com as informações armazenadas em b
                if (registroProcurado.id == id) { // encontrou o clube
                    arq.close(); // fecha o arquivo
                    registroProcurado.codigoProjeto = Descriptografa(registroProcurado.codigoProjeto);
                    return registroProcurado; // retorna o clube procurado
                }
            } else { // se a lápide indicar que é um arquivo excluído
                arq.close();
                registroProcurado.id = -1;
                return registroProcurado;
            }
            // se chegar aqui quer dizer que não encontrou, então retornaremos um clube com
            // id = -1
            arq.close();
            registroProcurado.id = -1;
            return registroProcurado;
        } catch (Exception e) {
            e.printStackTrace();
            RegistroDAOImpl lixo = new RegistroDAOImpl();
            lixo.id = -1;
            return lixo; // em caso de erro retorna lixo
        }
    }

    public void Update(RegistroDAOImpl registro) {
        byte[] b;
        RegistroDAOImpl registroProcurado = new RegistroDAOImpl();
        int tam;
        long p; // ponteiro para a posição inicial do registro

        try {
            RandomAccessFile arq = new RandomAccessFile(
                    "src\\main\\java\\com\\crud\\utils\\" + construtor.getName() + ".db",
                    "rw");
            arq.seek(4); // Ignorar o primeiro inteiro (ID)
            while (arq.getFilePointer() < arq.length()) {
                p = arq.getFilePointer();
                tam = arq.readInt(); // lê o tamanho do registro
                b = new byte[tam];
                arq.read(b);
                registroProcurado.fromByteArray(b);
                if (registroProcurado.codigoProjeto.equals(registro.codigoProjeto)) {
                    b = registro.toByteArray();
                    if (tam >= b.length) {
                        arq.seek(p); // volta para a posição do registro
                        arq.writeInt(b.length); // atualiza o tamanho do registro
                        arq.write(b); // escreve o registro atualizado
                    } else {
                        // Implementar lógica para lidar com registros de tamanhos diferentes se
                        // necessário.
                    }
                    arq.close();
                    return;
                }
            }
            arq.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Delete(int id) {
        byte[] b;
        RegistroDAOImpl registroProcurado = new RegistroDAOImpl();
        int tam;
        long p; // ponteiro para a posição inicial do registro

        try {
            RandomAccessFile arq = new RandomAccessFile(
                    "src\\main\\java\\com\\crud\\utils\\" + construtor.getName() + ".db",
                    "rw");
            arq.seek(4); // Ignorar o primeiro inteiro (ID)
            while (arq.getFilePointer() < arq.length()) {
                p = arq.getFilePointer();
                tam = arq.readInt(); // lê o tamanho do registro
                b = new byte[tam];
                arq.read(b);
                registroProcurado.fromByteArray(b);
                if (registroProcurado.codigoProjeto.equals(String.valueOf(id))) {
                    arq.seek(p); // volta para a posição do registro
                    arq.writeInt(0); // marca o registro como excluído (tamanho zero)
                    arq.close();
                    return;
                }
            }
            arq.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
