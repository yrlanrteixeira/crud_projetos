package main.java.com.crud.dao;

import main.java.com.crud.model.Registro;

public interface RegistroDAO {
    void carregarDados(String caminhoDoArquivo);
    void criarRegistro(Registro registro);
    Registro lerRegistro(int id);
    void atualizarRegistro(int id, Registro novoRegistro);
    void deletarRegistro(int id);
}
