# Instruções para Rodar o Projeto BancoATM

Siga os passos abaixo para configurar e executar o projeto BancoATM:

## 1. Pré-requisitos

- **NetBeans 2022**: Baixe e instale o NetBeans IDE.
- **JDK 17**: Certifique-se de ter o Java Development Kit (JDK) versão 17 instalado.(Pode baixar o netbeans com outro, mas vai precisar desse para usar a versão 7.0.7 do glassfish)
- **GlassFish 7.0.7**: Baixe e instale o servidor GlassFish 7.0.7.
- **MySQL**: Instale o MySQL Server e o MySQL Workbench.
- **MySQL Connector/J 9.3.0**: Baixe o arquivo `mysql-connector-j-9.3.0.jar`.

## 2. Configuração do Projeto

1. **Abra o projeto no NetBeans**.
2. **Adicione o MySQL Connector/J**:
  - Clique com o botão direito em "Libraries" do projeto.
  - Selecione "Add JAR/Folder" e adicione o arquivo `mysql-connector-j-9.3.0.jar`.
3. **Configure o GlassFish**:
  - No NetBeans, adicione o servidor GlassFish e certifique-se de que ele está usando o JDK 17.

## 3. Configuração do Banco de Dados

1. **Crie o banco de dados**:
  - Abra o MySQL Workbench.
  - No menu "File", selecione "Open SQL Script" e abra o arquivo `dbjava.sql` localizado na pasta `sql` do projeto.
  - Execute o script para criar as tabelas e estruturas necessárias.
2. **(Opcional) Dados Iniciais**:
  - Na pasta `sql` há um arquivo com sugestões de dados iniciais. Execute-o se desejar popular o banco.

## 4. Configuração da String de Conexão

- No projeto, localize a classe de conexão dentro da pasta `model`.
- Edite a string de conexão com as informações do seu banco de dados (usuário, senha, host, porta, nome do banco).

## 5. Executando o Projeto

1. Inicie o servidor GlassFish pelo NetBeans.
2. Execute o projeto pelo NetBeans.

Pronto! O projeto BancoATM estará rodando.