
/**
 * TRABALHO PRÁTICO AEDS III - ENGENHARIA DE COMPUTAÇÃO
 * 
 * ALUNOS: Ramon Vinícius Silva Corrêa
 *         Yrlan Rangel Teixeira
 * 
 * TURMA: 3133.1.01
 */

import main.java.com.crud.application.Aplicacao;
import main.java.com.crud.dao.RegistroDAO;
import main.java.com.crud.model.Registro;

/**
 * Função main que irá rodar o código em loop
 */
public class Main {
    public static void main(String[] args) {
        RegistroDAO dao = new Registro(); // Cria um novo objeto RegistroDAO
        Aplicacao aplicacao = new Aplicacao(dao);
        aplicacao.mostrarMenu();

        while (true) {
            int opcao = aplicacao.lerOpcao();
            aplicacao.executarOpcao(opcao);
            aplicacao.mostrarMenu();
        }
    }
}
