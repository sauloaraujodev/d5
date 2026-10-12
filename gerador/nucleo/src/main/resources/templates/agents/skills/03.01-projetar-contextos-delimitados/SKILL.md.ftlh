---
name: projetar-contextos-delimitados
description: Projetar contextos delimitados
---
Conceitualmente, existe um único contexto delimitado, pois a linguagem onipresente é válida em todo o software. Entretanto, como o Context Mapper não permite que um contexto delimitado seja definido em vários arquivos, [compartilhado/projeto.cml](../../../docs/compartilhado/projeto.cml) declara o domínio do software, seus subdomínios e o contexto delimitado compartilhado contendo o módulo compartilhado, que encapsula o projeto de software do módulo Maven dominio-compartilhado.

docs/**S**/projeto.cml importa [compartilhado/projeto.cml](../../../docs/compartilhado/projeto.cml), declara o `ContextMap` com a relação `**S** Downstream-Upstream compartilhado` (que autoriza **S** a referenciar tipos do compartilhado), declara o contexto delimitado **S** que implementa o subdomínio **S** e contém o módulo **S**, que encapsula o projeto de software de **S**.

[projeto.cml](../../../docs/projeto.cml) apenas importa os demais arquivos *.cml.

Ver [projetar-objetos-valor](../03.02-projetar-objetos-valor/SKILL.md).