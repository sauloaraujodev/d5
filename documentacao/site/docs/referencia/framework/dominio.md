# d5-framework-dominio
Possui interfaces e classes para:

- [objeto de valor](#objeto-de-valor);
- [entidade](#entidade);
- [agregado](#agregado);
- [repositório](#repositorio);
- [autorização](#autorizacao);
- [segurança](#seguranca);
- [serviço](#servico);
- [exceções](#excecoes).

## Objeto de valor

### `IObjetoValor`
Realiza o conceito de [objeto de valor](../ddd.md#objeto-de-valor). `IObjetoValor` redeclara `equals` e `hashCode` para documentar o contrato de que as implementações devem redefini-los, de modo que duas instâncias com os mesmos atributos sejam iguais. O compilador não impõe esse contrato, pois toda classe já herda `equals` e `hashCode` de `Object`; por isso, prefira herdar de [`ObjetoValorStringAbstrato`](#objetovalorstringabstrato), que já os implementa, sempre que o objeto de valor for representado por uma única `String`.

```java title="IObjetoValor.java"
--8<-- "framework/dominio/src/main/java/dev/sauloaraujo/d5/framework/dominio/IObjetoValor.java"
```

O parâmetro de `equals` é anotado com `@Nullable` porque sobrescreve `Object.equals`, cujo contrato admite `null`.

### `ObjetoValorStringAbstrato`
Classe base para objetos de valor representados por uma única `String` — um código, uma sigla, um nome. Guarda o valor em um atributo `final`, valida-o contra `null` no construtor e implementa `equals`, `hashCode` e `toString` a partir dele.

```java title="ObjetoValorStringAbstrato.java"
--8<-- "framework/dominio/src/main/java/dev/sauloaraujo/d5/framework/dominio/impl/ObjetoValorStringAbstrato.java"
```

Duas instâncias são iguais quando têm a mesma classe concreta (`getClass()`) e o mesmo valor. Portanto, objetos de valor de tipos diferentes com o mesmo valor nunca são iguais. `toString` devolve o valor sem alteração — usado pelo backend para serializar e converter essas instâncias como uma `String` pura; veja [Serialização de objetos de valor](comportamentos.md#serializacao-de-objetos-de-valor).

### `ObjetoValorRegexAbstrato`
Estende `ObjetoValorStringAbstrato` para valores cujo formato pode ser descrito por uma expressão regular. Além do valor, o construtor recebe o `formato` (a expressão regular) e a `mensagem` de erro (um modelo com um `%s` para o valor inválido), e valida com `Validate.matchesPattern`, que exige que o valor **inteiro** corresponda à expressão regular — não basta conter um trecho que corresponda. A validação de `null`, herdada de `ObjetoValorStringAbstrato`, ocorre antes da validação do formato.

```java title="ObjetoValorRegexAbstrato.java"
--8<-- "framework/dominio/src/main/java/dev/sauloaraujo/d5/framework/dominio/impl/ObjetoValorRegexAbstrato.java"
```

No tutorial, [`UnidadeFederativaSigla`](../../tutorial.md#sigla) e [`UnidadeFederativaCodigoIbge`](../../tutorial.md#codigo-ibge) herdam dessa classe.

## Entidade

### `IIdentificador`
Identificador de uma [entidade](../ddd.md#entidade). É um [`IObjetoValor`](#iobjetovalor) — portanto, comparado por valor — que adicionalmente redeclara `toString` para documentar que ele deve devolver a representação textual do identificador, usada, por exemplo, em URLs e mensagens de erro. Assim como em `IObjetoValor`, o compilador não impõe esse contrato; `ObjetoValorStringAbstrato` já o cumpre, devolvendo o próprio valor.

```java title="IIdentificador.java"
--8<-- "framework/dominio/src/main/java/dev/sauloaraujo/d5/framework/dominio/IIdentificador.java"
```

No tutorial, [`UnidadeFederativaCodigoIbge`](../../tutorial.md#codigo-ibge) implementa essa interface.

### `IEntidade`
Realiza o conceito de [entidade](../ddd.md#entidade). `IEntidade` é parametrizada pelo tipo `I` do identificador (um [`IIdentificador`](#iidentificador)) e expõe `getIdentificador`, que devolve um [`Optional`](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/util/Optional.html){target="_blank"} vazio enquanto a entidade ainda não tem identificador atribuído.

```java title="IEntidade.java"
--8<-- "framework/dominio/src/main/java/dev/sauloaraujo/d5/framework/dominio/IEntidade.java"
```

## Agregado

### `IAgregado`
Realiza o conceito de [agregado](../ddd.md#agregado): sua raiz. `IAgregado` estende [`IEntidade<I>`](#ientidade) e não acrescenta nada além disso — é um marcador da raiz do agregado, parametrizado pelo tipo do identificador.

```java title="IAgregado.java"
--8<-- "framework/dominio/src/main/java/dev/sauloaraujo/d5/framework/dominio/IAgregado.java"
```

No tutorial, [`UnidadeFederativa`](../../tutorial.md#agregado) implementa `IAgregado<UnidadeFederativaCodigoIbge>`.

## Repositório

### `IAgregadoRepositorio`
Realiza o conceito de [repositório](../ddd.md#repositorio). `IAgregadoRepositorio` é parametrizada pelo identificador `I` e pelo agregado `A`, e expõe `incluir`, `existePorIdentificador`, `buscarPorIdentificador`, `alterar` e `excluir`.

```java title="IAgregadoRepositorio.java"
--8<-- "framework/dominio/src/main/java/dev/sauloaraujo/d5/framework/dominio/spi/IAgregadoRepositorio.java"
```

Implementações são registradas automaticamente como beans — veja [Registro de beans](comportamentos.md#registro-de-beans).

## Autorização

### `IAgregadoAutorizacoes`
Representa se o usuário está autorizado a executar, sobre um [agregado](#agregado), cada uma das quatro operações de um [serviço](#servico): `getIncluir`, `getBuscar`, `getAlterar` e `getExcluir`. É parametrizada pelo agregado `A`.

```java title="IAgregadoAutorizacoes.java"
--8<-- "framework/dominio/src/main/java/dev/sauloaraujo/d5/framework/dominio/IAgregadoAutorizacoes.java"
```

### `AgregadoAutorizacoes`
Implementação de `IAgregadoAutorizacoes` que recebe os quatro valores booleanos diretamente pelo construtor.

```java title="AgregadoAutorizacoes.java"
--8<-- "framework/dominio/src/main/java/dev/sauloaraujo/d5/framework/dominio/impl/AgregadoAutorizacoes.java"
```

No tutorial, [`UnidadeFederativaServico`](../../tutorial.md#servico) devolve um `AgregadoAutorizacoes<UnidadeFederativa>` que libera a busca a qualquer usuário e as demais operações somente a administradores.

## Segurança

### `ISegurancaServico`
Dá acesso a informações do usuário autenticado: `getUsuario`, `getAutorizacoes` e o método `default` `possuiAutorizacao`, que verifica se uma autorização está entre as do usuário. Os três métodos lançam [`NaoAutenticadoExcecao`](#naoautenticadoexcecao) quando não há usuário autenticado.

```java title="ISegurancaServico.java"
--8<-- "framework/dominio/src/main/java/dev/sauloaraujo/d5/framework/dominio/spi/ISegurancaServico.java"
```

No tutorial, [`UnidadeFederativaServico`](../../tutorial.md#servico) recebe um `ISegurancaServico` no construtor e o consulta em `criarAutorizacoes`.

## Serviço

### `IAgregadoServico`
Realiza o conceito de [serviço](../ddd.md#servico). `IAgregadoServico` é parametrizada pelo identificador `I`, pelo agregado `A` e pelo tipo `U` de suas [autorizações](#iagregadoautorizacoes), e expõe `incluir`, `buscar`, `alterar`, `excluir` e `criarAutorizacoes`.

```java title="IAgregadoServico.java"
--8<-- "framework/dominio/src/main/java/dev/sauloaraujo/d5/framework/dominio/IAgregadoServico.java"
```

Implementações são registradas automaticamente como beans — veja [Registro de beans](comportamentos.md#registro-de-beans).

### `AgregadoServicoAbstrato`
Implementação base de [`IAgregadoServico`](#iagregadoservico). Recebe no construtor o [`ISegurancaServico`](#isegurancaservico), exposto às subclasses por `getSegurancaServico`, o [`IAgregadoRepositorio`](#iagregadorepositorio) do agregado e os modelos das mensagens de erro para identificador existente e inexistente — modelos no formato de `String.format`, com um `%s` substituído pelo identificador. Antes de cada operação, o método protegido `validarAutorizacao` — que recebe o identificador opcional do agregado e um predicado que seleciona, entre as [autorizações](#iagregadoautorizacoes), a da operação — chama o método abstrato `criarAutorizacoes`, que a subclasse implementa, e lança [`NaoAutorizadoExcecao`](#naoautorizadoexcecao) quando o predicado não libera a operação. `incluir` passa um identificador vazio; `buscar`, `alterar` e `excluir`, o identificador do agregado.

`incluir` e `alterar` chamam `validarInclusaoAlteracao`, ponto de extensão comum às duas operações que a subclasse pode sobrescrever para impor regras adicionais, como unicidade; `validarInclusao` e `validarAlteracao` são os pontos de extensão específicos de cada uma. `incluir`, ao receber um agregado com identificador, e `alterar` chamam `validarInexistencia` e `validarExistencia`, respectivamente, que lançam [`AgregadoExistenteExcecao`](#agregadoexistenteexcecao) e [`AgregadoInexistenteExcecao`](#agregadoinexistenteexcecao) a partir de `existePorIdentificador` do repositório; cada uma tem uma sobrecarga protegida — recebendo um predicado, um argumento e o modelo da mensagem de erro, formatado com o argumento — que a subclasse pode reutilizar para impor outras regras de unicidade ou existência. `alterar` exige que o agregado tenha identificador; sem ele, lança `NoSuchElementException`. `excluir` chama `validarExclusao`, ponto de extensão análogo para a exclusão.

```java title="AgregadoServicoAbstrato.java"
--8<-- "framework/dominio/src/main/java/dev/sauloaraujo/d5/framework/dominio/impl/AgregadoServicoAbstrato.java"
```

No tutorial, [`UnidadeFederativaServico`](../../tutorial.md#servico) estende `AgregadoServicoAbstrato` e sobrescreve `criarAutorizacoes` e `validarInclusaoAlteracao`.

## Exceções

### `AgregadoExistenteExcecao`
Estende `IllegalStateException`. Lançada por [`AgregadoServicoAbstrato.validarInexistencia`](#agregadoservicoabstrato) quando o identificador do agregado a incluir já existe; a subclasse pode reutilizar esse mesmo método para impor outras regras de unicidade. O backend converte essa exceção em uma resposta [409 Conflict](ui-backend.md#excecaomanipulador).

```java title="AgregadoExistenteExcecao.java"
--8<-- "framework/dominio/src/main/java/dev/sauloaraujo/d5/framework/dominio/AgregadoExistenteExcecao.java"
```

No tutorial, [`UnidadeFederativaServico`](../../tutorial.md#servico) a lança — através de `validarInexistencia` — quando a sigla ou o nome já pertencem a outra unidade federativa.

### `AgregadoInexistenteExcecao`
Estende `NoSuchElementException`. Declarada por [`IAgregadoRepositorio.buscarPorIdentificador`](#iagregadorepositorio), e lançada por [`AgregadoServicoAbstrato.validarExistencia`](#agregadoservicoabstrato) ao alterar ou excluir um agregado cujo identificador não existe. O backend converte essa exceção em uma resposta [404 Not Found](ui-backend.md#excecaomanipulador).

```java title="AgregadoInexistenteExcecao.java"
--8<-- "framework/dominio/src/main/java/dev/sauloaraujo/d5/framework/dominio/AgregadoInexistenteExcecao.java"
```

### `NaoAutorizadoExcecao`
Estende `RuntimeException`. Lançada por [`AgregadoServicoAbstrato.validarAutorizacao`](#agregadoservicoabstrato) e por [`AgregadoAplicacaoServicoAbstrato`](aplicacao.md#agregadoaplicacaoservicoabstrato) quando as [autorizações](#autorizacao) do usuário não liberam a operação. O backend converte essa exceção em uma resposta [403 Forbidden](ui-backend.md#excecaomanipulador).

```java title="NaoAutorizadoExcecao.java"
--8<-- "framework/dominio/src/main/java/dev/sauloaraujo/d5/framework/dominio/spi/NaoAutorizadoExcecao.java"
```

### `NaoAutenticadoExcecao`
Estende `RuntimeException`. Lançada pelos métodos de [`ISegurancaServico`](#isegurancaservico) quando não há usuário autenticado. O backend converte essa exceção em uma resposta [401 Unauthorized](ui-backend.md#excecaomanipulador).

```java title="NaoAutenticadoExcecao.java"
--8<-- "framework/dominio/src/main/java/dev/sauloaraujo/d5/framework/dominio/spi/NaoAutenticadoExcecao.java"
```
