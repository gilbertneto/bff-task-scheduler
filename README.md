# bff-task-scheduler

Orquestrador baseado no padrão **BFF (Backend for Frontend)** e ponto único de entrada da API. Centraliza a documentação (Swagger/OpenAPI) e integra os microsserviços de usuários, tarefas e notificações.

## Responsabilidades

- Expor uma API única, agregando as chamadas aos serviços `user`, `task-scheduler` e `notification`.
- Executar uma rotina agendada (`@Scheduled`) que verifica tarefas com vencimento na próxima hora, dispara a notificação por e-mail e atualiza o status da tarefa para `NOTIFICADO`.
- Centralizar o tratamento de erros das chamadas Feign (`FeignConfig` / `FeignError`).

## Stack

| Camada | Tecnologia |
|---|---|
| Linguagem | Java 17 |
| Framework | Spring Boot 3.5.5 |
| Integração | Spring Cloud OpenFeign (3 clients: `usuario`, `agendador-tarefas`, `notificacao`) |
| Agendamento | Spring Scheduling (`@Scheduled`) |
| Documentação | springdoc-openapi / Swagger |
| Build | Maven |
| Container | Docker |

## Endpoints principais

### `/usuario`
Rotas de cadastro, login, atualização, endereço, telefone e consulta de CEP — repassadas ao microsserviço `user`.

### `/tarefas`
| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/tarefas` | Cria uma nova tarefa |
| `GET` | `/tarefas` | Lista tarefas do usuário autenticado |
| `GET` | `/tarefas/eventos?dataInicial=&dataFinal=` | Lista tarefas por período |
| `PATCH` | `/tarefas?status=&id=` | Altera status de notificação |
| `PUT` | `/tarefas?id=` | Atualiza uma tarefa |
| `DELETE` | `/tarefas?id=` | Cancela uma tarefa |

## Rotina automática de notificação

A cada 5 minutos (configurável), o serviço busca tarefas com vencimento na próxima hora e dispara o e-mail de notificação correspondente:

```properties
cron.horario=0 0/5 * * * ?
```

## Configuração

Variáveis definidas em `src/main/resources/application.properties`:

```properties
user.url=localhost:8080/usuario
task-scheduler.url=localhost:8081/tarefas
notification.url=localhost:8082/email

server.port=8083

cron.horario=0 0/5 * * * ?

usuario.email=admin@admin.com
usuario.senha=1234
```

> `usuario.email` / `usuario.senha` autenticam a rotina agendada junto ao serviço `user` — substitua por uma credencial de serviço dedicada e mantenha fora do controle de versão em produção.

## 🐳 Docker — sobe o ecossistema completo

Este repositório concentra o `docker-composer.yml` que orquestra **todos os 4 microsserviços + os 2 bancos** de uma vez: `user`, `task-scheduler`, `notification`, `bff-scheduler`, PostgreSQL e MongoDB.

### 1. Estrutura de pastas esperada

O compose referencia os outros serviços por caminho relativo (`../user`, `../task-scheduler`, `../notification`), então os 4 repositórios precisam estar clonados lado a lado:

```
projetos/
├── user/
├── task-scheduler/
├── notification/
└── bff-task-scheduler/   ← rodar os comandos a partir daqui
```

### 2. Build do jar antes do Docker

Diferente dos outros 3 serviços (que fazem build multi-stage com Gradle dentro do próprio `Dockerfile`), o `Dockerfile` deste BFF espera um `.jar` já compilado em `target/`:

```bash
./mvnw clean package -DskipTests
```

### 3. Subir tudo

```bash
docker compose -f docker-composer.yml up --build
```

| Serviço | Porta host | Descrição |
|---|---|---|
| `bff-scheduler` | `8083` | API Gateway (ponto único de entrada) |
| `user` | `8080` | Usuários/autenticação |
| `task-scheduler` | `8081` | Tarefas (MongoDB) |
| `notification` | `8084`* | E-mail |
| `postgres` | `5433` | Banco do `user` |
| `mongodb` | `27017` | Banco do `task-scheduler` |

> ⚠️ **Pontos de atenção identificados no compose atual** (bons ajustes para uma próxima revisão):
> - `*` O serviço `notification` é mapeado para `8084:8084`, mas sua aplicação escuta em `8082` (`server.port` no `application.yaml`) — alinhar a porta evita falha de conexão entre os containers.
> - A variável `USER_URL` está escrita como `USER_URL:user:8080/usuario` (usando `:` em vez de `=`) — o formato correto de env var no Compose é `USER_URL=http://user:8080/usuario`.
> - Os caminhos de build (`..\\user`, `..\\task-scheduler`, `..\\notification`) usam barra invertida (padrão Windows); em Linux/macOS, trocar por `../user`, `../task-scheduler`, `../notification`.

## Como rodar localmente (sem Docker)

Requer os três microsserviços (`user`, `task-scheduler`, `notification`) em execução.

```bash
./mvnw spring-boot:run
```

Aplicação sobe em `http://localhost:8083`.

## Documentação da API

```
http://localhost:8083/swagger-ui.html
```

## Arquitetura

```
Cliente → bff-task-scheduler (8083)
              ├── user (8080)           — usuários, autenticação, endereço/telefone
              ├── task-scheduler (8081) — CRUD de tarefas
              └── notification (8082)   — envio de e-mail
```
