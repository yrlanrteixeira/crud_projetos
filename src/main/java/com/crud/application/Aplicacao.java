package main.java.com.crud.application;

import java.util.Scanner;
import main.java.com.crud.dao.RegistroDAOImpl;
import main.java.com.crud.model.Registro;

public class Aplicacao {

    public void mostrar() {
        // método que mostra na tela as opções do menu
        System.out.println("\tCRUD de Registros");
        System.out.println("1. Criar Registro");
        System.out.println("2. Ler Registro");
        System.out.println("3. Atualizar Registro");
        System.out.println("4. Excluir Registro");
        System.out.println("0. Fim");

        System.out.println("Opcao:");
    }

    public void criar() {
        System.out.println("Você entrou no método criar Registro.");
        String codigoProjeto;
        String setor;
        double valorOrcado;
        double valorNegociado;
        double descontoConcedido;
        String dataAtivacaoLead;
        String dataInicio;
        String dataTermino;
        String responsavel;
        String status;

        Scanner entrada = new Scanner(System.in);

        System.out.println("Entre com o código do projeto:");
        codigoProjeto = entrada.nextLine();

        System.out.println("Entre com o setor do projeto:");
        setor = entrada.nextLine();

        System.out.println("Entre com o valor orçado do projeto:");
        valorOrcado = entrada.nextDouble();
        entrada.nextLine(); // Consumir a quebra de linha

        System.out.println("Entre com o valor negociado do projeto:");
        valorNegociado = entrada.nextDouble();
        entrada.nextLine();

        System.out.println("Entre com o desconto concedido do projeto:");
        descontoConcedido = entrada.nextDouble();
        entrada.nextLine();

        System.out.println("Entre com a data de ativação do lead do projeto:");
        dataAtivacaoLead = entrada.nextLine();

        System.out.println("Entre com a data de início do projeto:");
        dataInicio = entrada.nextLine();

        System.out.println("Entre com a data de término do projeto:");
        dataTermino = entrada.nextLine();

        System.out.println("Entre com o responsável pelo projeto:");
        responsavel = entrada.nextLine();

        System.out.println("Entre com o status do projeto:");
        status = entrada.nextLine();

        RegistroDAOImpl novoRegistro = new RegistroDAOImpl();
        novoRegistro.criarObjeto(-1, codigoProjeto, setor, String.valueOf(valorOrcado),
                String.valueOf(valorNegociado), String.valueOf(descontoConcedido),
                dataAtivacaoLead, dataInicio, dataTermino, responsavel, status);

        try {
            Registro<RegistroDAOImpl> arquivoDeRegistros = new Registro<>(RegistroDAOImpl.class.getConstructor());
            arquivoDeRegistros.Create(novoRegistro);
            System.out.println("Registro criado com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao criar registro.");
        }
    }

    public void ler() {
        System.out.println("Você entrou no método ler Registro.");
        System.out.println("Entre com o ID do Registro:");
        Scanner entrada = new Scanner(System.in); // lê o ID
        byte id = entrada.nextByte();

        try {
            Registro<RegistroDAOImpl> arquivoDeRegistros = new Registro<>(RegistroDAOImpl.class.getConstructor());
            RegistroDAOImpl registroProcurado = arquivoDeRegistros.Read(id); // método que busca e retorna o Registro
                                                                             // que possui o ID igual ao ID passado pelo
                                                                             // usuário
            if (registroProcurado.id != -1) { // se o registro for encontrado
                System.out.println("Registro ENCONTRADO: ");
                System.out.println(registroProcurado.toString()); // mostra o registro encontrado
            } else { // se não for encontrado
                System.out.println("Registro NÃO ENCONTRADO!");
            }
        } catch (Exception e) {
            System.out.println("Erro ao ler registro.");
        }
    }

    public void atualizar() {
        System.out.println("Você entrou no método atualizar Registro.");
        System.out.println("Entre com o ID do Registro:");
        Scanner entrada = new Scanner(System.in);
        int id = entrada.nextInt();
        entrada.nextLine(); // Consumir a quebra de linha

        entrada = new Scanner(System.in);

        System.out.println("Entre com o novo código do projeto:");
        String codigoProjeto = entrada.nextLine();

        System.out.println("Entre com o novo setor do projeto:");
        String setor = entrada.nextLine();

        System.out.println("Entre com o novo valor orçado do projeto:");
        double valorOrcado = entrada.nextDouble();
        entrada.nextLine(); // Consumir a quebra de linha

        System.out.println("Entre com o novo valor negociado do projeto:");
        double valorNegociado = entrada.nextDouble();
        entrada.nextLine();

        System.out.println("Entre com o novo desconto concedido do projeto:");
        double descontoConcedido = entrada.nextDouble();
        entrada.nextLine();

        System.out.println("Entre com a nova data de ativação do lead do projeto:");
        String dataAtivacaoLead = entrada.nextLine();

        System.out.println("Entre com a nova data de início do projeto:");
        String dataInicio = entrada.nextLine();

        System.out.println("Entre com a nova data de término do projeto:");
        String dataTermino = entrada.nextLine();

        System.out.println("Entre com o novo responsável pelo projeto:");
        String responsavel = entrada.nextLine();

        System.out.println("Entre com o novo status do projeto:");
        String status = entrada.nextLine();

        // Criar um objeto RegistroDAOImpl com os dados atualizados
        RegistroDAOImpl registroAtualizado = new RegistroDAOImpl();
        registroAtualizado.criarObjeto(id, codigoProjeto, setor, String.valueOf(valorOrcado),
                String.valueOf(valorNegociado), String.valueOf(descontoConcedido),
                dataAtivacaoLead, dataInicio, dataTermino, responsavel, status);
        registroAtualizado.id = (byte) id;

        try {
            // Crie a instância da classe Registro que cuida das operações no arquivo
            Registro<RegistroDAOImpl> arquivoDeRegistros = new Registro<>(RegistroDAOImpl.class.getConstructor());
            arquivoDeRegistros.Update(registroAtualizado); // Chama o método que atualiza o registro
            System.out.println("Registro atualizado com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao atualizar registro.");
        }
    }

    public void exclui() {
        System.out.println("Você entrou no método Excluir Registro.");
        System.out.println("Entre com o ID do Registro:");
        Scanner entrada = new Scanner(System.in); // lê o ID
        int id = entrada.nextInt();
        entrada.nextLine(); // Consumir a quebra de linha

        try {
            // cria a instância da classe Registro que cuida das operações no arquivo
            Registro<RegistroDAOImpl> arquivoDeRegistros = new Registro<>(RegistroDAOImpl.class.getConstructor());
            arquivoDeRegistros.Delete(id); // chama o método que apaga o Registro que possui o ID passado pelo usuário
            System.out.println("Registro EXCLUÍDO com sucesso!");
        } catch (Exception e) { // em caso de algum erro
            System.out.println("Erro ao excluir registro.");
        }
    }

}
