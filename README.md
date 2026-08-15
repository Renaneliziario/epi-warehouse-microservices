# EPI Warehouse Microservices

Decomposição do monólito Memelândia (curso Backend Java Pro, EBAC, módulo 15) em
cinco microsserviços, usando almoxarifado de EPI (Equipamento de Proteção
Individual) como domínio adaptado.

## Arquitetura

- **servidor-config**: Spring Cloud Config, propriedades centralizadas (porta 8888).
- **servico-funcionario**: identidade dos colaboradores (porta 8081, H2).
- **servico-categoria-epi**: taxonomia de categorias de EPI (porta 8082, H2).
- **servico-retirada-epi**: catálogo de EPI e retiradas, consome os dois anteriores
  via REST síncrono (porta 8083, H2).
- **servico-operacao**: gestão de segurança industrial, setores, riscos e EPIs
  exigidos por operação de trabalho, consome o catálogo de EPI via REST síncrono
  (porta 8084, PostgreSQL + Flyway).

## Regras de negócio centrais

- Uma retirada de EPI não pode ser registrada sem um `Epi` existente no catálogo
  nem por um colaborador inexistente. A categoria é validada uma única vez, na
  criação do `Epi`, não a cada retirada.
- Um vínculo operação↔EPI não pode ser criado apontando pra um `epiId`
  inexistente no catálogo do `servico-retirada-epi`.

## Comunicação entre serviços

Todos os quatro serviços de domínio buscam sua configuração do `servidor-config`
no startup. Validação cross-service usa `RestClient` síncrono, padrão
`GET /recurso/{id}/exists` (204/404, sem corpo):

```
servico-operacao ──EpiClient──► servico-retirada-epi
servico-retirada-epi ──EmployeeClient──► servico-funcionario
servico-retirada-epi ──EpiCategoryClient──► servico-categoria-epi
```

## Tecnologias

Java 17, Spring Boot 3.3.4, Spring Cloud 2023.0.3, Spring Data JPA, H2, PostgreSQL,
Flyway, springdoc-openapi, Micrometer + Actuator (Prometheus), JUnit 5, Mockito,
Lombok, Docker Compose.

## Como rodar localmente

Via Maven (H2 dos 3 primeiros serviços não precisa de nada externo; o
`servico-operacao` precisa de um PostgreSQL local em `localhost:5432`,
banco `db_operacao`, usuário `postgres`):

```bash
mvn -pl servidor-config spring-boot:run &
mvn -pl servico-funcionario spring-boot:run &
mvn -pl servico-categoria-epi spring-boot:run &
mvn -pl servico-retirada-epi spring-boot:run &
mvn -pl servico-operacao spring-boot:run &
```

Ou via Docker (recomendado, sobe o Postgres do `servico-operacao` junto):

```bash
docker compose up --build
```

## Popular dados de exemplo

Setores, riscos e operações já vêm semeados pelo Flyway na subida do
`servico-operacao` (`V1__estrutura_inicial.sql`). Os EPIs e os vínculos
operação↔EPI não podem vir do Flyway (dependem de ids gerados por outro
serviço), populam-se via script:

```bash
./scripts/seed-operacao-epi.sh
```

Execute só depois que os 5 serviços estiverem `Up`/saudáveis.

## Endpoints principais

| Serviço | Endpoint | Descrição |
|---|---|---|
| servico-funcionario | `POST /employees` | Cadastra colaborador |
| servico-funcionario | `GET /employees/{id}/exists` | Existência (204/404) |
| servico-categoria-epi | `POST /epi-categories` | Cadastra categoria |
| servico-retirada-epi | `POST /epis` | Cadastra EPI no catálogo |
| servico-retirada-epi | `GET /epis/dia` | EPI do dia (bônus) |
| servico-retirada-epi | `GET /epis/{id}/exists` | Existência (204/404) |
| servico-retirada-epi | `POST /retiradas` | Registra retirada |
| servico-operacao | `POST /setores`, `POST /riscos` | Cadastro de setor/risco |
| servico-operacao | `POST /operacoes` | Cadastra operação de trabalho |
| servico-operacao | `POST /operacoes/{id}/epis` | Vincula EPI obrigatório à operação |
| servico-operacao | `GET /operacoes/{id}/epis` | Consulta EPIs exigidos pela operação (bônus) |
| servico-operacao | `POST /operacoes/{id}/riscos` | Vincula risco à operação |

## Documentação interativa

Swagger UI em `/swagger-ui.html` de cada serviço. Métricas Prometheus em
`/actuator/prometheus`.

## Testes

```bash
mvn -pl servico-funcionario,servico-categoria-epi,servico-retirada-epi test
```

`servico-operacao` ainda sem suíte de testes (pendência conhecida).

## Variáveis de ambiente

| Variável | Uso | Default |
|---|---|---|
| `DB_PASSWORD` | Senha dos bancos (H2 e Postgres) | `password` (dev) |
| `CONFIG_SERVER_URL` | URL do servidor de configuração | `http://localhost:8888` |
| `SERVICO_FUNCIONARIO_URL`, `SERVICO_CATEGORIA_EPI_URL`, `SERVICO_RETIRADA_EPI_URL` | URLs cross-service | `http://localhost:80xx` |
| `DB_HOST` | Host do Postgres do servico-operacao | `localhost` |
| `TZ` | Timezone dos containers | `America/Sao_Paulo` |

Nenhum valor default é segredo real, todos existem só como fallback de
desenvolvimento local.

## Decisões de arquitetura

- **Package-by-layer**, não por feature, mesma convenção do monólito original.
- **Tipos primitivos** onde o valor sempre existe pós-construção; wrappers só em
  `@Id` JPA e campos de Request DTO validados. **Sem `record`** nos DTOs
  (classes Lombok comuns), priorizando entendimento sobre modernidade.
- **RestClient síncrono** (não Feign, não WebClient) para validação cross-service,
  padrão `/exists` (204/404), mais barato que buscar o recurso inteiro.
- **H2 nos 3 serviços originais, PostgreSQL + Flyway no `servico-operacao`**: o
  primeiro serviço com schema versionado e seed real de domínio.
- **Catálogo de EPI nunca duplicado**: `servico-operacao` guarda só `epiId`,
  validado via REST, nunca replica a tabela `epi`.

## Histórico de commits (ordem de criação)

| Commit | Objetivo |
|---|---|
| `chore: mover monolito para monolith-original antes da divisao em microsservicos` | Ponto zero do projeto |
| `feat: adicionar pom.xml raiz e configuracao real do servidor-config (etapa 2, commit retroativo)` | Configuração centralizada |
| `chore: adicionar bootstrap dos tres servicos de dominio com cliente config` | Esqueleto dos 3 serviços originais |
| `feat: mapear entidades JPA e repositories dos tres dominios` | Persistência |
| `feat: implementar services, DTOs e validacao cross-service` | Regra de negócio |
| `feat: implementar controllers, exception handling e endpoint epi do dia` | API REST |
| `docs: adicionar configuracao OpenAPI do servico de retirada` | Documentação |
| `test: cobrir services e controllers com JUnit 5 e Mockito` | Testes |
| `chore: adicionar Dockerfiles e docker-compose com healthcheck, timezone e resolucao de rede entre containers` | Infraestrutura |
| `feat: criar servico-operacao com Postgres e Flyway para gestao de seguranca industrial` | 5º serviço, gestão de segurança |
| `docs: finalizar README, script de seed e checklist de entrega` | Entrega |

## Checklist final de entrega

- [x] Projeto compila (`mvn clean install` na raiz).
- [x] Testes passam nos 3 serviços originais (26/26).
- [ ] Testes do `servico-operacao` (pendência conhecida).
- [x] Os 5 serviços sobem, local ou via Docker Compose.
- [x] Retirada sem colaborador ou EPI válido retorna 422.
- [x] Vínculo operação-EPI sem EPI válido retorna 422.
- [x] Swagger UI acessível nos 4 serviços de domínio.
- [x] Métricas Prometheus expostas em `/actuator/prometheus`.
- [x] README completo e atualizado.
- [x] `.gitignore` correto, sem `target/` versionado.
- [x] Nenhum segredo ou credencial real no código ou no histórico.
- [x] Commits organizados, mensagens no padrão Conventional Commits em PT-BR.
- [x] Requisitos mínimos do enunciado atendidos: mínimo 2 domínios (temos 4),
  fatores essenciais de 12-Factor, logs úteis, métricas, validação cross-domain.

## Gaps conhecidos (adiados conscientemente)

- `EmployeeRepository.existsByEmail` não usado, email duplicado hoje retorna
  500 cru em vez de erro de validação tratado.
- Clients cross-service só tratam `HttpClientErrorException.NotFound`, não
  5xx genérico do serviço remoto.
- Sem load balancer/service discovery, fora do escopo do enunciado (cada
  serviço roda em 1 instância).
- `server.shutdown: graceful` não configurado em nenhum serviço.
