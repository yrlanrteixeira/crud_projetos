package main.java.com.crud.dao;

// Classe RegistroDAOImpl que implementa a interface RegistroDAO

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import main.java.com.crud.model.Registro;

public class RegistroDAOImpl implements RegistroDAO {

    // Atributo que representa o caminho do arquivo CSV
    private String path = "src/main/java/com/crud/utils/ProjetosBase.db";

    // Construtor da classe que cria o arquivo se ele não existir
    public RegistroDAOImpl(String path) {
        this.path = path;
        try {
            File file = new File(path);
            if (!file.exists()) { // Se o arquivo não existir, cria um novo com o cabeçalho inicializado com os
                                  // nomes dos campos
                PrintWriter pw = new PrintWriter(file);
                pw.println(
                        "index, Código Projeto, Setor, Valor Orçado, Valor Negociado, Desconto Concedido, Data Ativação Lead, Data Início,Data Termino,Responsável,Status");
                pw.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método auxiliar para converter um objeto Registro em uma string
    private String registroToString(Registro r) {
        StringBuilder sb = new StringBuilder();
        sb.append(r.getCodigo()).append(",");
        sb.append(r.getSetor()).append(",");
        sb.append(r.getValorOrcado()).append(",");
        sb.append(r.getValorNegociado()).append(",");
        sb.append(r.getDescontoConcedido()).append(",");
        sb.append(r.getDataAtivacao()).append(",");
        sb.append(r.getDataInicio()).append(",");
        sb.append(r.getDataTermino()).append(",");
        sb.append(r.getResponsavel()).append(",");
        sb.append(r.getStatus());
        return sb.toString(); // Retorna a string
    }

    // Método auxiliar para converter uma string em um objeto Registro
    private Registro stringToRegistro(String s) {
        String[] fields = s.split(","); // Divide a string em campos separados por vírgula
        String codigo = fields[0]; // Obtém o primeiro campo como código do projeto
        String setor = fields[1]; // Obtém o segundo campo como setor do projeto
        double valorOrcado = Double.parseDouble(fields[2]); // Converte o terceiro campo em real como valor orçado do
                                                            // projeto
        double valorNegociado = Double.parseDouble(fields[3]); // Converte o quarto campo em real como valor negociado
                                                               // do projeto
        double descontoConcedido = Double.parseDouble(fields[4]); // Converte o quinto campo em real como desconto
                                                                  // concedido do projeto
        LocalDate dataAtivacao = LocalDate.parse(fields[5]); // Converte o sexto campo em data como data de ativação do
                                                             // lead
        LocalDate dataInicio = LocalDate.parse(fields[6]); // Converte o sétimo campo em data como data de início do
                                                           // projeto
        LocalDate dataTermino = LocalDate.parse(fields[7]); // Converte o oitavo campo em data como data de término do
                                                            // projeto
        String responsavel = fields[8]; // Obtém o nono campo como responsável pelo projeto
        String status = fields[9]; // Obtém o décimo campo como status do projeto
        return new Registro(codigo, setor, valorOrcado, valorNegociado, descontoConcedido, dataAtivacao, dataInicio,
                dataTermino, responsavel, status); // Retorna o objeto Registro
    }

    // Método auxiliar para ler uma lista de strings de uma posição específica do
    // arquivo
    private List<String> readStrings(long pos) {
        try {
            RandomAccessFile raf = new RandomAccessFile(path, "r");
            raf.seek(pos); // Posiciona o ponteiro na posição desejada
            int size = raf.readInt(); // Lê o tamanho da lista de strings
            List<String> strings = new ArrayList<>(); // Cria uma lista vazia para armazenar as strings
            for (int i = 0; i < size; i++) { // Para cada string na lista
                int length = raf.readInt(); // Lê o tamanho da string
                byte[] bytes = new byte[length]; // Cria um vetor de bytes com o tamanho lido
                raf.read(bytes); // Lê os bytes da string e armazena no vetor
                String s = new String(bytes); // Converte os bytes em string
                strings.add(s); // Adiciona a string na lista
            }
            raf.close();
            return strings; // Retorna a lista de strings
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Método auxiliar para escrever uma lista de strings em uma posição específica
    // do arquivo
    private void writeStrings(long pos, List<String> strings) {
        try {
            RandomAccessFile raf = new RandomAccessFile(path, "rw");
            raf.seek(pos); // Posiciona o ponteiro na posição desejada
            raf.writeInt(strings.size()); // Escreve o tamanho da lista de strings
            for (String s : strings) { // Para cada string na lista
                byte[] bytes = s.getBytes(); // Converte a string em bytes
                raf.writeInt(bytes.length); // Escreve o tamanho da string
                raf.write(bytes); // Escreve os bytes da string
            }
            raf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para inserir um novo Registro no arquivo
    @Override
    public void inserir(Registro d) {
        try {
            RandomAccessFile raf = new RandomAccessFile(path, "rw");
            long pos = raf.length(); // Obtém a posição final do arquivo
            List<String> strings = new ArrayList<>(); // Cria uma lista vazia para armazenar as strings
            strings.add(registroToString(d)); // Adiciona a string correspondente ao Registro na lista
            writeStrings(pos, strings); // Escreve a lista de strings na posição final do arquivo
            raf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para buscar um Registro pelo código do projeto no arquivo
    @Override
    public Registro buscar(String codigo) {
        try {
            RandomAccessFile raf = new RandomAccessFile(path, "r");
            long pos = 0; // Inicia a posição no início do arquivo
            while (pos < raf.length()) { // Enquanto não chegar ao final do arquivo
                List<String> strings = readStrings(pos); // Lê a lista de strings da posição atual
                for (String s : strings) { // Para cada string na lista
                    Registro d = stringToRegistro(s); // Converte a string em objeto Registro
                    if (d.getCodigo().equals(codigo)) { // Se o código do Registro for igual ao buscado, retorna o
                                                        // objeto encontrado
                        raf.close();
                        return d;
                    }
                }
                pos += 4 + strings.size() * 4; // Incrementa a posição com o tamanho da lista de strings mais o
                                               // indicador de tamanho (4 bytes)
                for (String s : strings) { // Para cada string na lista, incrementa a posição com o tamanho da string em
                                           // bytes
                    pos += s.getBytes().length;
                }
            }
            raf.close();
            return null; // Se não encontrar o Registro, retorna null
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Método para atualizar um dado no arquivo
    @Override
    public void atualizar(Registro r) {
        try {
            RandomAccessFile raf = new RandomAccessFile(path, "rw");
            long pos = 0; // Inicia a posição no início do arquivo
            while (pos < raf.length()) { // Enquanto não chegar ao final do arquivo
                List<String> strings = readStrings(pos); // Lê a lista de strings da posição atual
                boolean found = false; // Variável para indicar se encontrou o dado ou não
                for (int i = 0; i < strings.size(); i++) { // Para cada índice na lista de strings
                    String s = strings.get(i); // Obtém a string correspondente ao índice
                    Registro old = stringToRegistro(s); // Converte a string em objeto Dado
                    if (old.getCodigo().equals(r.getCodigo())) { // Se o código do dado antigo for igual ao do dado
                                                                 // recebido, atualiza os valores
                        String newS = registroToString(r); // Converte o dado atualizado em string
                        strings.set(i, newS); // Substitui a string antiga pela nova na lista de strings
                        found = true; // Marca que encontrou o dado
                        break; // Sai do loop interno
                    }
                }
                if (found) { // Se encontrou o dado, escreve a nova lista de strings na posição atual do
                             // arquivo e sai do loop externo
                    writeStrings(pos, strings);
                    break;
                }
                pos += 4 + strings.size() * 4; // Incrementa a posição com o tamanho da lista de strings mais o
                                               // indicador de tamanho (4 bytes)
                for (String s : strings) { // Para cada string na lista, incrementa a posição com o tamanho da string em
                                           // bytes
                    pos += s.getBytes().length;
                }
            }
            raf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Registro> listar() {
        try {
            RandomAccessFile raf = new RandomAccessFile(path, "r");
            long pos = 0; // Inicia a posição no início do arquivo
            List<Registro> dados = new ArrayList<>(); // Cria uma lista vazia para armazenar os objetos Dado
            while (pos < raf.length()) { // Enquanto não chegar ao final do arquivo
                List<String> strings = readStrings(pos); // Lê a lista de strings da posição atual
                for (String s : strings) { // Para cada string na lista
                    Registro d = stringToRegistro(s); // Converte a string em objeto Dado
                    dados.add(d); // Adiciona o objeto Dado na lista de dados
                }
                pos += 4 + strings.size() * 4; // Incrementa a posição com o tamanho da lista de strings mais o
                                               // indicador de tamanho (4 bytes)
                for (String s : strings) { // Para cada string na lista, incrementa a posição com o tamanho da string em
                                           // bytes
                    pos += s.getBytes().length;
                }
            }
            raf.close();
            return dados; // Retorna a lista de dados
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void excluir(String codigo) {
        try {
            RandomAccessFile raf = new RandomAccessFile(path, "rw");
            long pos = 0; // Inicia a posição no início do arquivo
            while (pos < raf.length()) { // Enquanto não chegar ao final do arquivo
                List<String> strings = readStrings(pos); // Lê a lista de strings da posição atual
                boolean found = false; // Variável para indicar se encontrou o dado ou não
                for (int i = 0; i < strings.size(); i++) { // Para cada índice na lista de strings
                    String s = strings.get(i); // Obtém a string correspondente ao índice
                    Registro old = stringToRegistro(s); // Converte a string em objeto Dado
                    if (old.getCodigo().equals(codigo)) { // Se o código do dado antigo for igual ao do dado
                        strings.remove(i); // Substitui a string antiga pela nova na lista de strings
                        found = true; // Marca que encontrou o dado
                        break; // Sai do loop interno
                    }
                }
                if (found) { // Se encontrou o dado, escreve a nova lista de strings na posição atual do
                             // arquivo e sai do loop externo
                    writeStrings(pos, strings);
                    break;
                }
                pos += 4 + strings.size() * 4; // Incrementa a posição com o tamanho da lista de strings mais o
                                               // indicador de taille (4 bytes)
                for (String s : strings) { // Para cada string na lista, incrementa a posição com o tamanho da string em
                                           // bytes
                    pos += s.getBytes().length;
                }
            }
            raf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}