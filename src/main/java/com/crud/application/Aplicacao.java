package main.java.com.crud.application;

import java.io.IOException;
import java.util.Scanner;
import main.java.com.crud.dao.RegistroDAOImpl;
import main.java.com.crud.model.Registro;

public class Aplicacao {
    private RegistroDAOImpl registroDAO;
    private Scanner scanner;

    public Aplicacao() throws IOException {
        registroDAO = new RegistroDAOImpl();
        scanner = new Scanner(System.in);
    }

    public void iniciar() {
        while (true) {
            System.out.println("1. Carregar dados");
            System.out.println("2. Criar registro");
            System.out.println("3. Ler registro");
            System.out.println("4. Atualizar registro");
            System.out.println("5. Deletar registro");
            System.out.println("6. Sair");
            System.out.print("Escolha uma opção: ");
            int opcao = scanner.nextInt();
            switch (opcao) {
                case 1:
                    String caminhoDoArquivo = "src\\main\\java\\com\\crud\\utils\\ProjetosBase.csv"; // substitua pelo caminho correto para o arquivo CSV
                    registroDAO.carregarDados(caminhoDoArquivo);
                    break;
                case 2:
                    Registro novoRegistro = new Registro(); // substitua por uma instância de Registro com os dados corretos
                    registroDAO.criarRegistro(novoRegistro);
                    break;
                case 3:
                    System.out.print("Digite o ID do registro que deseja ler: ");
                    int id = scanner.nextInt();
                    Registro registro = registroDAO.lerRegistro(id);
                    System.out.println(registro); // imprime o registro
                    break;
                case 4:
                    System.out.print("Digite o ID do registro que deseja atualizar: ");
                    id = scanner.nextInt();
                    Registro novoRegistroAtualizado = new Registro(); // substitua por uma instância de Registro com os novos dados
                    registroDAO.atualizarRegistro(id, novoRegistroAtualizado);
                    break;
                case 5:
                    System.out.print("Digite o ID do registro que deseja deletar: ");
                    id = scanner.nextInt();
                    registroDAO.deletarRegistro(id);
                    break;
                case 6:
                    System.exit(0);
            }
        }
    }
}