# d5-framework-aplicacao
Possui interfaces e classes para o lado de consulta de um [agregado](../ddd.md#agregado):

- [projeção](#projecao);
- [ordenação](#ordenacao);
- [consulta](#consulta);
- [resultado da consulta](#resultado-da-consulta);
- [serviço de aplicação](#servico-de-aplicacao);
- [repositório de aplicação](#repositorio-de-aplicacao).

## Projeção

### `IProjecao`
Representa uma projeção somente leitura de um [agregado](../ddd.md#agregado) `A`, normalmente com um subconjunto de seus atributos. Não acrescenta métodos: apenas documenta, pelo parâmetro de tipo, qual agregado a projeção representa.

```java title="IProjecao.java"
--8<-- "framework/aplicacao/src/main/java/dev/sauloaraujo/d5/framework/aplicacao/IProjecao.java"
```

No tutorial, [`UnidadeFederativaProjecao`](../../tutorial.md#projecao) implementa `IProjecao<UnidadeFederativa>`.

## Ordenação

### `IOrdenacao`
Marca um `enum` como uma das propriedades pelas quais uma [projeção](#iprojecao) `P` pode ser ordenada. Não acrescenta métodos.

```java title="IOrdenacao.java"
--8<-- "framework/aplicacao/src/main/java/dev/sauloaraujo/d5/framework/aplicacao/IOrdenacao.java"
```

No tutorial, [`UnidadeFederativaOrdenacao`](../../tutorial.md#ordenacao) implementa `IOrdenacao<UnidadeFederativaProjecao>`.

## Consulta

### `IConsulta`
Representa os parâmetros de uma consulta sobre uma [projeção](#iprojecao): a [ordenação](#iordenacao) `O` (opcional), se ela é descendente, o índice da página e o tamanho da página.

```java title="IConsulta.java"
--8<-- "framework/aplicacao/src/main/java/dev/sauloaraujo/d5/framework/aplicacao/IConsulta.java"
```

### `Consulta` {#consulta-classe}
Implementação base de `IConsulta`. O construtor recebe a ordenação, o indicador de ordem descendente, o índice e o tamanho da página, todos `Optional`. Valida que o índice da página, quando informado, não é negativo, e que o tamanho da página, quando informado, está entre 1 e `TAMANHO_PAGINA_MAXIMO` (1000), lançando `IllegalArgumentException` caso contrário; na ausência de ordenação, descendente, índice e tamanho de página, assume, respectivamente, nenhuma ordenação, `false`, `0` e `TAMANHO_PAGINA_PADRAO` (10).

No backend, a consulta é instanciada pelo Spring MVC a partir dos parâmetros da requisição, através do construtor da subclasse: o nome de cada parâmetro do construtor é o nome do parâmetro de requisição correspondente. Isso depende de a classe ser compilada com `-parameters`, o que o `spring-boot-starter-parent`, herdado através de `d5-pai`, já configura. Veja [`AgregadoControladorAbstrato`](ui-backend.md#agregadocontroladorabstrato).

```java title="Consulta.java"
--8<-- "framework/aplicacao/src/main/java/dev/sauloaraujo/d5/framework/aplicacao/impl/Consulta.java"
```

No tutorial, [`UnidadeFederativaConsulta`](../../tutorial.md#consulta) estende `Consulta<UnidadeFederativaOrdenacao>`, acrescentando o atributo `nome` para filtrar a pesquisa.

## Resultado da consulta

### `IConsultaResultado`
Representa o resultado de uma consulta: a página de [projeções](#iprojecao) `P` e o total de projeções que atendem à consulta, desconsiderando a paginação.

```java title="IConsultaResultado.java"
--8<-- "framework/aplicacao/src/main/java/dev/sauloaraujo/d5/framework/aplicacao/IConsultaResultado.java"
```

### `ConsultaResultado`
Implementação de `IConsultaResultado` que recebe as projeções e o total diretamente pelo construtor, validando que o total não é negativo. Guarda uma cópia imutável da lista de projeções (`List.copyOf`), que não admite elementos `null`.

```java title="ConsultaResultado.java"
--8<-- "framework/aplicacao/src/main/java/dev/sauloaraujo/d5/framework/aplicacao/impl/ConsultaResultado.java"
```

## Serviço de aplicação

### `IAgregadoAplicacaoServico`
Expõe a operação de pesquisa sobre um [agregado](../ddd.md#agregado): `pesquisar`, que recebe uma [consulta](#iconsulta) `C` e devolve um [resultado](#iconsultaresultado) de [projeções](#iprojecao) `P`.

```java title="IAgregadoAplicacaoServico.java"
--8<-- "framework/aplicacao/src/main/java/dev/sauloaraujo/d5/framework/aplicacao/IAgregadoAplicacaoServico.java"
```

Implementações são registradas automaticamente como beans — veja [Registro de beans](comportamentos.md#registro-de-beans).

### `AgregadoAplicacaoServicoAbstrato`
Implementação base de `IAgregadoAplicacaoServico`. É parametrizada pelo identificador `I`, pelo agregado `A` e pelas autorizações `U` do [serviço](dominio.md#iagregadoservico) correspondente, e pela projeção `P` e pela consulta `C` da pesquisa. Recebe no construtor o [repositório de aplicação](#iagregadoaplicacaorepositorio) do agregado e o [`IAgregadoServico`](dominio.md#iagregadoservico) correspondente, usado apenas para validar, em `pesquisar`, se o usuário está autorizado a buscar o agregado — através de `getBuscar` das autorizações devolvidas por `criarAutorizacoes` com um identificador vazio — e lançar [`NaoAutorizadoExcecao`](dominio.md#naoautorizadoexcecao) caso contrário. A consulta em si é delegada ao repositório de aplicação.

```java title="AgregadoAplicacaoServicoAbstrato.java"
--8<-- "framework/aplicacao/src/main/java/dev/sauloaraujo/d5/framework/aplicacao/impl/AgregadoAplicacaoServicoAbstrato.java"
```

No tutorial, [`UnidadeFederativaAplicacaoServico`](../../tutorial.md#servico-de-aplicacao) estende `AgregadoAplicacaoServicoAbstrato`.

## Repositório de aplicação

### `IAgregadoAplicacaoRepositorio`
Dá acesso, através de `pesquisar`, às [projeções](#iprojecao) `P` de um agregado persistidas, filtradas, ordenadas e paginadas conforme uma [consulta](#iconsulta) `C`.

```java title="IAgregadoAplicacaoRepositorio.java"
--8<-- "framework/aplicacao/src/main/java/dev/sauloaraujo/d5/framework/aplicacao/spi/IAgregadoAplicacaoRepositorio.java"
```

Implementações são registradas automaticamente como beans — veja [Registro de beans](comportamentos.md#registro-de-beans).

No tutorial, [`IUnidadeFederativaAplicacaoRepositorio`](../../tutorial.md#repositorio-de-aplicacao) estende essa interface. Sua implementação com Spring Data JPA estende [`AgregadoAplicacaoRepositorioAbstrato`](infraestrutura.md#agregadoaplicacaorepositorioabstrato), do módulo `d5-framework-infraestrutura`.
