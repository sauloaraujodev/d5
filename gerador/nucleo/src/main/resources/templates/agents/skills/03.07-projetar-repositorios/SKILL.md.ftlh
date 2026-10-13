---
name: projetar-repositorios
description: Projetar repositórios
---
Existe um repositório para cada agregado **A** chamado **A**Repository.

Repositórios devem ser declarados dentro da entidade raiz de seu agregado.

Repositórios declaram métodos através dos quais serviços acessam o mecanismo de persistência do sistema, por exemplo, um banco de dados relacional e/ou o sistema de arquivos, para incluir, alterar e excluir agregados e para verificar a existência de um agregado e buscá-lo a partir de seu identificador: incluir, existePorIdentificador, buscarPorIdentificador, alterar e excluir (ver [exemplo](../03-projetar-software/references/REFERENCE.md)).

Adicionalmente, repositórios declaram métodos que verificam se existem agregados que satisfazem uma condição e que os serviços utilizam para implementar regras de negócio.

Repositórios não declaram métodos de pesquisa. Seguindo CQRS, pesquisas, que respondem projeções de agregados para exibição, são declaradas nos repositórios de aplicação, desenvolvidos junto com o backend.

Ver [validar-modelo](../03.08-validar-modelo/SKILL.md).