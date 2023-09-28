package main.java.com.crud.dao;

import java.util.List;

import main.java.com.crud.model.Registro;

// Interface RegistroDAO que define os métodos que o DAO deve implementar
public interface RegistroDAO {

    // Insere um novo registro no arquivo
    public void inserir(Registro r);

    // Busca um registro pelo id no arquivo
    public Registro buscar(String codigo);

    // Atualiza um registro no arquivo
    public void atualizar(Registro r);

    // Exclui um registro no arquivo
    public void excluir(String codigo);

    // Lista todos os registros válidos do arquivo
    public List<Registro> listar();
}
