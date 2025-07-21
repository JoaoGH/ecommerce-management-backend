# E-commerce Management Backend

Sistema de gerenciamento de produtos e pedidos para um e-commerce.

## Sobre

O objetivo desse projeto é um sistema backend completo com autenticação JWT, gerenciamento de produtos e pedidos, 
controle de estoque e consultas otimizadas em banco de dados.


## Técnologias e Ferramentas

- Java 21
- Spring Boot 3.5.3
- MySql LTS
- Maven
- Gradle
- Docker
- JWT
- JUnit

## Funcionalidades

### Autenticação JWT
- **Usuários com perfil `ADMIN`** podem:
    - Criar, editar e deletar produtos.
    - Criar, editar, visualizar e deletar usuários.
    - Criar, editar, visualizar e deletar ROLES de acesso (Dentro das permitidas ADMIN e USER).
    - Realizar consulta de relatórios.

- **Usuários com perfil `USER`** podem:
    - Visualizar produtos.
    - Criar pedidos.
    - Realizar pagamentos.

### Produtos
CRUD completo contendo os dados basicos e dados para auditoria (data de criação e data de atualização).

### Pedidos
- Criação de pedidos com múltiplos produtos.
- Pagamento de pedidos.
- Atualização de estoque após o pagamento.
- Cancelamento automático de pedidos caso haja produto sem estoque no pedido.
- Valor total calculado dinamicamente.


### Relatórios

Consultas SQL otimizadas com os seguintes endpoints:
1. Top 5 usuários que mais compraram. **(/relatorio/top-compradores)**
2. Ticket médio por usuário. **(/relatorio/ticket-medio)**
3. Valor total faturado no mês atual. **(/relatorio/faturamento-mensal)**

## Como executar

### Pré-requisitos

- Java 21
- SpringBoot 3.5.3

## Variáveis de Ambiente
- **JWT_SECRET**: segredo jwt.
- **JWT_TTL**: tempo de vida em minutos de um token JWT.

### Passos

1. **Clone o repositório**
   ```bash
   git clone https://github.com/JoaoGH/ecommerce-management-backend
   cd ecommerce-management-backend
   ```

2. **Suba o banco de dados**

3. **Importe o dump do banco**
  - Use o arquivo [ecommerce_dump.sql](src/main/resources/db/ecommerce_dump.sql):
    ```bash
    docker exec -i seu_container_mysql mysql -u root -p suabase < ecommerce_dump.sql
    ```

4. **Execute a aplicação**
   ```bash
   ./gradlew bootRun
   ```

## Endpoints

| Recurso                                | Método | Endpoint                        | Acesso      |
|----------------------------------------|--------|---------------------------------|-------------|
| Autenticar                             | POST   | `/auth/login`                   | USER, ADMIN |
| Listar Produtos                        | GET    | `/produtos`                     | USER, ADMIN |
| Buscar Produto                         | GET    | `/produtos/{id}`                | USER, ADMIN |
| Criar Produto                          | POST   | `/produtos`                     | ADMIN       |
| Editar Produto                         | PUT    | `/produtos/{id}`                | ADMIN       |
| Deletar Produto                        | DELETE | `/produtos/{id}`                | ADMIN       |
| Listar Roles                           | GET    | `/role`                         | ADMIN       |
| Buscar Role                            | GET    | `/role/{id}`                    | ADMIN       |
| Criar Role                             | POST   | `/role`                         | ADMIN       |
| Editar Role                            | PUT    | `/role/{id}`                    | ADMIN       |
| Deletar Role                           | DELETE | `/role/{id}`                    | ADMIN       |
| Listar Usuarios                        | GET    | `/usuario`                      | ADMIN       |
| Buscar Usuario                         | GET    | `/usuario/{id}`                 | ADMIN       |
| Criar Usuario                          | POST   | `/usuario`                      | ADMIN       |
| Editar Usuario                         | PUT    | `/usuario/{id}`                 | ADMIN       |
| Deletar Usuario                        | DELETE | `/usuario/{id}`                 | ADMIN       |
| Criar Pedido                           | POST   | `/pedido`                       | USER, ADMIN |
| Adicionar item no Pedido               | POST   | `/pedido`                       | USER, ADMIN |
| Pagar Pedido                           | PUT    | `/pedido/{id}`                  | USER, ADMIN |
| Cancelar Pedido                        | DELETE | `/pedido/{id}`                  | USER, ADMIN |
| Listar Pedidos                         | GET    | `/pedido`                       | USER, ADMIN |
| Relatório Top 5 compradores            | GET    | `/relatorio/top-compradores`    | ADMIN       |
| Relatório Ticket Médio Por Compradores | GET    | `/relatorio/ticket-medio`       | ADMIN       |
| Relatório Faturamento Mensal           | GET    | `/relatorio/faturamento-mensal` | ADMIN       |

---

## Usuários para Testes

```json
ADMIN:
  email: admin@email.com
  senha: senha

USER:
  email: user@email.com
  senha: senha
```


## Autor

João Gabriel Hartmann  
[LinkedIn](https://www.linkedin.com/in/joaoghartmann)

---
