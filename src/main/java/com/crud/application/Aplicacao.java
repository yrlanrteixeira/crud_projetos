package main.java.com.crud.application;

import main.java.com.crud.dao.RegistroDAO;
import main.java.com.crud.model.Registro;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Constructor;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Aplicacao {
    private RegistroDAO registroDAO;
    private static IndiceInvertido indiceInvertido = new IndiceInvertido();

    public Aplicacao(RegistroDAO registroDAO) {
        this.registroDAO = registroDAO;
    }

    /**
     * Função que mostra o menu de seleção
     */
    public void mostrarMenu() {
        System.out.println("\tCRUD de Registros");
        System.out.println("1. Criar Registro");
        System.out.println("2. Ler Registro");
        System.out.println("3. Atualizar Registro");
        System.out.println("4. Excluir Registro");
        System.out.println("5. Importar Base de Dados");
        System.out.println("6. Ordenar");
        System.out.println("7. Listar");
        System.out.println("8. Lista Invertida");
        System.out.println("9. Compressão");
        System.out.println("10. Descompressão");
        System.out.println("0. Sair");
        System.out.print("Escolha uma opção: ");
    }

    /**
     * Função que lê a opção do menu escolhida
     * 
     * @return Retorna a opção digitada pelo usuário
     */
    public int lerOpcao() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }

    /**
     * Função que converte a base de dados CSV em um arquivo .db
     */
    public static void importarBase() {
        BufferedReader file = null;
        String line = "";

        String csvFileName = "src\\main\\java\\com\\crud\\db\\ProjetosBase.csv"; // Caminho do arquivo CSV
        String dbFileName = "src\\main\\java\\com\\crud\\db\\Projetos.db"; // caminho do arquivo DB

        try {
            /**
             * Abre os arquivos DB e CSV
             */
            file = new BufferedReader(new FileReader(csvFileName));
            RandomAccessFile file_2 = new RandomAccessFile(dbFileName, "rw");

            if (file_2.length() > 0) {
                file_2.close();

                File fp = new File(dbFileName);
                fp.delete();
                file_2 = new RandomAccessFile(dbFileName, "rw");
            }
            file_2.seek(0);
            file_2.writeInt(0);
            file_2.close();

            line = file.readLine(); // Ignora a primeira linha de cabeçalho

            /**
             * Percorre todas as linhas do arquivo CSV e escreve no arquivo DB
             */
            while ((line = file.readLine()) != null) {
                String[] content = line.split(",");
                Registro csvRegistro = new Registro();
                csvRegistro.criarObjeto(content[2].toString(),
                        Double.parseDouble(content[3].toString()),
                        Double.parseDouble(content[4].toString()),
                        Double.parseDouble(content[5].toString()),
                        content[6].toString(),
                        content[7].toString(),
                        content[8].toString(),
                        content[9].toString(),
                        content[10].toString());

                try {
                    CRUD<Registro> arquivoDeProjetos = new CRUD<>(Registro.class.getConstructor());
                    arquivoDeProjetos.Create(csvRegistro);
                    // System.out.println("Projeto Criado com Sucesso!");
                } catch (Exception e) {
                    System.out.println("Erro");
                }
            }
            file.close();
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado!");
        } catch (IOException e) {
            System.out.println("ERROR");
        }

    }

    /**
     * Função para o usuário digitar os valores de criação
     * de um novo registro
     */
    public static void criarRegistro() {

        /**
         * Digita os parâmetros desejados do registro
         */
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

        /**
         * Cria um novo objeto Registro passando
         * os parâmetros digitados pelo usuário
         */
        Registro novoProjeto = new Registro();
        novoProjeto.criarObjeto(setor, valorOrcado, valorNegociado, descontoConcedido, dataAtivacao,
                dataInicio, dataTermino, responsavel, status);

        /**
         * Cria o novo registro no arquivo de projetos
         */
        try {
            CRUD<Registro> arquivoDeProjetos = new CRUD<>(Registro.class.getConstructor());
            arquivoDeProjetos.Create(novoProjeto);

            // Adiciona o índice invertido para o responsável do novo projeto
            indiceInvertido.adicionarIndice(responsavel, novoProjeto.getIdProjeto());

            System.out.println("Projeto Criado com Sucesso!");
        } catch (Exception e) {
            System.out.println("Erro");
        }
    }

    /**
     * Função para o usuário escolher o código do
     * projeto que ele deseja ler.
     */
    public static void lerRegistro() {
        System.out.println("Você entrou no método ler.");
        System.out.println("Entre com o Código do Projeto:");
        Scanner entrada = new Scanner(System.in);
        byte idProjeto = entrada.nextByte();

        try {
            CRUD<Registro> arquivoDeProjetos = new CRUD<>(Registro.class.getConstructor());
            Registro projetoProcurado = arquivoDeProjetos.Read(idProjeto);

            if (projetoProcurado.idProjeto != -1) {
                System.out.println("PROJETO ENCONTRADO: ");
                System.out.println(projetoProcurado.toString());
            }

            else {
                System.out.println("PROJETO NÃO ENCONTRADO!");
            }
        } catch (Exception e) {
            System.out.println("Erro");
        }
    }

    /**
     * Função para o usuário escolher o registro que ele
     * deseja atualizar.
     * 
     */
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

    /**
     * Função para o usuário escolher o registro
     * que deseja excluir
     */
    public static void excluirRegistro() {
        System.out.println("Você entrou no método excluir.");
        System.out.println("Entre com o Código do Projeto que deseja excluir:");
        Scanner entrada = new Scanner(System.in);
        byte idProjeto = entrada.nextByte();

        try {
            CRUD<Registro> arquivoDeProjetos = new CRUD<>(Registro.class.getConstructor());
            Registro projetoExistente = arquivoDeProjetos.Delete(idProjeto);

            if (projetoExistente.getIdProjeto() != 0) {
                // Remove o índice invertido para o responsável do projeto excluído
                indiceInvertido.buscarResponsavel(projetoExistente.getResponsavel()).remove(Integer.valueOf(idProjeto));

                System.out.println("Projeto Excluído com Sucesso!");
            } else {
                System.out.println("Projeto Não Encontrado!");
            }
        } catch (Exception e) {
            System.out.println("Erro");
        }
    }

    public static void listarTodosRegistros() {
        try {
            Constructor<Registro> registroConstructor = Registro.class.getConstructor();
            CRUD<Registro> arquivoDeRegistros = new CRUD<>(registroConstructor);
            List<Registro> registros = arquivoDeRegistros.listarTodosRegistros(); // Método que lista todos os registros

            if (!registros.isEmpty()) {
                System.out.println("=====================");
                System.out.println("Registros Encontrados:");
                for (Registro registro : registros) {
                    System.out.println(registro);
                }
            } else {
                System.out.println("Nenhum registro encontrado.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao listar registros.");
        }
    }

    public static void listaInvertida() {
        Scanner entrada = new Scanner(System.in);
        System.out.println("Digite o nome do responsável que deseja buscar:");
        String responsavel = entrada.nextLine();

        // Obtém os índices associados ao responsável da lista invertida
        ArrayList<Integer> resultados = indiceInvertido.buscarResponsavel(responsavel);

        if (!resultados.isEmpty()) {
            System.out.println("Número de ocorrências para o responsável '" + responsavel + "': " +
                    indiceInvertido.obterContadorOcorrencias(responsavel));
            System.out.println("Registros encontrados para o responsável '" + responsavel + "':");

            // Exibe os registros associados aos índices encontrados
            for (Integer id : resultados) {
                try {
                    CRUD<Registro> arquivoDeProjetos = new CRUD<>(Registro.class.getConstructor());
                    Registro registro = arquivoDeProjetos.Read(id.byteValue());

                    if (registro.getIdProjeto() != -1) {
                        // Aqui você pode imprimir todas as informações associadas ao projeto
                        System.out.println("Código: " + registro.getIdProjeto());
                        System.out.println("Setor: " + registro.getSetor());
                        // Adicione outros campos conforme necessário

                        // Adicione uma linha em branco para separar os registros
                        System.out.println();
                    }
                } catch (Exception e) {
                    System.out.println("Erro ao ler o registro com ID: " + id);
                }
            }
        } else {
            System.out.println("Nenhum registro encontrado para o responsável '" + responsavel + "'.");
        }
    }

    /**
     * Função para executar a compressão da base de dados
     */
    public static void comprime() {
        System.out.println("Método de compressão do arquivo original");
        System.out.println("\nDigite a versão da compressão: ");
        Scanner entrada = new Scanner(System.in);
        String versao = entrada.nextLine();
        Huffman huff = new Huffman();
        huff.compress(versao);
        // huff.decompress();
    }

    /**
     * Função para executar a descompressão da base de dados
     */
    public static void descomprime() {
        System.out.println("Método de descompressão");
        System.out.println("\nDigite a versão: ");
        Scanner entrada = new Scanner(System.in);
        String versao = entrada.nextLine();
        Huffman huff = new Huffman();
        huff.decompress(versao);
    }

    /**
     * Função que executa a opção escolhida no menu
     * 
     * @param opcao Opção escolhida
     */
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
                importarBase(); // Importa base de dados CSV
                break;

            case 6:
                Ordenacao o = new Ordenacao();
                o.ordenaArquivo();

            case 7:
                listarTodosRegistros();
                break;

            case 8:
                listaInvertida();
                break;

            case 9:
                comprime();
                break;

            case 10:
                descomprime();
                break;

            case 0:
                System.out.println("Encerrando o programa...");
                System.exit(0);
            default:
                System.out.println("Opção inválida. Tente novamente.");
        }
    }
}