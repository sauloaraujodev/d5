---
name: desenvolver-infraestrutura-dominio-d5
description: Desenvolver camada de infraestrutura para a camada de domínio com D5
---
Aplique [desenvolver-infraestrutura-dominio](../07-desenvolver-infraestrutura-dominio/SKILL.md).

Não decompile o D5. Seu código fonte está em [.agents/d5/framework](../../d5/framework). O código do exemplo está em [.agents/d5/exemplo](../../d5/exemplo).

Implementações de repositórios herdam de [AgregadoRepositorioAbstrato](../../d5/framework/infraestrutura/src/main/java/dev/sauloaraujo/d5/framework/infraestrutura/AgregadoRepositorioAbstrato.java) (ver [MunicipioRepositorio](../../d5/exemplo/infraestrutura/src/main/java/com/example/exemplo/infraestrutura/geografia/municipio/MunicipioRepositorio.java)).