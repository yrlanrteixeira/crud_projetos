package main.java.com.crud.model;

import java.time.LocalDate;

// Classe Registro que representa um objeto da base de dados
public class Registro {
    // Atributos da classe
    private String codigo; // Código do projeto
    private String setor; // Setor do projeto
    private double valorOrcado; // Valor orçado do projeto
    private double valorNegociado; // Valor negociado do projeto
    private double descontoConcedido; // Desconto concedido do projeto
    private LocalDate dataAtivacao; // Data de ativação do lead
    private LocalDate dataInicio; // Data de início do projeto
    private LocalDate dataTermino; // Data de término do projeto
    private String responsavel; // Responsável pelo projeto
    private String status; // Status do projeto

    // Construtor da classe
    public Registro(String codigo, String setor, double valorOrcado, double valorNegociado, double descontoConcedido,
            LocalDate dataAtivacao, LocalDate dataInicio, LocalDate dataTermino, String responsavel, String status) {
        this.codigo = codigo;
        this.setor = setor;
        this.valorOrcado = valorOrcado;
        this.valorNegociado = valorNegociado;
        this.descontoConcedido = descontoConcedido;
        this.dataAtivacao = dataAtivacao;
        this.dataInicio = dataInicio;
        this.dataTermino = dataTermino;
        this.responsavel = responsavel;
        this.status = status;
    }

    // Métodos get e set para cada atributo
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
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
        return "Dado [codigo=" + codigo + ", setor=" + setor + ", valorOrcado=" + valorOrcado + ", valorNegociado="
                + valorNegociado + ", descontoConcedido=" + descontoConcedido + ", dataAtivacao=" + dataAtivacao
                + ", dataInicio=" + dataInicio + ", dataTermino=" + dataTermino + ", responsavel=" + responsavel
                + ", status=" + status + "]";
    }
}
