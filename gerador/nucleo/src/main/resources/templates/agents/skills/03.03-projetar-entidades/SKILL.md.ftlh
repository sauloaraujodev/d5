---
name: projetar-entidades
description: Projetar entidades
---
Conceitos que possuem identidade são projetados como entidades.

Para evitar o antipadrão objetos anêmicos, regras de negócio que dependem apenas dos atributos de uma entidade e/ou de parâmetros de métodos são encapsuladas em métodos da entidade.

Entidades são declaradas no módulo do contexto delimitado que corresponde ao subdomínio **S** no qual elas foram identificadas.

Regras relacionadas a atributos e coleções presentes em [projetar-objetos-valor](../03.02-projetar-objetos-valor/SKILL.md) também se aplicam a entidades.

Ver [projetar-agregados](../03.04-projetar-agregados/SKILL.md).