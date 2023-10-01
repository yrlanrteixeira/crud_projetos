package main.java.com.crud.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import main.java.com.crud.dao.RegistroDAO;

// Classe Registro que representa um objeto da base de dados
public class Registro implements RegistroDAO {
    // Atributos da classe
    public byte idProjeto; // Índice do registro
    private String setor; // Setor do projeto
    private double valorOrcado; // Valor orçado do projeto
    private double valorNegociado; // Valor negociado do projeto
    private double descontoConcedido; // Desconto concedido do projeto
    private LocalDate dataAtivacao; // Data de ativação do lead
    private LocalDate dataInicio; // Data de início do projeto
    private LocalDate dataTermino; // Data de término do projeto
    private String responsavel; // Responsável pelo projeto
    private String status; // Status do projeto

    @Override
    /**
     * Método para criar um Objeto do tipo registro passando os valores de
     * registro como parêmetros
     */
    public void criarObjeto(String setor, double valorOrcado, double valorNegociado,
            double descontoConcedido, String dataAtivacao, String dataInicio, String dataTermino, String responsavel,
            String status) {
        this.idProjeto = 0;
        this.setor = setor;
        this.valorOrcado = valorOrcado;
        this.valorNegociado = valorNegociado;
        this.descontoConcedido = descontoConcedido;
        this.dataAtivacao = LocalDate.parse(dataAtivacao, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        this.dataInicio = LocalDate.parse(dataInicio, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        this.dataTermino = LocalDate.parse(dataTermino, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        this.responsavel = responsavel;
        this.status = status;
    }

    // Métodos get e set para cada atributo
    public byte getIdProjeto() {
        return idProjeto;
    }

    public void setIdProjeto(byte idProjeto) {
        this.idProjeto = idProjeto;
    }

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }

    public double getValorOrcado() {
        return valorOrcado;
    }

    public void setValorOrcado(double valorOrcado) {
        this.valorOrcado = valorOrcado;
    }

    public double getValorNegociado() {
        return valorNegociado;
    }

    public void setValorNegociado(double valorNegociado) {
        this.valorNegociado = valorNegociado;
    }

    public double getDescontoConcedido() {
        return descontoConcedido;
    }

    public void setDescontoConcedido(double descontoConcedido) {
        this.descontoConcedido = descontoConcedido;
    }

    public LocalDate getDataAtivacao() {
        return dataAtivacao;
    }

    public void setDataAtivacao(LocalDate dataAtivacao) {
        this.dataAtivacao = dataAtivacao;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataTermino() {
        return dataTermino;
    }

    public void setDataTermino(LocalDate dataTermino) {
        this.dataTermino = dataTermino;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Método toString para retornar uma representação textual do objeto
    @Override
    public String toString() {
        return String.format(
                "Projetos:%n" +
                        "Código: %s%n" +
                        "Setor: %s%n" +
                        "Valor Orçado: %.2f%n" +
                        "Valor Negociado: %.2f%n" +
                        "Desconto Concedido: %.2f%n" +
                        "Data Ativação: %s%n" +
                        "Data Início: %s%n" +
                        "Data Término: %s%n" +
                        "Responsável: %s%n" +
                        "Status: %s%n",
                idProjeto, setor, valorOrcado, valorNegociado, descontoConcedido, dataAtivacao,
                dataInicio, dataTermino, responsavel, status);
    }

    public byte[] toByteArray() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            dos.writeUTF(this.setor);
            dos.writeDouble(this.valorOrcado);
            dos.writeDouble(this.valorNegociado);
            dos.writeDouble(this.descontoConcedido);
            dos.writeUTF(this.dataAtivacao.toString()); // Convertendo LocalDate para String
            dos.writeUTF(this.dataInicio.toString()); // Convertendo LocalDate para String
            dos.writeUTF(this.dataTermino.toString()); // Convertendo LocalDate para String
            dos.writeUTF(this.responsavel);
            dos.writeUTF(this.status);
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            byte[] b = new byte[0];
            return b;
        }
    }

    public void fromByteArray(byte[] b) {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);
        try {
            this.idProjeto = dis.readByte();
            this.setor = dis.readUTF();
            this.valorOrcado = dis.readDouble();
            this.valorNegociado = dis.readDouble();
            this.descontoConcedido = dis.readDouble();
            this.dataAtivacao = LocalDate.parse(dis.readUTF()); // Convertendo String para LocalDate
            this.dataInicio = LocalDate.parse(dis.readUTF()); // Convertendo String para LocalDate
            this.dataTermino = LocalDate.parse(dis.readUTF()); // Convertendo String para LocalDate
            this.responsavel = dis.readUTF();
            this.status = dis.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
