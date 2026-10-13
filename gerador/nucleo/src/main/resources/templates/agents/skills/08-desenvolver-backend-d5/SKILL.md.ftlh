---
name: desenvolver-backend-d5
description: Desenvolver backend com D5
---
Aplique [desenvolver-backend](../08-desenvolver-backend/SKILL.md).

Não decompile o D5. Seu código fonte está em [.agents/d5/framework](../../d5/framework). O código do exemplo está em [.agents/d5/exemplo](../../d5/exemplo).

Projeções implementam [IProjecao](../../d5/framework/aplicacao/src/main/java/dev/sauloaraujo/d5/framework/aplicacao/IProjecao.java) (ver [MunicipioProjecao](../../d5/exemplo/aplicacao/src/main/java/com/example/exemplo/aplicacao/geografia/municipio/MunicipioProjecao.java)).

Ordenações são enumerações que implementam [IOrdenacao](../../d5/framework/aplicacao/src/main/java/dev/sauloaraujo/d5/framework/aplicacao/IOrdenacao.java) (ver [MunicipioOrdenacao](../../d5/exemplo/aplicacao/src/main/java/com/example/exemplo/aplicacao/geografia/municipio/MunicipioOrdenacao.java)).

Consultas herdam de [Consulta](../../d5/framework/aplicacao/src/main/java/dev/sauloaraujo/d5/framework/aplicacao/impl/Consulta.java) (ver [MunicipioConsulta](../../d5/exemplo/aplicacao/src/main/java/com/example/exemplo/aplicacao/geografia/municipio/MunicipioConsulta.java)).

Interfaces de repositórios de aplicação estendem [IAgregadoAplicacaoRepositorio](../../d5/framework/aplicacao/src/main/java/dev/sauloaraujo/d5/framework/aplicacao/spi/IAgregadoAplicacaoRepositorio.java) (ver [IMunicipioAplicacaoRepositorio](../../d5/exemplo/aplicacao/src/main/java/com/example/exemplo/aplicacao/geografia/municipio/spi/IMunicipioAplicacaoRepositorio.java)).

Implementações de interfaces de repositórios de aplicação herdam de [AgregadoAplicacaoRepositorioAbstrato](../../d5/framework/infraestrutura/src/main/java/dev/sauloaraujo/d5/framework/infraestrutura/AgregadoAplicacaoRepositorioAbstrato.java) (ver [MunicipioAplicacaoRepositorio](../../d5/exemplo/infraestrutura/src/main/java/com/example/exemplo/infraestrutura/geografia/municipio/MunicipioAplicacaoRepositorio.java)).

Serviços de aplicação herdam de [AgregadoAplicacaoServicoAbstrato](../../d5/framework/aplicacao/src/main/java/dev/sauloaraujo/d5/framework/aplicacao/impl/AgregadoAplicacaoServicoAbstrato.java) (ver [MunicipioAplicacaoServico](../../d5/exemplo/aplicacao/src/main/java/com/example/exemplo/aplicacao/geografia/municipio/MunicipioAplicacaoServico.java)).

Controladores herdam de [AgregadoControladorAbstrato](../../d5/framework/ui-backend/src/main/java/dev/sauloaraujo/d5/framework/ui/AgregadoControladorAbstrato.java), que já fornece as operações padrão de inclusão, alteração, exclusão e pesquisa. Ações adicionais são novos métodos anotados com `@GetMapping`/`@PostMapping` (ver [MunicipioControlador](../../d5/exemplo/ui-backend/src/main/java/com/example/exemplo/ui/backend/geografia/municipio/MunicipioControlador.java), que acrescenta a ação extinguir).

As autorizações de um agregado são criadas pelo método criarAutorizacoes de seu serviço e respondidas pelo controlador, junto com o agregado, nas operações de alteração e exclusão (ver [AgregadoRecurso](../../d5/framework/ui-backend/src/main/java/dev/sauloaraujo/d5/framework/ui/AgregadoRecurso.java)). Uma ação adicional não exige mudança no controlador para ser respondida: basta que sua autorização seja um atributo da subclasse de AgregadoAutorizacoes do agregado (ver [MunicipioAutorizacoes](../../d5/exemplo/dominio-geografia/src/main/java/com/example/exemplo/dominio/geografia/municipio/MunicipioAutorizacoes.java)). O [AutenticacaoControlador](../../d5/exemplo/ui-backend/src/main/java/com/example/exemplo/ui/backend/seguranca/AutenticacaoControlador.java) responde apenas o usuário autenticado e não é alterado.