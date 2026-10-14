# Tutorial
Neste tutorial, você vai desenvolver, passo a passo, uma aplicação com o framework.

## Subdomínios
No exemplo deste tutorial, o [domínio](../referencia/ddd.md#dominio) foi decomposto em dois [subdomínios](../referencia/ddd.md#subdominio):

- **Geografia**, que contém os conceitos de **unidade federativa** e **município**;
- **CRM (Customer Relationship Management)**, que contém o conceito de **cliente**.

## Lições
O tutorial é dividido em três lições, cada uma dedicada a um [agregado](../referencia/ddd.md#agregado). Cada lição parte do projeto deixado pela anterior e explica apenas o que ela acrescenta:

| Lição | O que acrescenta | Projeto ao final da lição |
| --- | --- | --- |
| [1. Unidade federativa](1-unidade-federativa.md) | o projeto Maven e o primeiro agregado, implementado camada por camada: domínio, testes, aplicação, infraestrutura e API REST | `exemplo1` |
| [2. Município](2-municipio.md) | um agregado que referencia outro, com uma associação entre as entidades JPA, uma ação além das quatro operações básicas e a proteção contra a exclusão de um agregado referenciado | `exemplo2` |
| [3. Cliente](3-cliente.md) | um novo subdomínio, que depende de outro, e um agregado com identificador gerado pelo banco de dados e um objeto de valor formado por vários atributos | `exemplo` |

Os diretórios `exemplo1`, `exemplo2` e `exemplo` do repositório de D5 contêm o projeto completo ao final de cada lição. Todos usam os mesmos nomes de módulos e pacotes — em cada lição, você continua evoluindo o mesmo projeto, criado no diretório `exemplo`.
