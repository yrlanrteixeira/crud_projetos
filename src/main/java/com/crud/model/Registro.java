package main.java.com.crud.model;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Registro {
    private String cod_projeto;
    private String setor;
    private String valor_orcado;
    private String valor_negociado;
    private String desconto_concedido;
    private String data_ativacao_lead;
    private String data_inicio;
    private String data_termino;
    private String responsavel;
    private String status;
    private int id;

    // Mapeamento dos nomes dos campos do CSV para os campos da classe Registro
    private static final Map<String, String> CAMPO_MAP = new HashMap<>();

    static {
        CAMPO_MAP.put("CÃ³digo Projeto", "cod_projeto");
        CAMPO_MAP.put("Setor", "setor");
        CAMPO_MAP.put("Valor OrÃ§ado", "valor_orcado");
        CAMPO_MAP.put("Valor Negociado", "valor_negociado");
        CAMPO_MAP.put("Desconto Concedido", "desconto_concedido");
        CAMPO_MAP.put("Data AtivaÃ§Ã£o Lead", "data_ativacao_lead");
        CAMPO_MAP.put("Data InÃ­cio", "data_inicio");
        CAMPO_MAP.put("Data Termino", "data_termino");
        CAMPO_MAP.put("ResponsÃ¡vel", "responsavel");
        CAMPO_MAP.put("Status", "status");
    }

    // Construtor vazio
    public Registro() {
    }

    // Getters e setters para cada campo
    public void setId(int id) {
        this.id = id;   
    }
    
    public int getId() {
        return id;
    }
    public String getCod_projeto() {
        return cod_projeto;
    }

    public void setCod_projeto(String cod_projeto) {
        this.cod_projeto = cod_projeto;
    }

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }

    public String getValor_orcado() {
        return valor_orcado;
    }

    public void setValor_orcado(String valor_orcado) {
        this.valor_orcado = valor_orcado;
    }

    public String getValor_negociado() {
        return valor_negociado;
    }

    public void setValor_negociado(String valor_negociado) {
        this.valor_negociado = valor_negociado;
    }

    public String getDesconto_concedido() {
        return desconto_concedido;
    }

    public void setDesconto_concedido(String desconto_concedido) {
        this.desconto_concedido = desconto_concedido;
    }

    public String getData_ativacao_lead() {
        return data_ativacao_lead;
    }

    public void setData_ativacao_lead(String data_ativacao_lead) {
        this.data_ativacao_lead = data_ativacao_lead;
    }

    public String getData_inicio() {
        return data_inicio;
    }

    public void setData_inicio(String data_inicio) {
        this.data_inicio = data_inicio;
    }

    public String getData_termino() {
        return data_termino;
    }

    public void setData_termino(String data_termino) {
        this.data_termino = data_termino;
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
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeUTF(cod_projeto  != null ? cod_projeto : "");
        dos.writeUTF(setor  != null ? setor : "");
        dos.writeUTF(valor_orcado  != null ? valor_orcado : "");
        dos.writeUTF(valor_negociado  != null ? valor_negociado : "");
        dos.writeUTF(desconto_concedido  != null ? desconto_concedido : "");
        dos.writeUTF(data_ativacao_lead  != null ? data_ativacao_lead : "");
        dos.writeUTF(data_inicio  != null ? data_inicio : "");
        dos.writeUTF(data_termino  != null ? data_termino : "");
        dos.writeUTF(responsavel  != null ? responsavel : "");
        dos.writeUTF(status  != null ? status : "");
        
        return baos.toByteArray();
    }
    

    public void fromByteArray(byte[] b) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);

        this.cod_projeto = dis.readUTF();
        this.setor = dis.readUTF();
        this.valor_orcado = dis.readUTF();
        this.valor_negociado = dis.readUTF();
        this.desconto_concedido = dis.readUTF();
        this.data_ativacao_lead = dis.readUTF();
        this.data_inicio = dis.readUTF();
        this.data_termino = dis.readUTF();
        this.responsavel = dis.readUTF();
        this.status = dis.readUTF();
    }
}