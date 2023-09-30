package main.java.com.crud.application;

import main.java.com.crud.dao.RegistroDAO;
import main.java.com.crud.model.Registro;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class Aplicacao {
    private RegistroDAO registroDAO;

    public Aplicacao(RegistroDAO registroDAO) {
        this.registroDAO = registroDAO;
    }

    public void mostrarMenu() {
        System.out.println("\tCRUD de Registros");
        System.out.println("1. Criar Registro");
        System.out.println("2. Ler Registro");
        System.out.println("3. Atualizar Registro");
        System.out.println("4. Excluir Registro");
        System.out.println("0. Sair");
        System.out.print("Escolha uma opção: ");
    }

    public int lerOpcao() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }

    public static void criarRegistro() {
        System.out.println("Você entrou no método criar.");
        Scanner entrada = new Scanner(System.in);
        System.out.println("Entre com o Setor:");
        String setor = entrada.nextLine();
        System.out.println("Entre com o Valor Orcado:");
        double valorOrcado = entrada.nextDouble();
        System.out.println("Entre com o Valor Negociado:");
        double valorNegociado = entrada.nextDouble();
        System.out.println("Entre com o Desconto Concedido:");
        double descontoConcedido = entrada.nextDouble();
        System.out.println("Entre com a Data de Ativacao (dd/MM/yyyy):");
        String dataAtivacao = entrada.next();
        System.out.println("Entre com a Data de Inicio (dd/MM/yyyy):");
        String dataInicio = entrada.next();
        System.out.println("Entre com a Data de Termino (dd/MM/yyyy):");
        String dataTermino = entrada.next();
        entrada.nextLine(); // Limpa o buffer
        System.out.println("Entre com o Responsavel:");
        String responsavel = entrada.nextLine();
        System.out.println("Entre com o Status:");
        String status = entrada.nextLine();
        Registro novoProjeto = new Registro();
        novoProjeto.criarObjeto(setor, valorOrcado, valorNegociado, descontoConcedido, dataAtivacao,
                dataInicio, dataTermino, responsavel, status);
        try {
            CRUD<Registro> arquivoDeProjetos = new CRUD<>(Registro.class.getConstructor());
            arquivoDeProjetos.Create(novoProjeto);
            System.out.println("Projeto Criado com Sucesso!");
        } catch (Exception e) {
            System.out.println("Erro");
        }
    }

    public static void lerRegistro() {
        System.out.println("Você entrou no método ler.");
        System.out.println("Entre com o Código do Projeto:");
        Scanner entrada = new Scanner(System.in);
        byte idProjeto = entrada.nextByte();

        try {
            CRUD<Registro> arquivoDeProjetos = new CRUD<>(Registro.class.getConstructor());
            Registro projetoProcurado = arquivoDeProjetos.Read(idProjeto);

            if (projetoProcurado.getIdProjeto() != 0) {
                System.out.println("PROJETO ENCONTRADO: ");
                System.out.println("Código do Projeto: " + projetoProcurado.getIdProjeto());
                System.out.println("Setor: " + projetoProcurado.getSetor());
                System.out.println("Valor Orçado: " + projetoProcurado.getValorOrcado());
                System.out.println("Valor Negociado: " + projetoProcurado.getValorNegociado());
                System.out.println("Desconto Concedido: " + projetoProcurado.getDescontoConcedido());
                System.out.println("Data de Ativação: " + projetoProcurado.getDataAtivacao());
                System.out.println("Data de Início: " + projetoProcurado.getDataInicio());
                System.out.println("Data de Término: " + projetoProcurado.getDataTermino());
                System.out.println("Responsável: " + projetoProcurado.getResponsavel());
                System.out.println("Status: " + projetoProcurado.getStatus());
            } else {
                System.out.println("PROJETO NÃO ENCONTRADO!");
            }
        } catch (Exception e) {
            System.out.println("Erro");
        }
    }

    public static void atualizarRegistro() {
        System.out.println("Você entrou no método atualizar.");
        System.out.println("Entre com o Código do Projeto que deseja atualizar:");
        Scanner entrada = new Scanner(System.in);
        byte codigoProjeto = entrada.nextByte();

        try {
            CRUD<Registro> arquivoDeProjetos = new CRUD<>(Registro.class.getConstructor());
            Registro projetoExistente = arquivoDeProjetos.Read(codigoProjeto);

            if (projetoExistente.getIdProjeto() != 0) {
                System.out.println("Entre com os novos dados do Projeto:");

                // Solicite ao usuário os novos valores para o projeto
                System.out.println("Novo Setor:");
                entrada = new Scanner(System.in);
                String novoSetor = entrada.nextLine();

                System.out.println("Novo Valor Orçado:");
                double novoValorOrcado = entrada.nextDouble();

                System.out.println("Novo Valor Negociado:");
                double novoValorNegociado = entrada.nextDouble();

                System.out.println("Novo Desconto Concedido:");
                double novoDescontoConcedido = entrada.nextDouble();

                System.out.println("Nova Data de Ativação (dd/MM/yyyy):");
                entrada = new Scanner(System.in);
                String novaDataAtivacao = entrada.nextLine();

                System.out.println("Nova Data de Início (dd/MM/yyyy):");
                String novaDataInicio = entrada.nextLine();

                System.out.println("Nova Data de Término (dd/MM/yyyy):");
                String novaDataTermino = entrada.nextLine();

                System.out.println("Novo Responsável:");
                String novoResponsavel = entrada.nextLine();

                System.out.println("Novo Status:");
                String novoStatus = entrada.nextLine();

                // Atualize os valores do projeto existente com os novos valores
                projetoExistente.setSetor(novoSetor);
                projetoExistente.setValorOrcado(novoValorOrcado);
                projetoExistente.setValorNegociado(novoValorNegociado);
                projetoExistente.setDescontoConcedido(novoDescontoConcedido);
                projetoExistente
                        .setDataAtivacao(LocalDate.parse(novaDataAtivacao, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                projetoExistente
                        .setDataInicio(LocalDate.parse(novaDataInicio, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                projetoExistente
                        .setDataTermino(LocalDate.parse(novaDataTermino, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                projetoExistente.setResponsavel(novoResponsavel);
                projetoExistente.setStatus(novoStatus);

                // Use o CRUD para atualizar o projeto no arquivo
                arquivoDeProjetos.Update(projetoExistente);

                System.out.println("Projeto Atualizado com Sucesso!");
            } else {
                System.out.println("Projeto Não Encontrado!");
            }
        } catch (Exception e) {
            System.out.println("Erro");
        }
    }

    public static void excluirRegistro() {
        System.out.println("Você entrou no método excluir.");
        System.out.println("Entre com o Código do Projeto que deseja excluir:");
        Scanner entrada = new Scanner(System.in);
        byte idProjeto = entrada.nextByte();

        try {
            CRUD<Registro> arquivoDeProjetos = new CRUD<>(Registro.class.getConstructor());
            Registro projetoExistente = arquivoDeProjetos.Delete(idProjeto);

            if (projetoExistente.getIdProjeto() != 0) {
                System.out.println("Projeto Excluído com Sucesso!");
            } else {
                System.out.println("Projeto Não Encontrado!");
            }
        } catch (Exception e) {
            System.out.println("Erro");
        }
    }

    public static void executarOpcao(int opcao) {
        switch (opcao) {
            case 1:
                criarRegistro(); // Chama o método criar da classe Menu
                break;
            case 2:
                lerRegistro(); // Chama o método ler da classe Menu
                break;
            case 3:
                atualizarRegistro(); // Chama o método atualizar da classe Menu
                break;
            case 4:
                excluirRegistro(); // Chama o método excluir da classe Menu
                break;
            case 5:
                listarTodosRegistros(); // Chama um método que lista todos os registros
                break;
            case 0:
                System.out.println("Encerrando o programa...");
                System.exit(0);
            default:
                System.out.println("Opção inválida. Tente novamente.");
        }
    }

    public static void listarTodosRegistros() {
        try {
            CRUD<Registro> arquivoDeRegistros = new CRUD<>(Registro.class.getConstructor());
            List<Registro> registros = arquivoDeRegistros.listarTodosRegistros(); // Método que lista todos os registros

            if (!registros.isEmpty()) {
                System.out.println("Registros Encontrados:");
                for (Registro registro : registros) {
                    System.out.println(registro);
                }
            } else {
                System.out.println("Nenhum registro encontrado.");
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar registros.");
        }
    }
}