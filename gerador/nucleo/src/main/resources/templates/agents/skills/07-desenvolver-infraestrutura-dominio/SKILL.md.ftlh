---
name: desenvolver-infraestrutura-dominio
description: Desenvolver camada de infraestrutura para a camada de domínio
---
Desenvolver a camada de infraestrutura para a camada de domínio consiste em implementar os repositórios e demais SPIs utilizados por uma funcionalidade além de testes automatizados com @DataJpaTest para os repositórios.

As implementações fazem uso de [programação defensiva](../programar-defensivamente/SKILL.md), exceto as entidades JPA, e são adaptadores para repositórios Spring Boot.

O nome da implementação de um repositório de um agregado **A** é **A**Repositorio.

As entidades JPA seguem o modelo do projeto de banco de dados docs/**S**/projeto.dbml: os nomes das tabelas e colunas do modelo são declarados em @Table e @Column.