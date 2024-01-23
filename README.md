# CRUD de Registros

Este repositório contém um aplicativo de CRUD (Create, Read, Update, Delete) de registros em Java, desenvolvido como parte de um trabalho prático da disciplina de AEDS III na Engenharia de Computação. O sistema permite que você crie, leia, atualize e exclua registros de projetos, bem como importar uma base de dados CSV e ordenar os registros. O objetivo era estudar e aprender mais sobre operações em memória secundária, por isso todas as alterações são salvas em um arquivos de dados em que é possível recuperar os dados inseridos pelo usuário. Além de que os dados seriam armazenados e dessa forma não seriam perdidos ao fim do programa.

## Pré-requisitos

- Java Development Kit (JDK) instalado no seu sistema.
- Um ambiente de desenvolvimento Java, como o Eclipse, IntelliJ IDEA ou qualquer IDE de sua escolha.
- Conhecimento básico em Java e programação.

## Como executar o aplicativo

1. Clone ou faça o download deste repositório.

2. Abra o projeto na sua IDE de escolha.

3. Execute a classe `Main.java`. Isso iniciará o aplicativo e apresentará um menu no console com as seguintes opções:

   - Criar Registro
   - Ler Registro
   - Atualizar Registro
   - Excluir Registro
   - Importar Base de Dados
   - Ordenar
   - Sair

4. Escolha uma das opções digitando o número correspondente e siga as instruções fornecidas no console.

## Funcionalidades

### 1. Criar Registro

Permite ao usuário criar um novo registro de projeto, inserindo informações como setor, valor orçado, valor negociado, desconto concedido, datas de ativação, início e término, responsável e status.

### 2. Ler Registro

Permite ao usuário ler um registro existente, fornecendo o código do projeto desejado. O sistema exibirá os detalhes do projeto se ele existir.

### 3. Atualizar Registro

Permite ao usuário atualizar um registro existente. É necessário fornecer o código do projeto a ser atualizado e, em seguida, digitar os novos valores para os campos relevantes.

### 4. Excluir Registro

Permite ao usuário excluir um registro existente. O usuário deve fornecer o código do projeto a ser excluído.

### 5. Importar Base de Dados

Permite a importação de uma base de dados a partir de um arquivo CSV. Os dados do arquivo CSV serão lidos e convertidos em registros de projeto no sistema.

### 6. Ordenar

Esta opção permite ao usuário ordenar os registros por algum critério específico. A implementação da ordenação não está claramente definida no código, portanto, verifique a classe `Ordenacao` para mais detalhes.

### 7. Listar Todos os Registros

Esta opção permite ao usuário listar todos os registros atualmente armazenados no sistema.

### 0. Sair

Encerra o aplicativo.

## Estrutura do Projeto

- `src/main/java/com/crud/application`: Contém a classe `Aplicacao`, que é a classe principal do aplicativo.
- `src/main/java/com/crud/dao`: Contém interfaces e classes relacionadas ao acesso a dados.
- `src/main/java/com/crud/model`: Contém a classe `Registro`, que representa os registros de projetos.
- `src/main/java/com/crud/db`: Local onde os arquivos de dados são armazenados.

## Considerações Finais

Este é um projeto de exemplo para demonstrar as operações básicas de um sistema de CRUD em Java. Sinta-se à vontade para explorar, modificar e aprender com o código-fonte.

**Atenção:** Este aplicativo é uma demonstração e pode não ser adequado para uso em um ambiente de produção sem melhorias e tratamento de erros adicionais.

## Autor

Este projeto foi desenvolvido por Ramon Vinícius Silva Corrêa e Yrlan Rangel Teixeira como parte do Trabalho Prático da disciplina de AEDS III.

---

**Aproveite a exploração do CRUD de Registros em Java!**
