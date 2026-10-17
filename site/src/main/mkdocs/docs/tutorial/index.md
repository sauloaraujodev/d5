# Introdução
Neste tutorial, você vai desenvolver, passo a passo, uma aplicação com o framework.

## Pré-requisitos
Para seguir o tutorial, você precisa:

- do [Java 25](https://adoptium.net/temurin/releases/?version=25){target="_blank"};
- do [Maven](https://maven.apache.org/install.html){target="_blank"};
- de acesso à internet, para que o Maven baixe o framework do [Maven Central](../referencia/framework/index.md#modulos-principais).

O tutorial pressupõe que você sabe programar em Java e usar o Maven. Os conceitos de [DDD](../referencia/ddd.md), [arquitetura limpa](../referencia/arquitetura-limpa.md), [CQRS](../referencia/cqrs.md) e [programação defensiva](../referencia/programacao.md) e o framework são explicados nas páginas de referência, para as quais as lições apontam. Já as bibliotecas sobre as quais o framework é construído não são ensinadas aqui; as lições explicam apenas os detalhes delas que o código usa de forma pouco evidente. Se alguma delas for nova para você, consulte sua documentação:

- [Spring Boot](https://docs.spring.io/spring-boot/index.html){target="_blank"}, que configura e executa a aplicação, e o [Spring Framework](https://docs.spring.io/spring-framework/reference/core/beans.html){target="_blank"}, cujo contêiner cria os objetos da aplicação — os *beans* — e injeta suas dependências;
- [Jakarta Persistence (JPA)](https://jakarta.ee/learn/docs/jakartaee-tutorial/current/persist/persistence-intro/persistence-intro.html){target="_blank"}, que mapeia classes para tabelas, e a linguagem de consulta JPQL;
- [Spring Data JPA](https://docs.spring.io/spring-data/jpa/reference/){target="_blank"}, que implementa repositórios a partir de interfaces;
- [Spring Security](https://docs.spring.io/spring-security/reference/){target="_blank"}, que autentica os usuários e protege os endpoints;
- [Cucumber](https://cucumber.io/docs/guides/10-minute-tutorial/){target="_blank"} e a linguagem [Gherkin](https://cucumber.io/docs/gherkin/reference/){target="_blank"}, usados para especificar e testar o comportamento dos serviços;
- [JUnit](https://docs.junit.org/current/user-guide/){target="_blank"} e [Mockito](https://site.mockito.org){target="_blank"}, usados nos testes.

## Subdomínios
No exemplo deste tutorial, o [domínio](../referencia/ddd.md#dominio) foi decomposto em dois [subdomínios](../referencia/ddd.md#subdominio):

- **Geografia**, que contém os conceitos de **unidade federativa** e **município**;
- **CRM (Customer Relationship Management)**, que contém o conceito de **cliente**.

## Lições
O tutorial é dividido em três lições, cada uma dedicada a um [agregado](../referencia/ddd.md#agregado). Cada lição parte do projeto deixado pela anterior e explica apenas o que ela acrescenta:

| Lição | O que acrescenta | Projeto ao final da lição |
| --- | --- | --- |
| [1. Unidade federativa](1-unidade-federativa.md) | o projeto Maven, gerado pelo [gerador](../gerador.md), e o primeiro agregado, implementado camada por camada: domínio, testes, aplicação, infraestrutura e API REST | `exemplo1` |
| [2. Município](2-municipio.md) | um agregado que referencia outro, com uma associação entre as entidades JPA, uma ação além das quatro operações básicas e a proteção contra a exclusão de um agregado referenciado | `exemplo2` |
| [3. Cliente](3-cliente.md) | o subdomínio CRM, que passa a depender de outro, e um agregado com identificador gerado pelo banco de dados e um objeto de valor formado por vários atributos | `exemplo` |

Os diretórios `exemplo1`, `exemplo2` e `exemplo` do repositório de D5 contêm o projeto completo ao final de cada lição. Todos usam os mesmos nomes de módulos e pacotes — em cada lição, você continua evoluindo o mesmo projeto, criado no diretório `exemplo`.
