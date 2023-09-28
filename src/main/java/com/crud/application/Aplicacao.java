package main.java.com.crud.application;

// Classe Aplicacao que interage com o usuário pelo terminal

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import main.java.com.crud.dao.RegistroDAO;
import main.java.com.crud.dao.RegistroDAOImpl;
import main.java.com.crud.model.Registro;

public class Aplicacao {

    // Atributos da classe
    private RegistroDAO dao; // Objeto DAO
    private Scanner sc; // Objeto Scanner para ler do terminal

    // Construtor da classe que recebe o caminho do arquivo binário
    public Aplicacao(String path) {
        dao = new RegistroDAOImpl(path); // Cria um objeto RegistroDAOImpl a partir do caminho
        sc = new Scanner(System.in); // Cria um objeto Scanner para ler do terminal
    }

    // Método para exibir o menu de opções para o usuário
    public void mostrarMenu() {
        System.out.println("Escolha uma opção:");
        System.out.println("1 - Inserir um novo Registro");
        System.out.println("2 - Buscar um Registro pelo código do projeto");
        System.out.println("3 - Atualizar um Registro pelo código do projeto");
        System.out.println("4 - Excluir um Registro pelo código do projeto");
        System.out.println("5 - Listar todos os Registros");
        System.out.println("0 - Sair do programa");
    }

    // Método para ler uma opção do usuário pelo terminal
    public int lerOpcao() {
        int opcao = sc.nextInt(); // Lê um número inteiro do terminal
        return opcao; // Retorna a opção lida
    }

    public void carregarBaseDeDados(String caminhoArquivoCSV) {
        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivoCSV))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                // Processar cada linha do arquivo CSV e criar objetos Registro
                String[] campos = linha.split(","); // Dividir a linha em campos
                // Criar um objeto Registro com base nos campos e adicionar à base de dados
                Registro registro = new Registro(campos[0], campos[1], Double.parseDouble(campos[2]),
                        Double.parseDouble(campos[3]), Double.parseDouble(campos[4]),
                        LocalDate.parse(campos[5]), LocalDate.parse(campos[6]), LocalDate.parse(campos[7]),
                        campos[8], campos[9]);

                dao.inserir(registro);
            }
            System.out.println("Carga da base de dados concluída com sucesso!");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erro ao carregar a base de dados a partir do arquivo CSV.");
        }
    }

    // Método para ler os Registros de um registro do usuário pelo terminal
    public Registro lerRegistro() {
        System.out.println("Digite o código do projeto:");
        String codigo = sc.nextLine(); // Lê uma linha do terminal como código do projeto
        System.out.println("Digite o setor do projeto:");
        String setor = sc.nextLine(); // Lê uma linha do terminal como setor do projeto
        System.out.println("Digite o valor orçado do projeto:");
        double valorOrcado = sc.nextDouble(); // Lê um número real do terminal como valor orçado do projeto
        System.out.println("Digite o valor negociado do projeto:");
        double valorNegociado = sc.nextDouble(); // Lê um número real do terminal como valor negociado do projeto
        System.out.println("Digite o desconto concedido do projeto:");
        double descontoConcedido = sc.nextDouble(); // Lê um número real do terminal como desconto concedido do projeto
        System.out.println("Digite a data de ativação do lead (no formato AAAA-MM-DD):");
        LocalDate dataAtivacao = LocalDate.parse(sc.next()); // Lê uma data do terminal como data de ativação do lead
        System.out.println("Digite a data de início do projeto (no formato AAAA-MM-DD):");
        LocalDate dataInicio = LocalDate.parse(sc.next()); // Lê uma data do terminal como data de início do projeto
        System.out.println("Digite a data de término do projeto (no formato AAAA-MM-DD):");
        LocalDate dataTermino = LocalDate.parse(sc.next()); // Lê uma data do terminal como data de término do projeto
        sc.nextLine(); // Limpa o buffer de entrada
        System.out.println("Digite o responsável pelo projeto:");
        String responsavel = sc.nextLine(); // Lê uma linha do terminal como responsável pelo projeto
        System.out.println("Digite o status do projeto:");
        String status = sc.nextLine(); // Lê uma linha do terminal como status do projeto
        Registro r = new Registro(codigo, setor, valorOrcado, valorNegociado, descontoConcedido, dataAtivacao,
                dataInicio, dataTermino, responsavel, status); // Cria um objeto Registro com os Registros lidos
        return r; // Retorna o objeto Registro
    }

    // Método para realizar a carga da base de Registros a partir de um arquivo CSV
    public void executarOpcao(int opcao) {
        switch (opcao) { // Verifica qual foi a opção escolhida
            case 1: // Se for 1, insere um novo Registro no arquivo CSV
                Registro r = lerRegistro(); // Lê os Registros do Registro do usuário pelo terminal
                dao.inserir(r); // Insere o Registro no arquivo CSV usando o método do DAO
                System.out.println("Registro inserido com sucesso!");
                break;
            case 2: // Se for 2, busca um Registro pelo código do projeto no arquivo CSV
                System.out.println("Digite o código do projeto que deseja buscar:");
                String codigo = sc.nextLine(); // Lê uma linha do terminal como código do projeto
                r = dao.buscar(codigo); // Busca o Registro pelo código do projeto no arquivo CSV usando o
                                        // método do
                // DAO
                if (r != null) { // Se o Registro não for nulo, exibe os seus Registros
                    System.out.println("Registro encontrado:");
                    System.out.println(r);
                } else { // Se o Registro for nulo, informa que não foi encontrado
                    System.out.println("Registro não encontrado!");
                }
                break;
            case 3: // Se for 3, atualiza um Registro pelo código do projeto no arquivo CSV
                System.out.println("Digite o código do projeto que deseja atualizar:");
                codigo = sc.nextLine(); // Lê uma linha do terminal como código do projeto
                r = dao.buscar(codigo); // Busca o Registro pelo código do projeto no arquivo CSV usando o
                                        // método do
                // DAO
                if (r != null) { // Se o Registro não for nulo, atualiza os seus Registros
                    System.out.println("Registro encontrado:");
                    System.out.println(r);
                    Registro novo = lerRegistro(); // Lê os novos Registros do Registro do usuário pelo terminal
                    novo.setCodigo(codigo); // Mantém o mesmo código do Registro antigo
                    dao.atualizar(novo); // Atualiza o Registro no arquivo CSV usando o método do DAO
                    System.out.println("Registro atualizado com sucesso!");
                } else { // Se o Registro for nulo, informa que não foi encontrado
                    System.out.println("Registro não encontrado!");
                }
                break;
            case 4: // Se for 4, exclui um Registro pelo código do projeto no arquivo CSV
                System.out.println("Digite o código do projeto que deseja excluir:");
                codigo = sc.nextLine(); // Lê uma linha do terminal como código do projeto
                dao.excluir(codigo); // Exclui o Registro pelo código do projeto no arquivo CSV usando o método do
                                     // DAO
                System.out.println("Registro excluído com sucesso!");
                break;
            case 5: // Se for 5, lista todos os Registros do arquivo CSV
                List<Registro> lista = dao.listar(); // Obtém a lista de Registros usando o método do DAO
                if (!lista.isEmpty()) { // Se a lista não estiver vazia, exibe os seus elementos
                    System.out.println("Registros:");
                    for (Registro registro : lista) { // Para cada Registro na lista, exibe os seus Registros
                        System.out.println(registro);
                    }
                } else { // Se a lista estiver vazia, informa que não há Registros
                    System.out.println("Não há Registros!");
                }
                break;
            case 0: // Se for 0, encerra o programa
                System.out.println("Programa encerrado!");
                break;
            default: // Se for qualquer outro valor, informa que a opção é inválida
                System.out.println("Opção inválida!");
        }
    }
}