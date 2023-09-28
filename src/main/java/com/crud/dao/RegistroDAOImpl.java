package main.java.com.crud.dao;

import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;

public class RegistroDAOImpl implements RegistroDAO {

    // Atributos
    protected int index;
    public byte id;
    public String codigoProjeto;
    protected String setor;
    protected String valorOrcado;
    protected String valorNegociado;
    protected String descontoConcedido;
    protected String dataAtivacaoLead;
    protected String dataInicio;
    protected String dataTermino;
    protected String responsavel;
    protected String status;
    protected int tam;

    // Cria uma instância da classe Registro com os campos especificados
    public void criarObjeto(int index, String codigoProjeto, String setor, String valorOrcado,
            String valorNegociado, String descontoConcedido,
            String dataAtivacaoLead, String dataInicio,
            String dataTermino, String responsavel, String status) {
        this.codigoProjeto = codigoProjeto;
        this.setor = setor;
        this.valorOrcado = valorOrcado;
        this.valorNegociado = valorNegociado;
        this.descontoConcedido = descontoConcedido;
        this.dataAtivacaoLead = dataAtivacaoLead;
        this.dataInicio = dataInicio;
        this.dataTermino = dataTermino;
        this.responsavel = responsavel;
        this.status = status;
    }

    // Retorna uma representação em string do registro
    public String toString() {
        return "\nIndex: " + index +
                " \nCódigo do Projeto: " + codigoProjeto +
                "\nSetor: " + setor +
                "\nValor Orçado: " + valorOrcado +
                "\nValor Negociado: " + valorNegociado +
                "\nDesconto Concedido: " + descontoConcedido +
                "\nData de Ativação do Lead: " + dataAtivacaoLead +
                "\nData de Início: " + dataInicio +
                "\nData de Término: " + dataTermino +
                "\nResponsável: " + responsavel +
                "\nStatus: " + status;
    }

    public byte[] toByteArray() { // Retorna um array de bytes para escrever no arquivo
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            dos.writeByte(this.id);
            dos.writeInt(this.index);
            dos.writeUTF(this.codigoProjeto);
            dos.writeUTF(this.setor);
            dos.writeUTF(this.valorOrcado);
            dos.writeUTF(this.valorNegociado);
            dos.writeUTF(this.descontoConcedido);
            dos.writeUTF(this.dataAtivacaoLead);
            dos.writeUTF(this.dataInicio);
            dos.writeUTF(this.dataTermino);
            dos.writeUTF(this.responsavel);
            dos.writeUTF(this.status);
            return baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            byte[] b = new byte[0];
            return b;
        }
    }

    public void fromByteArray(byte[] b) { // Recebe um array de bytes vindo do arquivo e lê salvando cada propriedade
                                          // devidamente
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);
        try {
            this.id = dis.readByte();
            this.index = dis.readInt();
            this.codigoProjeto = dis.readUTF();
            this.setor = dis.readUTF();
            this.valorOrcado = dis.readUTF();
            this.valorNegociado = dis.readUTF();
            this.descontoConcedido = dis.readUTF();
            this.dataAtivacaoLead = dis.readUTF();
            this.dataInicio = dis.readUTF();
            this.dataTermino = dis.readUTF();
            this.responsavel = dis.readUTF();
            this.status = dis.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
