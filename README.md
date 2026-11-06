# BFF Task Scheduler

Serviço **Backend for Frontend (BFF)** da plataforma de agendamento de tarefas. Atua como ponto de entrada único para o(s) cliente(s) (web/mobile), agregando e orquestrando chamadas aos microsserviços internos (`user`, `task-scheduler`, `notification`).

## Responsabilidades

- Expor uma API única e simplificada para os clientes front-end
- Orquestrar chamadas aos microsserviços internos via clientes HTTP declarativos (Feign)
- Agregar e adaptar respostas de múltiplos serviços em um formato otimizado para o consumo pelo front-end
- Reduzir o acoplamento entre o front-end e a topologia interna de microsserviços

## Stack técnica

| Categoria | Tecnologia |
|---|---|
| Linguagem | Java 17 |
| Framework | Spring Boot 3.5.5 |
| Comunicação entre serviços | Spring Cloud OpenFeign |
| Build | Maven |
| Testes | JUnit 5 (Spring Boot Starter Test) |
| CI/CD | GitHub Actions |
| Boilerplate | Lombok |
| Gerenciamento de versões | Spring Cloud BOM (`spring-cloud-dependencies`) |

## Arquitetura

O BFF não possui persistência própria — ele é uma camada de orquestração que consome os demais microsserviços via **Feign Clients**, centralizando regras de composição de dados que seriam custosas de implementar diretamente no front-end (múltiplas chamadas, agregação, tratamento de falhas parciais).

```
Cliente (Web/Mobile)
        │
        ▼
  bff-task-scheduler
   ├──▶ user (autenticação/dados de usuário)
   ├──▶ task-scheduler (tarefas/agendamentos)
   └──▶ notification (disparo de notificações)
```

## Como executar localmente

### Pré-requisitos
- Java 17 (JDK)
- Maven (o wrapper `./mvnw` já está incluso no projeto)
- Instâncias em execução dos serviços `user`, `task-scheduler` e `notification`

### Passos

```bash
# Clonar o repositório
git clone https://github.com/gilbertneto/bff-task-scheduler.git
cd bff-task-scheduler

# Build
./mvnw clean install

# Executar
./mvnw spring-boot:run
```

### Configuração

Defina as URLs dos serviços consumidos via Feign (por exemplo, em `application.properties`/`application.yml`):

```
USER_SERVICE_URL=http://localhost:8080
TASK_SCHEDULER_SERVICE_URL=http://localhost:8081
NOTIFICATION_SERVICE_URL=http://localhost:8082
```

## Testes

A implementar...

## CI/CD

O repositório possui pipeline configurada em `.github/workflows`, executando build e testes automaticamente a cada push/pull request.

## Roadmap / melhorias futuras

- [x] Documentar endpoints agregados via OpenAPI/Swagger
- [ ] Adicionar tratamento de resiliência (circuit breaker/retry) nas chamadas Feign, ex.: Resilience4j
- [ ] Adicionar Dockerfile e instruções de execução via container
- [ ] Adicionar testes de integração cobrindo cenários de falha parcial dos serviços consumidos

## Ecossistema Relacionado

- https://github.com/gilbertneto/user
- https://github.com/gilbertneto/notification	
- https://github.com/gilbertneto/task-scheduler
