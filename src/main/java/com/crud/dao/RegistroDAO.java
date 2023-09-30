package main.java.com.crud.dao;

import java.util.List;

import main.java.com.crud.model.Registro;

// Interface RegistroDAO que define os métodos que o RegistroDAOImpl deve implementar
public interface RegistroDAO {
    public void criarObjeto(String setor, double valorOrcado, double valorNegociado,
            double descontoConcedido, String dataAtivacao, String dataInicio, String dataTermino, String responsavel,
            String status);

    public String toString();

    public byte[] toByteArray();

    public void fromByteArray(byte[] b);
}