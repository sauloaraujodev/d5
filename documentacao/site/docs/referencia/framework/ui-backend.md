# d5-framework-ui-backend
Possui as classes que expõem um [agregado](../ddd.md#agregado) como uma API REST, com Spring Web:

- [controlador](#controlador);
- [recurso do agregado](#recurso-do-agregado);
- [recurso de pesquisa](#recurso-de-pesquisa);
- [tratamento de erros](#tratamento-de-erros);
- [segurança](#seguranca);
- [conversão](#conversao).

## `Backend`
Declara `PREFIXO`, o prefixo `backend/` usado no `@RequestMapping` de todo controlador do backend.

```java title="Backend.java"
--8<-- "framework/ui-backend/src/main/java/dev/sauloaraujo/d5/framework/ui/Backend.java"
```

## Recurso do agregado

### `IAgregadoRecurso`
Representa a resposta de uma operação sobre um único [agregado](../ddd.md#agregado) `A`: o próprio agregado e as [autorizações](dominio.md#iagregadoautorizacoes) `U` do usuário sobre ele.

```java title="IAgregadoRecurso.java"
--8<-- "framework/ui-backend/src/main/java/dev/sauloaraujo/d5/framework/ui/IAgregadoRecurso.java"
```

### `AgregadoRecurso`
Implementação de `IAgregadoRecurso` que recebe o agregado e as autorizações diretamente pelo construtor.

```java title="AgregadoRecurso.java"
--8<-- "framework/ui-backend/src/main/java/dev/sauloaraujo/d5/framework/ui/AgregadoRecurso.java"
```

## Recurso de pesquisa

### `IPesquisaRecurso`
Representa a resposta de uma pesquisa: a [consulta](aplicacao.md#iconsulta) `C` recebida e o [resultado](aplicacao.md#resultado-da-consulta) de [projeções](aplicacao.md#iprojecao) `P`.

```java title="IPesquisaRecurso.java"
--8<-- "framework/ui-backend/src/main/java/dev/sauloaraujo/d5/framework/ui/IPesquisaRecurso.java"
```

### `PesquisaRecurso`
Implementação de `IPesquisaRecurso` que recebe a consulta e o resultado diretamente pelo construtor.

```java title="PesquisaRecurso.java"
--8<-- "framework/ui-backend/src/main/java/dev/sauloaraujo/d5/framework/ui/PesquisaRecurso.java"
```

## Controlador

### `AgregadoControladorAbstrato`
Expõe as operações de um [serviço](dominio.md#iagregadoservico) e de um [serviço de aplicação](aplicacao.md#iagregadoaplicacaoservico) como endpoints REST: `inclusao` e `incluir`, `alteracao` e `alterar`, `exclusao` e `excluir`, e `pesquisa`. Cada operação de escrita é precedida por uma correspondente que devolve o [recurso do agregado](#iagregadorecurso) — agregado e autorizações, obtidas por `autorizacoes` a partir de `criarAutorizacoes` do serviço — usado pelo cliente para montar o formulário; `pesquisa` devolve o [recurso de pesquisa](#ipesquisarecurso) a partir da consulta recebida como parâmetros da requisição. `alteracao`, `exclusao` e `pesquisa` têm, cada uma, uma sobrecarga protegida — recebendo o agregado e as autorizações, ou a consulta e o resultado, já obtidos — que a subclasse pode sobrescrever para customizar o recurso devolvido.

```java title="AgregadoControladorAbstrato.java"
--8<-- "framework/ui-backend/src/main/java/dev/sauloaraujo/d5/framework/ui/AgregadoControladorAbstrato.java"
```

Os métodos mapeados para `POST` — `incluir`, `alterar` e `excluir` — são executados dentro de uma transação automaticamente; veja [Gerenciamento de transações](comportamentos.md#gerenciamento-de-transacoes).

No tutorial, [`UnidadeFederativaControlador`](../../tutorial.md#controlador) estende `AgregadoControladorAbstrato`.

## Tratamento de erros

### `ExcecaoManipulador`
Converte qualquer exceção lançada durante o processamento de uma requisição em uma resposta JSON — `{"mensagem": "..."}` — com o status HTTP correspondente, sem que controladores, serviços ou repositórios precisem tratar nada:

| Exceção | Status |
| --- | --- |
| `AccessDeniedException` (Spring Security), [`NaoAutorizadoExcecao`](dominio.md#naoautorizadoexcecao) | [403 Forbidden](https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Reference/Status/403){target="_blank"} |
| `AuthenticationException` (Spring Security), [`NaoAutenticadoExcecao`](dominio.md#naoautenticadoexcecao) | [401 Unauthorized](https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Reference/Status/401){target="_blank"} |
| `IllegalArgumentException`, `NullPointerException` | [422 Unprocessable Content](https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Reference/Status/422){target="_blank"} |
| `IllegalStateException` — inclui [`AgregadoExistenteExcecao`](dominio.md#agregadoexistenteexcecao) | [409 Conflict](https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Reference/Status/409){target="_blank"} |
| `NoSuchElementException` — inclui [`AgregadoInexistenteExcecao`](dominio.md#agregadoinexistenteexcecao) | [404 Not Found](https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Reference/Status/404){target="_blank"} |
| `HttpMessageNotReadableException` (corpo da requisição ilegível) | a causa é percorrida em busca de uma das exceções acima; sem correspondência, [400 Bad Request](https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Reference/Status/400){target="_blank"} |
| qualquer outra `Exception` | [500 Internal Server Error](https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Reference/Status/500){target="_blank"}, registrada em log |

`AccessDeniedException` e `AuthenticationException` — lançadas pelo próprio Spring Security, por exemplo quando `autorizar` nega um endpoint — precisam de tratamento explícito apesar de já produzirem 403/401 por conta do framework: lançadas de dentro de um controlador, seriam interceptadas antes pelo mecanismo padrão de tratamento de exceções do Spring MVC, chegando a `ExcecaoManipulador` só pelo handler genérico de `Exception` — e virando [500 Internal Server Error](https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Reference/Status/500){target="_blank"}.

```java title="ExcecaoManipulador.java"
--8<-- "framework/ui-backend/src/main/java/dev/sauloaraujo/d5/framework/ui/ExcecaoManipulador.java"
```

## Segurança

### `SegurancaConfiguracaoAbstrata`
Configura o Spring Security para o backend: exige autenticação para todo endpoint sob [`Backend.PREFIXO`](#backend) — a constante protegida `BACKEND`, no formato exigido por `authorizeHttpRequests` — exceto os do próprio Swagger UI, sempre liberados — delegando ao método abstrato `autorizar` regras adicionais por endpoint, como as que restringem escrita a administradores; expõe a constante protegida `SAIR` como `POST .../sair` para logout; autentica via HTTP Basic, com [`EntradaAutenticacaoSemDesafio`](#entradaautenticacaosemdesafio) no lugar do desafio padrão; e protege contra CSRF com um cookie não `HttpOnly`, lido pelo cliente e devolvido no cabeçalho `X-XSRF-TOKEN`, através de [`ManipuladorRequisicaoCsrfSpa`](#manipuladorrequisicaocsrfspa) e [`FiltroCookieCsrf`](#filtrocookiecsrf). Também expõe, como bean, o `PasswordEncoder` devolvido por `codificadorSenha`, usado para codificar e verificar as senhas dos usuários da aplicação.

```java title="SegurancaConfiguracaoAbstrata.java"
--8<-- "framework/ui-backend/src/main/java/dev/sauloaraujo/d5/framework/ui/seguranca/SegurancaConfiguracaoAbstrata.java"
```

No tutorial, [`SegurancaConfiguracao`](../../tutorial.md#seguranca) estende `SegurancaConfiguracaoAbstrata`, implementando `autorizar` e declarando os usuários da aplicação.

### `EntradaAutenticacaoSemDesafio`
Por padrão, a autenticação HTTP Basic do Spring Security responde a uma requisição não autenticada com o cabeçalho `WWW-Authenticate`, que faz o navegador abrir o diálogo nativo de login — inadequado para uma SPA ou para o Swagger UI, que devem tratar a autenticação por conta própria. Esta implementação de `AuthenticationEntryPoint` responde `401` com um corpo JSON, sem esse cabeçalho.

```java title="EntradaAutenticacaoSemDesafio.java"
--8<-- "framework/ui-backend/src/main/java/dev/sauloaraujo/d5/framework/ui/seguranca/EntradaAutenticacaoSemDesafio.java"
```

### `FiltroCookieCsrf`
O token de CSRF do Spring Security é resolvido de forma [preguiçosa](https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html#deferred-csrf-token){target="_blank"}: o cookie só é enviado ao cliente se algo, durante o processamento da requisição, efetivamente ler o token. Como o backend não renderiza HTML nem acessa o token em nenhuma outra requisição, o cookie nunca seria criado. Este filtro força essa leitura em toda requisição, logo após a autenticação, garantindo que o cookie já exista desde a primeira resposta.

```java title="FiltroCookieCsrf.java"
--8<-- "framework/ui-backend/src/main/java/dev/sauloaraujo/d5/framework/ui/seguranca/FiltroCookieCsrf.java"
```

### `ManipuladorRequisicaoCsrfSpa`
Implementação de `CsrfTokenRequestAttributeHandler` que segue a [recomendação do Spring Security para SPAs](https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html#csrf-integration-javascript-spa){target="_blank"}: delega a `XorCsrfTokenRequestAttributeHandler` — que mascara o token para mitigar ataques [BREACH](https://docs.spring.io/spring-security/reference/features/exploits/csrf.html#csrf-protection-stateless-breach){target="_blank"} — mas, ao resolver o token enviado pelo cliente, usa o valor do cabeçalho `X-XSRF-TOKEN` sem máscara quando presente, já que é lido diretamente do cookie por JavaScript, em vez de devolvido mascarado por um formulário renderizado pelo servidor.

```java title="ManipuladorRequisicaoCsrfSpa.java"
--8<-- "framework/ui-backend/src/main/java/dev/sauloaraujo/d5/framework/ui/seguranca/ManipuladorRequisicaoCsrfSpa.java"
```

No tutorial, a seção [CSRF no Swagger UI](comportamentos.md#csrf-no-swagger-ui) descreve o efeito desses dois filtros na prática.

### `AutenticacaoControladorAbstrato`
Expõe, em `Backend.PREFIXO + "autenticacao"`, o [`ISegurancaServico`](dominio.md#isegurancaservico) recebido no construtor como um recurso somente leitura — permitindo ao cliente descobrir, por exemplo, o nome do usuário autenticado.

```java title="AutenticacaoControladorAbstrato.java"
--8<-- "framework/ui-backend/src/main/java/dev/sauloaraujo/d5/framework/ui/seguranca/AutenticacaoControladorAbstrato.java"
```

No tutorial, [`AutenticacaoControlador`](../../tutorial.md#autenticacao) estende `AutenticacaoControladorAbstrato`.

### `AutenticacaoRecurso`
Recurso devolvido por `AutenticacaoControladorAbstrato`, com o nome do usuário autenticado.

```java title="AutenticacaoRecurso.java"
--8<-- "framework/ui-backend/src/main/java/dev/sauloaraujo/d5/framework/ui/seguranca/AutenticacaoRecurso.java"
```

## Conversão

### `ConversaoConfiguracao` e `JacksonConfiguracao`
Fazem todo [objeto de valor](dominio.md#objetovalorstringabstrato) representado por uma única `String` trafegar como essa `String`, em parâmetros de caminho, de consulta e no corpo JSON — sem que o exemplo escreva nenhum código de conversão. Veja [Serialização de objetos de valor](comportamentos.md#serializacao-de-objetos-de-valor).
