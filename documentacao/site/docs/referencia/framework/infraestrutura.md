# d5-framework-infraestrutura
Possui classes para implementar, com [Spring Data JPA](https://spring.io/projects/spring-data-jpa){target="_blank"}, o [repositório](../ddd.md#repositorio) e o [repositório de aplicação](aplicacao.md#repositorio-de-aplicacao) de um [agregado](../ddd.md#agregado):

- [entidade JPA](#entidade-jpa);
- [repositório](#repositorio);
- [repositório de aplicação](#repositorio-de-aplicacao);
- [segurança](#seguranca);
- [registro de beans](#registro-de-beans).

## Entidade JPA

### `IAgregadoJpa`
Marca uma classe como a entidade JPA correspondente a um [agregado](../ddd.md#agregado) `A`. Não acrescenta métodos: apenas documenta, pelo parâmetro de tipo, qual agregado a entidade representa.

```java title="IAgregadoJpa.java"
--8<-- "framework/infraestrutura/src/main/java/dev/sauloaraujo/d5/framework/infraestrutura/IAgregadoJpa.java"
```

No tutorial, [`UnidadeFederativaJpa`](../../tutorial.md#entidade-jpa) implementa `IAgregadoJpa<UnidadeFederativa>`.

## Repositório

### `AgregadoRepositorioAbstrato`
Implementação base de [`IAgregadoRepositorio`](dominio.md#iagregadorepositorio) sobre um [`JpaRepository`](https://docs.spring.io/spring-data/jpa/docs/current/api/org/springframework/data/jpa/repository/JpaRepository.html){target="_blank"} do Spring Data. É parametrizada pelo identificador `I1` do agregado, pelo agregado `A1`, pelo identificador `I2` e pela entidade `A2` — uma [`IAgregadoJpa<A1>`](#iagregadojpa) — usados pelo Spring Data. Recebe no construtor o `JpaRepository<A2, I2>` e o modelo da mensagem de erro para identificador inexistente — no formato de `String.format`, com um `%s` substituído pelo identificador, usado por `buscarPorIdentificador` ao lançar [`AgregadoInexistenteExcecao`](dominio.md#agregadoinexistenteexcecao) — e implementa `incluir`, `buscarPorIdentificador`, `alterar`, `excluir` e `existePorIdentificador` convertendo entre agregado e entidade através dos métodos abstratos `paraJpa`, `paraAgregado` e `paraIdentificadorJpa`, que a subclasse implementa.

```java title="AgregadoRepositorioAbstrato.java"
--8<-- "framework/infraestrutura/src/main/java/dev/sauloaraujo/d5/framework/infraestrutura/AgregadoRepositorioAbstrato.java"
```

No tutorial, [`UnidadeFederativaRepositorio`](../../tutorial.md#implementacao-do-repositorio) estende `AgregadoRepositorioAbstrato` e implementa `paraJpa`, `paraAgregado` e `paraIdentificadorJpa`.

## Repositório de aplicação

### `AgregadoAplicacaoRepositorioAbstrato`
Implementação base de [`IAgregadoAplicacaoRepositorio`](aplicacao.md#iagregadoaplicacaorepositorio) sobre [Spring Data JPA](https://spring.io/projects/spring-data-jpa){target="_blank"}. É parametrizada pela [projeção](aplicacao.md#iprojecao) `P`, pela [ordenação](aplicacao.md#iordenacao) `O` e pela [consulta](aplicacao.md#iconsulta) `C`. Implementa `pesquisar(C)` convertendo a consulta em um [`Pageable`](https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/domain/Pageable.html){target="_blank"} — cuja ordenação é resolvida pelo método abstrato `paraPropriedadeOrdenacao`, que a subclasse implementa — e delegando ao método abstrato `pesquisar(C, Pageable)`, que a subclasse também implementa consultando o Spring Data. Na ausência de ordenação na consulta, a página não é ordenada.

Também expõe `paraLike`, que converte um filtro textual opcional em um padrão para cláusulas `like` que encontra o filtro em qualquer posição: converte o filtro para minúsculas, escapa `%`, `_` e `\` com o caractere de escape `\` e o envolve com `%`; na ausência de filtro, devolve `null`. Para que o padrão funcione, a consulta JPQL que o recebe deve, portanto, comparar com a coluna em minúsculas, declarar o caractere de escape e ignorar o filtro quando ele for `null` — por exemplo, `:nome is null or lower(u.nome) like :nome escape '\'`.

```java title="AgregadoAplicacaoRepositorioAbstrato.java"
--8<-- "framework/infraestrutura/src/main/java/dev/sauloaraujo/d5/framework/infraestrutura/AgregadoAplicacaoRepositorioAbstrato.java"
```

No tutorial, [`UnidadeFederativaAplicacaoRepositorio`](../../tutorial.md#implementacao-do-repositorio-de-aplicacao) estende `AgregadoAplicacaoRepositorioAbstrato`, implementa `pesquisar(UnidadeFederativaConsulta, Pageable)` e `paraPropriedadeOrdenacao`, e usa `paraLike` para filtrar pelo nome.

## Segurança

### `SegurancaServico`
Implementação de [`ISegurancaServico`](dominio.md#isegurancaservico) sobre o Spring Security: lê o `Authentication` do `SecurityContextHolder`, lançando `NaoAutenticadoExcecao` quando não há usuário autenticado — inclusive quando a autenticação é anônima.

```java title="SegurancaServico.java"
--8<-- "framework/infraestrutura/src/main/java/dev/sauloaraujo/d5/framework/infraestrutura/SegurancaServico.java"
```

Diferente das implementações de agregado, que o [registro de beans](comportamentos.md#registro-de-beans) descobre por herança de interface, `SegurancaServico` é registrado diretamente como autoconfiguração do módulo — é a única implementação de `ISegurancaServico` esperada em uma aplicação D5.

## Registro de beans

### `AgregadoRegistrador`
Registra como beans, sem que precisem de anotações do Spring, as implementações de [`IAgregadoRepositorio`](dominio.md#iagregadorepositorio), [`IAgregadoAplicacaoRepositorio`](aplicacao.md#iagregadoaplicacaorepositorio), [`IAgregadoServico`](dominio.md#iagregadoservico) e [`IAgregadoAplicacaoServico`](aplicacao.md#iagregadoaplicacaoservico). É registrado como autoconfiguração do módulo. Veja [Registro de beans](comportamentos.md#registro-de-beans) para os detalhes e o código.
