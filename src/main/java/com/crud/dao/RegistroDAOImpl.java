package main.java.com.crud.dao;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;

import main.java.com.crud.model.Registro;

public class RegistroDAOImpl implements RegistroDAO {
    
    private RandomAccessFile file;
    private Indice index;

    public RegistroDAOImpl() {
        try {
            file = new RandomAccessFile("src\\main\\java\\com\\crud\\utils\\ProjetosBase.db", "rw");
            index = new Indice();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void carregarDados(String caminhoDoArquivo) {
              caminhoDoArquivo = "src\\main\\java\\com\\crud\\utils\\ProjetosBase.csv";
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(caminhoDoArquivo), StandardCharsets.UTF_8))) {
            br.readLine(); // Pular a primeira linha
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] campos = linha.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                if (campos.length < 10) { // Verificar se a linha tem todos os campos esperados
                    System.out.println("Linha ignorada por falta de campos: " + linha);
                    continue;
                }
                try {
                    String codigoProjeto = campos[0];
                    String setor = campos[1];
                    String valorOrcado = campos[2].replaceAll("[R$\",]", "").trim();
                    String valorNegociado = campos[3].replaceAll("[R$\",]", "").trim();
                    String descontoConcedido = campos[4].replaceAll("[R$\",]", "").trim();
                    String dataAtivacaoLead = campos[5].trim();
                    String dataInicio = campos[6].trim();
                    String dataTermino = campos[7].trim();
                    String responsavel = campos[8];
                    String status = campos[9];

                    // Aqui você pode fazer o que precisar com os valores lidos, por exemplo, criar um objeto com esses dados.
                    System.out.println("Código Projeto: " + codigoProjeto);
                    System.out.println("Setor: " + setor);
                    System.out.println("Valor Orçado: " + valorOrcado);
                    System.out.println("Valor Negociado: " + valorNegociado);
                    System.out.println("Desconto Concedido: " + descontoConcedido);
                    System.out.println("Data Ativação Lead: " + dataAtivacaoLead);
                    System.out.println("Data Início: " + dataInicio);
                    System.out.println("Data Término: " + dataTermino);
                    System.out.println("Responsável: " + responsavel);
                    System.out.println("Status: " + status);
                } catch (NumberFormatException e) {
                    System.out.println("Erro ao processar a linha: " + linha);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void criarRegistro(Registro registro) {
        byte[] b;

        try {
            byte id = 1;
            file.seek(0);
            if (file.length() != 0) {
                id = file.readByte();
                id++;
                file.seek(0);
                file.writeByte(id);
            }
            file.seek(file.length());

            long posAtual = file.getFilePointer();
            file.writeByte(' ');
            b = registro.toByteArray();
            file.writeInt(b.length);
            file.writeByte(id);
            file.write(b);
            index.insert(id, posAtual);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Registro lerRegistro(int id) {
        Registro registro = null;
        try {
            long pos = index.search(id);
            if (pos == -1) {
                registro = new Registro();
                registro.setId(-1);
                return registro;
            }
            file.seek(pos);

            if (file.readByte() == ' ') {
                int tamanho = file.readInt();
                byte[] dados = new byte[tamanho];
                file.read(dados);
                registro = new Registro();
                registro.fromByteArray(dados);

                if (registro.getId() == id) {
                    return registro;
                }
            } else {
                registro = new Registro();
                registro.setId(-1);
                return registro;
            }

            registro = new Registro();
            registro.setId(-1);
            return registro;
        } catch (IOException e) {
            e.printStackTrace();
            registro = new Registro();
            registro.setId(-1);
            return registro;
        }
    }

    @Override
    public void atualizarRegistro(int id, Registro novoRegistro) {
        try {
            file.seek(id); // vai para o registro com o ID especificado
            byte lapide = file.readByte(); // lê a lápide
            if (lapide == 0) { // se o registro é válido
                int tamanhoAntigo = file.readInt(); // lê o tamanho do registro antigo
                byte[] dadosNovos = novoRegistro.toByteArray();
                if (dadosNovos.length <= tamanhoAntigo) { // se o novo registro é menor ou igual ao antigo
                    file.seek(id + 1); // volta para o início do registro
                    file.writeInt(dadosNovos.length); // escreve o novo tamanho do registro
                    file.write(dadosNovos); // escreve os novos dados do registro
                } else { // se o novo registro é maior que o antigo
                    file.seek(id); // volta para o início do registro
                    file.writeByte(1); // marca o registro antigo como excluído (lápide)
                    criarRegistro(novoRegistro); // cria um novo registro no final do arquivo
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deletarRegistro(int id) {
        try {
            file.seek(id); // vai para o registro com o ID especificado
            file.writeByte(1); // marca o registro como excluído (lápide)
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}