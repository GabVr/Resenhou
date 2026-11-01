# 🗣️ Resenhou

![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.1-6DB33F?logo=springboot&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8-4479A1?logo=mysql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-ready-2496ED?logo=docker&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-build-C71A36?logo=apachemaven&logoColor=white)
![Arquitetura](https://img.shields.io/badge/arquitetura-microsservi%C3%A7os-blueviolet)
![OpenAPI](https://img.shields.io/badge/OpenAPI-Swagger%20UI-85EA2D?logo=swagger&logoColor=black)

> Plataforma construída em **arquitetura de microsserviços** com Java e Spring Boot, organizada em um **monorepo** e executada via **Docker Compose**.

O Resenhou é uma plataforma inspirada no Letterboxd, voltada para quem ama livros: descobrir, avaliar, organizar e compartilhar experiências de leitura. Os usuários podem explorar um catálogo de livros, acompanhar o progresso no diário de leitura, escrever avaliações e resenhas e montar listas personalizadas, incluindo a lista “Quero ler”. O sistema é dividido em microsserviços por domínio de negócio (usuários, catálogo e leitura), o que permite desenvolver, testar e escalar cada parte de forma independente. Como próximos passos, o projeto prevê notícias literárias por meio de uma API externa e recursos sociais, como seguidores, curtidas, comentários e feed de atividades.
---

## 🏗️ Arquitetura

O sistema é dividido em serviços independentes, cada um com sua responsabilidade e seu próprio build. O **usuarios** cuida da autenticação: emite tokens JWT assinados com uma **chave privada RSA**. Já **catalogo** e **leitura** funcionam como *resource servers* e validam esses tokens com a **chave pública**, sem consultar o serviço de usuários a cada requisição. A **resenhou-ui** é a interface web e se comunica com o `usuarios` (via `USUARIOS_URL`).

---

## 🧩 Módulos

| Módulo | Responsabilidade | Destaques |
|---|---|---|
| **resenhou-ui** | Interface web do Resenhou | Thymeleaf, Spring Security, Bean Validation |
| **usuarios** | Cadastro, autenticação e gerenciamento de usuários | Emissão de JWT (RSA), OAuth2 Client, e-mails com Resend, usuário inicial automático |
| **catalogo** | Gerenciamento do catálogo da plataforma | Resource Server (valida JWT), JPA + MySQL |
| **leitura** | Funcionalidades de leitura da plataforma | Resource Server (valida JWT), JPA + MySQL |
| **exceptions** | Biblioteca compartilhada de tratamento de erros | `GlobalExceptionHandler` |

<!-- TODO: detalhe o domínio de cada serviço (o que é um "item" do catálogo? o que é "leitura"?) -->

---

## 🛠️ Tecnologias

| Categoria | Tecnologia |
|---|---|
| Linguagem | Java 21 |
| Framework | Spring Boot 4.1.1 (Spring Web MVC) |
| Interface web | Thymeleaf |
| Persistência | Spring Data JPA + MySQL |
| Segurança | Spring Security, OAuth2 Resource Server, OAuth2 Client, JWT (RSA) |
| Validação | Bean Validation (Jakarta Validation) |
| E-mail | [Resend](https://resend.com) *(usuarios)* |
| Documentação | springdoc-openapi (Swagger UI) |
| Produtividade | Lombok |
| Build | Maven |
| Containers | Docker e Docker Compose |
| Testes | Spring Boot Starter Test |

---

## 📁 Estrutura do repositório

```
Resenhou/
├── exceptions/                # Biblioteca compartilhada (GlobalExceptionHandler)
├── usuarios/                  # Microsserviço de usuários e autenticação
│   └── Dockerfile
├── catalogo/                  # Microsserviço de catálogo
│   └── Dockerfile
├── leitura/                   # Microsserviço de leitura
│   └── Dockerfile
├── resenhou-web/              # Interface web (artifactId: resenhou-ui)
│   └── Dockerfile
├── sql/                       # Scripts executados na primeira inicialização do MySQL
├── docker-compose.yml
├── .env.example               # Modelo do .env
├── .env.properties.example    # Modelo do .env.properties
└── README.md
```

Cada módulo é um projeto Maven independente, com seu próprio `pom.xml`.

---

## 🚨 Biblioteca de exceções (`exceptions`)

Módulo compartilhado (`com.example.Resenhou:exceptions:0.0.1-SNAPSHOT`) usado por `usuarios`, `catalogo` e `leitura`. Ele centraliza o tratamento de erros por meio do `GlobalExceptionHandler`, garantindo respostas de erro padronizadas em toda a plataforma.

Os três serviços usam a **raiz do repositório como contexto de build** no Docker Compose, o que permite que seus `Dockerfile`s tenham acesso ao módulo `exceptions` durante o build das imagens.
<!-- TODO: confirme que os Dockerfiles compilam/instalam o módulo exceptions -->

> ⚠️ Ao rodar **sem Docker**, a biblioteca precisa ser instalada manualmente com `mvn clean install` antes de compilar os serviços. Veja [Rodando sem Docker](#-rodando-sem-docker-opcional).

---

## 🚀 Como rodar com Docker

### Pré-requisitos

- [Docker](https://docs.docker.com/get-docker/) com Docker Compose
- [OpenSSL](https://www.openssl.org/) (para gerar as chaves JWT)
- Uma chave de API do [Resend](https://resend.com/api-keys) (para o envio de e-mails)

### Passo a passo

**1. Clone o repositório**

```bash
git clone https://github.com/GabVr/Resenhou.git
cd Resenhou
```

**2. Crie os arquivos de ambiente**

O `docker-compose.yml` carrega **dois** arquivos em cada serviço: `.env` e `.env.properties`.

```bash
cp .env.example .env
cp .env.properties.example .env.properties
```

Preencha as senhas do MySQL e a `RESEND_API_KEY`. Os valores repetidos nos dois arquivos devem ser iguais. Todas as variáveis estão descritas em [Variáveis de ambiente](#-variáveis-de-ambiente).

**3. Gere o par de chaves JWT**

```bash
openssl genpkey -algorithm RSA -pkeyopt rsa_keygen_bits:2048 -out app.key
openssl rsa -pubout -in app.key -out app.pub
```

Como as chaves são lidas via `classpath:`, coloque-as em `src/main/resources` **antes** do build das imagens:

| Serviço | Chave necessária |
|---|---|
| usuarios | `app.key` (privada) e `app.pub` (pública) |
| catalogo | `app.pub` |
| leitura | `app.pub` |

<!-- TODO: confirme em quais serviços cada chave deve ficar -->

> 🔒 Adicione `app.key`, `.env` e `.env.properties` ao `.gitignore`. **A chave privada nunca deve ser versionada.** Como ela é copiada para dentro da imagem do serviço, não publique essas imagens em registries públicos.

**4. Suba todos os serviços**

```bash
docker compose up --build
```

O MySQL sobe primeiro e os serviços `usuarios`, `catalogo` e `leitura` só iniciam depois que o banco estiver saudável (*healthcheck*).

**5. Acesse a aplicação**

| Serviço | URL |
|---|---|
| resenhou-ui | http://localhost:8080 |
| usuarios (Swagger) | http://localhost:8083/swagger-ui.html |
| catalogo (Swagger) | http://localhost:8081/swagger-ui.html |
| leitura (Swagger) | http://localhost:8082/swagger-ui.html |

**Comandos úteis**

```bash
docker compose up -d --build     # sobe em segundo plano
docker compose logs -f usuarios  # acompanha os logs de um serviço
docker compose ps                # lista o estado dos containers
docker compose down              # para os containers
docker compose down -v           # para e apaga também os dados do banco
```

---

## 🔌 Portas

Todos os serviços escutam na porta `8080` **dentro** do container; o Compose publica uma porta diferente para cada um no host.

| Módulo | Container | Host → Container |
|---|---|---|
| resenhou-ui | `web-app` | `8080:8080` |
| catalogo | `catalogo-app` | `8081:8080` |
| leitura | `leitura-app` | `8082:8080` |
| usuarios | `usuario-app` | `8083:8080` |
| MySQL | `resenhou_db` | `3306:3306` |

Entre containers, a comunicação usa o nome do serviço e a porta interna. Exemplo: a UI acessa o serviço de usuários em `http://usuarios:8080` (variável `USUARIOS_URL`).

---

## ⚙️ Variáveis de ambiente

Os modelos estão em [`.env.example`](.env.example) e [`.env.properties.example`](.env.properties.example).

### MySQL

| Variável | Descrição | Exemplo |
|---|---|---|
| `MYSQL_ROOT_PASSWORD` | Senha do usuário root do MySQL | `troque_esta_senha_root` |
| `MYSQL_DATABASE` | Nome do banco de dados | `resenhou` |
| `MYSQL_USER` | Usuário usado pelos serviços | `resenhou_user` |
| `MYSQL_PASSWORD` | Senha do usuário acima | `troque_esta_senha` |
| `MYSQL_HOST` | Host do banco (nome do serviço no Compose) | `mysql` |

### JWT

| Variável | Descrição | Exemplo |
|---|---|---|
| `JWT_PRIVATE_KEY` | Chave privada, usada pelo `usuarios` para assinar os tokens | `classpath:app.key` |
| `JWT_PUBLIC_KEY` | Chave pública, usada para validar os tokens | `classpath:app.pub` |

### Usuário inicial *(apenas desenvolvimento)*

Um usuário é criado automaticamente para facilitar os testes.

| Variável | Descrição |
|---|---|
| `INITIAL_USER_NAME` | Nome completo |
| `INITIAL_USER_USERNAME` | Nome de usuário (login) |
| `INITIAL_USER_EMAIL` | E-mail |
| `INITIAL_USER_PASSWORD` | Senha |
| `INITIAL_USER_CPF` | CPF |
| `INITIAL_USER_BIO` | Biografia |

> ⚠️ Em produção, **troque ou desative** o usuário inicial.

### E-mail

| Variável | Descrição |
|---|---|
| `RESEND_API_KEY` | Chave da API do Resend (`re_...`) |

### Definida no `docker-compose.yml`

| Variável | Serviço | Valor |
|---|---|---|
| `USUARIOS_URL` | resenhou-ui | `http://usuarios:8080` |

---

## 🗄️ Banco de dados

- Um único banco **MySQL** (`resenhou`) é usado pelos serviços.
- Os dados ficam no volume Docker `db_data`, então sobrevivem a `docker compose down`.
- Scripts `.sql` colocados na pasta `sql/` são executados automaticamente **apenas na primeira inicialização** (quando o volume ainda está vazio). Para reexecutá-los, rode `docker compose down -v`.
- O MySQL fica acessível no host em `localhost:3306`, caso queira conectar com um cliente como DBeaver ou MySQL Workbench.

---

## 🔐 Autenticação e segurança

1. O cliente se autentica no **usuarios**, que emite um **JWT assinado com a chave privada (RSA)**.
2. Nas requisições seguintes, o cliente envia o header `Authorization: Bearer <token>`.
3. **catalogo** e **leitura** validam o token com a **chave pública**, de forma independente.
4. Os dados de entrada são validados com Bean Validation, e os erros seguem o padrão do `GlobalExceptionHandler`.

**Boas práticas adotadas**

- Segredos ficam em arquivos de ambiente fora do versionamento, nunca no código.
- Senhas de usuários devem ser armazenadas com hash (ex.: BCrypt).
- Healthcheck no MySQL garante que os serviços só iniciem com o banco disponível.

---

## 📚 Documentação da API

Cada serviço expõe sua própria documentação interativa (OpenAPI/Swagger):

| Serviço | Swagger UI | OpenAPI (JSON) |
|---|---|---|
| usuarios | http://localhost:8083/swagger-ui.html | http://localhost:8083/v3/api-docs |
| catalogo | http://localhost:8081/swagger-ui.html | http://localhost:8081/v3/api-docs |
| leitura | http://localhost:8082/swagger-ui.html | http://localhost:8082/v3/api-docs |

---

## 🔌 Endpoints

> A documentação interativa completa (com os schemas de cada objeto) fica no Swagger de cada serviço. Veja [Documentação da API](#-documentação-da-api).

<!-- TODO: indicar quais rotas exigem token (depende do SecurityConfig de cada serviço) -->

| Serviço | URL base (Docker) | Recursos |
|---|---|---|
| usuarios | `http://localhost:8083` | `/auth`, `/api/usuarios` |
| catalogo | `http://localhost:8081` | `/api/autores`, `/api/editoras`, `/api/livros` |
| leitura | `http://localhost:8082` | `/api/avaliacoes`, `/api/diarios`, `/api/listas`, `/api/quero-ler` |

Nos serviços de **leitura**, `usuarioId` e `livroId` são os IDs de um usuário (serviço `usuarios`) e de um livro (serviço `catalogo`).

---

### 🔑 Autenticação — `usuarios`

| Método | Rota | Descrição | Sucesso |
|---|---|---|---|
| `POST` | `/auth/authenticate` | Autentica o usuário e gera o token JWT | `200 OK` |

**Corpo da requisição**

```json
{
  "username": "teste",
  "password": "sua_senha"
}
```

**Resposta**

```json
{
  "accessToken": "eyJhbGciOiJSUzI1NiJ9..."
}
```

Credenciais inválidas retornam `401 Unauthorized`.

---

### 👤 Usuários — `usuarios`

Base: `/api/usuarios`

| Método | Rota | Descrição | Sucesso |
|---|---|---|---|
| `GET` | `/api/usuarios` | Lista todos os usuários | `200 OK` |
| `GET` | `/api/usuarios/{id}` | Busca um usuário pelo ID | `302 Found` |
| `GET` | `/api/usuarios/buscar?nome={nome}` | Busca um usuário pelo nome | `302 Found` |
| `POST` | `/api/usuarios` | Cria um usuário | `201 Created` |
| `PUT` | `/api/usuarios/{id}` | Atualiza um usuário pelo ID | `200 OK` |
| `DELETE` | `/api/usuarios/{id}` | Remove um usuário pelo ID | `204 No Content` |

---

### ✍️ Autores — `catalogo`

Base: `/api/autores`

| Método | Rota | Descrição | Sucesso |
|---|---|---|---|
| `GET` | `/api/autores` | Lista todos os autores | `200 OK` |
| `GET` | `/api/autores/{id}` | Busca um autor pelo ID | `200 OK` |
| `GET` | `/api/autores/buscar?nome={nome}` | Busca autores pelo nome | `200 OK` |
| `POST` | `/api/autores` | Cria um autor | `201 Created` |
| `PUT` | `/api/autores/{id}` | Atualiza um autor pelo ID | `200 OK` |
| `DELETE` | `/api/autores/{id}` | Remove um autor pelo ID | `204 No Content` |

---

### 🏢 Editoras — `catalogo`

Base: `/api/editoras`

| Método | Rota | Descrição | Sucesso |
|---|---|---|---|
| `GET` | `/api/editoras` | Lista todas as editoras | `200 OK` |
| `GET` | `/api/editoras/{id}` | Busca uma editora pelo ID | `200 OK` |
| `GET` | `/api/editoras/buscar?nome={nome}` | Busca editoras pelo nome | `200 OK` |
| `POST` | `/api/editoras` | Cria uma editora | `201 Created` |
| `PUT` | `/api/editoras/{id}` | Atualiza uma editora pelo ID | `200 OK` |
| `DELETE` | `/api/editoras/{id}` | Remove uma editora pelo ID | `204 No Content` |

---

### 📕 Livros — `catalogo`

Base: `/api/livros`

| Método | Rota | Descrição | Sucesso |
|---|---|---|---|
| `GET` | `/api/livros` | Lista todos os livros | `200 OK` |
| `GET` | `/api/livros/{id}` | Busca um livro pelo ID | `200 OK` |
| `GET` | `/api/livros/buscar/titulo?titulo={titulo}` | Busca livros pelo título | `200 OK` |
| `GET` | `/api/livros/buscar/autor?autor={autor}` | Busca livros por autor | `302 Found` |
| `GET` | `/api/livros/buscar/editora?editora={editora}` | Busca livros por editora | `302 Found` |
| `POST` | `/api/livros` | Cria um livro | `201 Created` |
| `PUT` | `/api/livros/{id}` | Atualiza um livro pelo ID | `200 OK` |
| `DELETE` | `/api/livros/{id}` | Remove um livro pelo ID | `204 No Content` |

---

### ⭐ Avaliações — `leitura`

Base: `/api/avaliacoes`

| Método | Rota | Descrição | Sucesso |
|---|---|---|---|
| `GET` | `/api/avaliacoes` | Lista todas as avaliações | `200 OK` |
| `GET` | `/api/avaliacoes/{id}` | Busca uma avaliação pelo ID | `302 Found` |
| `GET` | `/api/avaliacoes/buscar/usuario?usuarioId={id}` | Busca avaliação por usuário | `302 Found` |
| `GET` | `/api/avaliacoes/buscar/livro?livroId={id}` | Busca avaliação por livro | `302 Found` |
| `POST` | `/api/avaliacoes` | Cria uma avaliação | `201 Created` |
| `PUT` | `/api/avaliacoes/{id}` | Atualiza uma avaliação pelo ID | `200 OK` |
| `DELETE` | `/api/avaliacoes/{id}` | Remove uma avaliação pelo ID | `204 No Content` |

---

### 📓 Diários — `leitura`

Base: `/api/diarios`

| Método | Rota | Descrição | Sucesso |
|---|---|---|---|
| `GET` | `/api/diarios` | Lista todos os diários | `200 OK` |
| `GET` | `/api/diarios/{id}` | Busca um diário pelo ID | `302 Found` |
| `GET` | `/api/diarios/buscar/usuario?usuarioId={id}` | Busca diário por usuário | `302 Found` |
| `GET` | `/api/diarios/buscar/livro?livroId={id}` | Busca diário por livro | `302 Found` |
| `POST` | `/api/diarios` | Cria um diário | `201 Created` |
| `PUT` | `/api/diarios/{id}` | Atualiza um diário pelo ID | `200 OK` |
| `DELETE` | `/api/diarios/{id}` | Remove um diário pelo ID | `204 No Content` |

---

### 📋 Listas — `leitura`

Base: `/api/listas`

| Método | Rota | Descrição | Sucesso |
|---|---|---|---|
| `GET` | `/api/listas` | Lista todas as listas | `200 OK` |
| `GET` | `/api/listas/{id}` | Busca uma lista pelo ID | `302 Found` |
| `GET` | `/api/listas/buscar/usuario?usuarioId={id}` | Busca lista por usuário | `302 Found` |
| `GET` | `/api/listas/buscar/livro?livroId={id}` | Busca lista por livro | `302 Found` |
| `GET` | `/api/listas/buscar/nome?nome={nome}` | Busca listas pelo nome | `200 OK` |
| `POST` | `/api/listas` | Cria uma lista | `201 Created` |
| `PUT` | `/api/listas/{id}` | Atualiza uma lista pelo ID | `200 OK` |
| `DELETE` | `/api/listas/{id}` | Remove uma lista pelo ID | `204 No Content` |

---

### 🔖 Quero ler — `leitura`

Base: `/api/quero-ler`

| Método | Rota | Descrição | Sucesso |
|---|---|---|---|
| `GET` | `/api/quero-ler` | Lista todos os livros marcados como "quero ler" | `200 OK` |
| `GET` | `/api/quero-ler/{id}` | Busca um registro pelo ID | `302 Found` |
| `GET` | `/api/quero-ler/buscar/usuario?usuarioId={id}` | Busca registros por usuário | `302 Found` |
| `GET` | `/api/quero-ler/buscar/livro?livroId={id}` | Busca registros por livro | `302 Found` |
| `POST` | `/api/quero-ler` | Adiciona um livro à lista "quero ler" | `201 Created` |
| `PUT` | `/api/quero-ler/{id}` | Atualiza um registro pelo ID | `200 OK` |
| `DELETE` | `/api/quero-ler/{id}` | Remove um registro pelo ID | `204 No Content` |

---

### 📨 Corpo das requisições

O corpo do `POST` e do `PUT` de cada recurso é o objeto correspondente (`Usuario`, `Autor`, `Editora`, `Livro`, `Avaliacao`, `Diario`, `Lista`, `QueroLer`), validado com Bean Validation (`@Valid`). Consulte os schemas no Swagger.

### ❗ Códigos de erro comuns

Os erros seguem o padrão do `GlobalExceptionHandler` (biblioteca `exceptions`).

| Código | Significado |
|---|---|
| `400 Bad Request` | Dados inválidos (falha na validação) |
| `401 Unauthorized` | Credenciais ou token inválidos |
| `404 Not Found` | Recurso não encontrado |
| `500 Internal Server Error` | Erro inesperado no servidor |

### 🧪 Exemplo de uso

```bash
# 1. Autentique-se e copie o accessToken da resposta
curl -X POST http://localhost:8083/auth/authenticate \
  -H "Content-Type: application/json" \
  -d '{"username": "teste", "password": "sua_senha"}'

# 2. Use o token nas demais requisições
curl http://localhost:8081/api/livros \
  -H "Authorization: Bearer <accessToken>"

curl "http://localhost:8082/api/avaliacoes/buscar/livro?livroId=1" \
  -H "Authorization: Bearer <accessToken>"
```
**Exemplo de requisição autenticada**

```bash
curl http://localhost:8083/usuarios/1 \
  -H "Authorization: Bearer <seu_token>"
```

---

## 👤 Autores

Feito por **[GabVr](https://github.com/GabVr)** e
**[MarcioLuan](https://github.com/MarcioLuan)**

<!-- TODO: adicionar licença (ex.: MIT) e criar o arquivo LICENSE -->