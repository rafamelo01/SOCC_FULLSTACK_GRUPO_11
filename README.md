# Projeto de Gerenciamento de Usuários

Este projeto é uma parte do escopo do projeto SOCC utilizando **Spring Boot**, **Spring Data JPA** e **H2 Database** para gerenciar usuários e realizar operações de alteração de status, perfil e carga horária mínima (para docentes).

## Funcionalidade

O sistema permite:

- Criar usuários com diferentes perfis.
- Alterar o **status** de um usuário (somente **Diretores** ou **Vice-Diretores** podem fazer isso).
- Alterar o **perfil** de um usuário (também restrito a **Diretores** ou **Vice-Diretores**).
- Alterar a **carga horária mínima** de um usuário **com perfil DOCENTE**, somente se o solicitante for um **Diretor** ou **Vice-Diretor**.

## Estrutura do Projeto

O projeto é dividido em três pacotes principais dentro de **src/main**:

1. **Models**: Contém as classes de modelo que representam as entidades do banco de dados.
2. **Repository**: Contém os repositórios JPA para realizar operações de persistência.
3. **run.socc_manterusuario**: Contém a classe de inicialização do Spring Boot (`TesteUsuarios`) e a lógica de execução.

## Pacotes e Explicação

### 1. **Models**
O pacote **Models** contém as entidades que são mapeadas para tabelas no banco de dados.

- **Usuario**: Representa um usuário do sistema, com informações como nome, email, usuário, status, perfis e, se for docente, carga horária mínima.
  - Contém métodos para alteração de status, perfil e carga horária mínima, com validações de autorização.
- **Status**: Enum que define os possíveis status de um usuário (`ATIVO`, `INATIVO`, `SUSPENSO`).
- **Perfil**: Enum que define os perfis de um usuário (`DIRETOR`, `VICE_DIRETOR`, `DOCENTE`, `DISCENTE`, etc.).

### 2. **Repository**
O pacote **Repository** contém os repositórios que utilizam Spring Data JPA para interagir com o banco de dados. Ele facilita operações como salvar, atualizar, excluir e listar usuários.

- **UsuarioRepository**: Interface que estende `JpaRepository` e permite operações CRUD no banco de dados para a entidade `Usuario`.

### 3. **run.socc_manterusuario**
Este pacote contém a classe principal que inicializa a aplicação e executa a lógica de testes, criando e manipulando usuários.

- **TesteUsuarios**: A classe que implementa `CommandLineRunner`, permitindo executar lógica quando a aplicação é iniciada. Dentro desta classe, são realizados os seguintes passos:
  - Criação de dois usuários: um **Diretor** e um **Aluno**.
  - Alteração do status do **Aluno** por um **Diretor**.
  - Tentativa de alteração de perfil de um usuário por outro **Aluno** (o que deve falhar).
  - Alteração do perfil do **Aluno** por um **Diretor**.
  - Alteração da **carga horária mínima** de um usuário com perfil **DOCENTE**, somente por um **Diretor** (também validado).

## Regras de Negócio

| Operação                       | Permissão Necessária              |
|-------------------------------|-----------------------------------|
| Alterar status                | Diretor ou Vice-Diretor           |
| Alterar perfil                | Diretor ou Vice-Diretor           |
| Alterar carga horária mínima  | Diretor ou Vice-Diretor **e** o usuário deve ser DOCENTE |

## Como Executar

### 1. Clonar o Repositório

Clone este repositório em sua máquina:

```bash
git clone https://github.com/rafamelo01/SOCC_FULLSTACK_GRUPO_11.git

```
### 2. Executar o projeto com o MAVEN

Comandos:

```
mvn clean install
mvn spring-boot:run
```
