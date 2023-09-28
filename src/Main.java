import java.io.IOException;
import java.util.Scanner;

import main.java.com.crud.application.Aplicacao;

public class Main {
    public static void main(String[] args) {
        Aplicacao aplicacao = new Aplicacao();
        byte opcao;
        Scanner entrada = new Scanner(System.in); // usuário digita a opção desejada
        do {
            aplicacao.mostrar();
            opcao = entrada.nextByte(); // armazenamos a opção desejada na variável opção

            switch (opcao) { // switch case que lida com a opção escolhida pelo usuário

                case 1:
                    aplicacao.criar();
                    break;

                case 2:
                    aplicacao.ler();
                    break;

                case 3:
                    aplicacao.atualizar();
                    break;

                case 4:
                    aplicacao.exclui();
                    break;

            }
        } while (opcao != 0); // fica em looping enquanto o usuário não escolher a opção de sair
    }
}
