import java.io.IOException;
import java.util.Scanner;

import main.java.com.crud.application.Aplicacao;

// Classe Main que contém apenas o método main
public class Main {

    // Método principal que executa o programa
    public static void main(String[] args) {
        Aplicacao app = new Aplicacao("registros.db"); // Cria um objeto Aplicacao com o caminho do arquivo binário
        int opcao; // Declara uma variável para armazenar a opção escolhida pelo usuário
        do { // Repete até que a opção seja zero
            app.mostrarMenu(); // Exibe o menu de opções para o usuário
            opcao = app.lerOpcao(); // Lê a opção escolhida pelo usuário pelo terminal
            app.executarOpcao(opcao); // Executa a opção escolhida pelo usuário
        } while (opcao != 0); // Enquanto a opção não for zero
    }
}
