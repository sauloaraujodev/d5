---
name: projetar-repositorios
description: Projetar repositórios
---
Existe um repositório para cada agregado **A** chamado **A**Repository.

Repositórios devem ser declarados dentro da entidade raiz de seu agregado.

Repositórios declaram métodos através dos quais serviços acessam o mecanismo de persistência do sistema, por exemplo, um banco de dados relacional e/ou o sistema de arquivos, para incluir, alterar e excluir agregados. 

Adicionalmente, repositórios declaram métodos que verificam se existem agregados que satisfazem uma condição e que os serviços utilizam para implementar regras de negócio.

Por fim, repositórios declaram métodos de pesquisa.

Ver [validar-modelo](../03.08-validar-modelo/SKILL.md).