# 📘 README — Sistema Financeiro Minimalista

## 🏷️ Nome do Projeto

**Financial Microservices System** — Distributed Architecture

*Uma arquitetura financeira minimalista com evolução planejada para CQRS, Event Sourcing, mensageria e Sagas.*

## 👤 Integrante

* Leonardo da Conceição Muniz
* **Turma:** 2ª/6ª feira
* **Trabalho:** Individual

## 🧭 Descrição do Projeto

Este repositório contém a primeira etapa (TP1) de um *Sistema Financeiro Minimalista* desenvolvido com arquitetura de microsserviços utilizando *Spring Boot* e *Spring Cloud*.

O objetivo atual é implementar uma base funcional contendo:

* Criação de contas
* Registro de transações
* Comunicação resiliente entre serviços
* Descoberta dinâmica via Eureka
* API Gateway como ponto único de entrada
* Bancos separados por serviço (PostgreSQL e Cassandra)

Nos próximos TPs, o sistema evoluirá para padrões avançados de arquitetura distribuída, mantendo compatibilidade com práticas modernas de sistemas financeiros.

## 🧰 Tecnologias Utilizadas

* **Java 25**
* **Spring Boot 4**
* **Spring Cloud**
* **Spring Cloud Netflix Eureka**
* **Spring Cloud Gateway**
* **PostgreSQL 18**
* **Cassandra 4**
* **Resilience4j**
* **Docker & Docker Compose**

## 🧩 Arquitetura Atual (TP1)

### 1. Discovery Server (Eureka)

Gerencia o registro e descoberta dos microsserviços. Permite comunicação baseada em nome lógico, *reduzindo acoplamento*.

### 2. API Gateway (Spring Cloud Gateway)

Roteia requisições externas para os microsserviços corretos. *Centraliza* o acesso e *simplifica* a exposição da API.

### 3. account-service

Gerencia contas e saldo.

**Banco:** PostgreSQL

**Justificativa:** operações financeiras exigem *consistência forte* (ACID) e *controle de concorrência*.
O PostgreSQL oferece mecanismos como *row-level locking* (`SELECT FOR UPDATE`) para evitar race conditions.

### 4. transaction-service

Registra transações como eventos imutáveis.

**Banco:** Cassandra  

**Justificativa:** otimizado para *escrita massiva*, histórico *append-only* e consultas por *chave de partição* (accountId).
Ideal para *rastreabilidade* e *alta performance* em sistemas financeiros.

## 🗃️ Bancos de Dados

| Serviço | Banco | Tipo | Justificativa |
| --- | --- | --- | --- |
| account-service | PostgreSQL | Relacional | Consistência forte e controle de concorrência |
| transaction-service | Cassandra | Não relacional | Alta taxa de escrita, histórico imutável, consultas por chave |

## 🔄 Resiliência entre Microsserviços

### Comunicação:

**transaction-service** **→** **account-service**

### Risco:

account-service *indisponível* ou *lento*.

### Estratégia:

* **Timeout**
* **Retry**
* **Circuit Breaker**
* **Fallback:** transação registrada com status **PENDING**

### Comportamento:

Nenhuma transação é perdida.
O fallback preserva *rastreabilidade* mesmo em falhas temporárias.

## 🔌 Rotas do API Gateway

| Rota externa | Serviço destino |
| --- | --- |
| ``/api/accounts/**`` | account-service |
| ``/api/transactions/**`` | transaction-service |

## 🚀 Como Executar o Projeto (com Docker Compose)

Este projeto utiliza **Docker** e **Docker Compose** para facilitar a execução dos serviços e dos bancos de dados, garantindo um ambiente *padronizado* e *reprodutível*.

### 📦 1. Subir infraestrutura (PostgreSQL + Cassandra)

    docker-compose up -d

Isso iniciará:

* **PostgreSQL** (para o account-service)
* **Cassandra** (para o transaction-service)

Com volumes persistentes e portas expostas para desenvolvimento local.

### 🧩 2. Subir o Discovery Server

    cd discovery-server
    mvn spring-boot:run

### 🚪 3. Subir o API Gateway

    cd api-gateway
    mvn spring-boot:run

### 💳 4. Subir o account-service

    cd account-service
    mvn spring-boot:run

### 🔄 5. Subir o transaction-service

    cd transaction-service
    mvn spring-boot:run

### 🔍 6. Verificar serviços registrados no Eureka

Acesse:

    http://localhost:8761

Você deverá visualizar:

* `account-service`
* `transaction-service`
* `api-gateway`

## 📡 Exemplos de Requisições

### 🧾 Criar conta

    POST /api/accounts
    Content-Type: application/json

    {
        "ownerName": "Leonardo",
        "initialBalance": 1000
    }

#### ✔ Resposta esperada

    201 Created
    {
        "id": 1,
        "ownerName": "Leonardo",
        "balance": 1000
    }

### 💰 Consultar conta

    GET /api/accounts/1

#### ✔ Resposta esperada

    200 OK
    {
        "id": 1,
        "ownerName": "Leonardo",
        "balance": 1000
    }

### ➕ Depositar valor

    POST /api/accounts/1/operations
    Content-Type: application/json

    {
        "type": "DEPOSIT",
        "amount": 500
    }

#### ✔ Resposta esperada

    200 OK
    {
        "id": 1,
        "balance": 1500
    }

### ➖ Sacar valor

    POST /api/accounts/1/operations
    Content-Type: application/json

    {
        "type": "WITHDRAW",
        "amount": 200
    }

#### ✔ Resposta esperada

    200 OK
    {
        "id": 1,
        "balance": 1300
    }

### 🔄 Criar transação

#### *(registrada como evento imutável no Cassandra)*

    POST /api/transactions
    Content-Type: application/json

    {
        "accountId": 1,
        "amount": 250,
        "type": "DEBIT"
    }

#### ✔ Resposta esperada (conta disponível)

    201 Created
    {
        "id": "b1f8e3a0-9c2f-4c1e-9f3d-1a2b3c4d5e6f",
        "accountId": 1,
        "amount": 250,
        "type": "DEBIT",
        "status": "CONFIRMED"
    }

 ⚠️ Criar transação com fallback (account-service indisponível)

#### ✔ Resposta esperada (fallback Resilience4j)

    201 Created
    {
        "id": "c7d1a9b2-4f3e-8a1c-9d2e-7f6a5b4c3d2e",
        "accountId": 1,
        "amount": 250,
        "type": "DEBIT",
        "status": "PENDING"
    }

### 📜 Consultar extrato

    GET /api/transactions/account/1

#### ✔ Resposta esperada

    200 OK
    [
        {
            "id": "b1f8e3a0-9c2f-4c1e-9f3d-1a2b3c4d5e6f",
            "amount": 250,
            "type": "DEBIT",
            "status": "CONFIRMED"
        },
        {
            "id": "c7d1a9b2-4f3e-8a1c-9d2e-7f6a5b4c3d2e",
            "amount": 250,
            "type": "DEBIT",
            "status": "PENDING"
        }
    ]

## 🛣️ Roadmap Técnico — Evolução para Próximos TPs

Esta seção deixa claro o que já está implementado no TP1 e o que será adicionado futuramente, conforme o projeto evolui.

### ✔ Implementado no TP1

* Arquitetura de microsserviços
* Eureka Discovery Server
* API Gateway
* account-service (PostgreSQL)
* transaction-service (Cassandra)
* Comunicação resiliente com Resilience4j
* Bancos separados por serviço
* Rotas configuradas
* Primeira versão funcional da arquitetura

### 🔜 Planejado para TP2 e TP3

#### *(não implementado ainda — apenas planejado)*

#### 📌 CQRS (Command Query Responsibility Segregation)

Separação entre modelos de escrita e leitura para aumentar escalabilidade e performance.

#### 📌 Event Sourcing

Transações como eventos imutáveis que reconstroem o estado da conta.

#### 📌 Mensageria (Kafka ou RabbitMQ)

Comunicação assíncrona entre serviços.
Processamento de eventos financeiros em pipelines distribuídos.

#### 📌 Sagas / Transações Distribuídas

Coordenação de operações financeiras entre múltiplos serviços com consistência eventual.

#### 📌 Auditoria e Rastreamento Distribuído

Logs estruturados, correlação de requisições e trilhas de auditoria completas.

#### 📌 Segurança e Criptografia

Proteção de dados sensíveis, hashing, criptografia de payloads e tokens seguros.

#### 📌 Banco Não Relacional para Eventos

Cassandra ou DynamoDB como event store distribuído.

## 🖼️ Evidências (Prints para o TP1)

[ ] Tela do Eureka com serviços registrados

[ ] Chamadas passando pelo Gateway

[ ] Logs de Retry / Circuit Breaker / Fallback

[ ] Bancos separados funcionando

[ ] Simulação de falha do account-service

[ ] Startup dos quatro módulos
