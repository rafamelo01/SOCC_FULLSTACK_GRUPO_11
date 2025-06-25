# Escopo MANTER USUÁRIOS do projeto SOCC

Este projeto é uma parte do escopo do projeto SOCC utilizando **Spring Boot**, **Spring Data JPA** e **H2 Database** para gerenciar usuários e realizar operações de alteração de carga horária mínima (para docentes) e extração em csv da lista de usuarios.

## Funcionalidade

O sistema permite:
- Alterar a **carga horária mínima** de um usuário **com perfil DOCENTE**, somente se o solicitante for um **Diretor** ou **Vice-Diretor**.
- Exportar dados dos usuários em planilha, somente se o solicitante for um **Diretor** ou **Vice-Diretor**.

## Estrutura do Projeto

O projeto é dividido em cinco pacotes principais dentro de **src/main**:

1. **Models**: Contém as classes de modelo que representam as entidades do banco de dados.
2. **Repository**: Contém os repositórios JPA para realizar operações de persistência.
3. **Controller**: Contém a classe controladora UsuarioController responsável por receber as requisições de alteração de carga horária e exportação de usuários.
4. **Service**: Contém a classe UsuarioService com os métodos responsáveis por interagir com os objetos e persistir as alterações no banco com o JPA.
5. **run.socc_manterusuario**: Contém a classe de inicialização do Spring Boot (`TesteUsuarios`) e a lógica de execução e criação de usuários teste.
6. **data**: Contém o arquivo persistido do banco de dados.

## Banco de dados
Contido no pacote **data**, o projeto utiliza o banco de dados H2, um banco relacional leve e embutido, desenvolvido em Java, que persiste os dados localmente em um arquivo no formato .mv.db.

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

## Endpoints de Usuários

### 1. **Exportar Usuários CSV**
Endpoint para exportar todos os usuários do sistema.

- **Método HTTP**: `GET`
- **URL**: `http://localhost:8080/usuarios/exportar`

### 2. **Alterar Carga Horária**
Endpoint para Alteração de Carga Horária de Usuários Docentes.

- **Método HTTP**: `PUT`
- **URL**: `http://localhost:8080/usuarios/carga_horaria`
- **Body** (JSON):
  ```json
  {
      "usuarioId": 2,
      "novaCargaHorariaMinima": "64",
      "solicitanteId": 1
  }
  ```

### 3. **Listar todos os Usuários**
Endpoint para listar todos os usuários do sistema.

- **Método HTTP**: `GET`
- **URL**: `http://localhost:8080/usuarios`

  
### Executar o projeto com o MAVEN
Comandos:
```
mvn clean install
mvn spring-boot:run
```
