package main.java.com.crud.dao;

public interface RegistroDAO {
    // Cria um novo registro
    void criarObjeto(int index, String codigoProjeto, String setor, String valorOrcado,
            String valorNegociado, String descontoConcedido,
            String dataAtivacaoLead, String dataInicio,
            String dataTermino, String responsavel, String status);

    public String toString();

    public byte[] toByteArray();

    public void fromByteArray(byte[] b);
}
