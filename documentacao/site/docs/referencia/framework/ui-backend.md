# d5-framework-ui-backend
Possui as classes que expõem um [agregado](../ddd.md#agregado) como uma API REST, com Spring Web:

- [controlador](#controlador);
- [recurso do agregado](#recurso-do-agregado);
- [recurso de pesquisa](#recurso-de-pesquisa);
- [tratamento de erros](#tratamento-de-erros);
- [segurança](#seguranca);
- [conversão](#conversao);
- [transações](#transacoes).

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
Expõe as operações de um [serviço](dominio.md#iagregadoservico) e de um [serviço de aplicação](aplicacao.md#iagregadoaplicacaoservico) como endpoints REST. É parametrizada pelo identificador `I`, pelo agregado `A`, pelas autorizações `U`, pela projeção `P` e pela consulta `C`, e recebe no construtor o serviço e o serviço de aplicação.

A classe não declara `@RestController` nem o caminho base dos endpoints: a subclasse deve ser anotada com `@RestController` e com `@RequestMapping(Backend.PREFIXO + "...")`. Os caminhos abaixo são relativos a esse caminho base:

<div class="tabela-centralizada" markdown="1">

| Método | Requisição | Corpo da requisição | Resposta |
| --- | --- | --- | --- |
| `inclusao` | `GET inclusao` | — | objeto vazio, `{}` |
| `incluir` | `POST incluir` | agregado | 200, sem corpo |
| `alteracao` | `GET {identificador}/alteracao` | — | [recurso do agregado](#iagregadorecurso) |
| `alterar` | `POST {identificador}/alterar` | agregado | 200, sem corpo |
| `exclusao` | `GET {identificador}/exclusao` | — | [recurso do agregado](#iagregadorecurso) |
| `excluir` | `POST {identificador}/excluir` | — | 200, sem corpo |
| `pesquisa` | `GET pesquisa` | — | [recurso de pesquisa](#ipesquisarecurso) |

</div>

`alteracao` e `exclusao` buscam o agregado através de `buscar` do serviço — sujeito, portanto, à autorização de busca — e obtêm as autorizações através do método protegido `autorizacoes`, que delega a `criarAutorizacoes` do serviço; o cliente usa esse recurso para montar o formulário de alteração ou de confirmação da exclusão. `inclusao` não consulta o serviço nem verifica autorizações. `alterar` valida que o identificador do caminho é igual ao do agregado recebido no corpo, lançando `IllegalArgumentException` — e, portanto, respondendo 422 — caso contrário. `pesquisa` recebe a [consulta](aplicacao.md#consulta-classe) como `@ModelAttribute`, isto é, a partir de parâmetros de requisição individuais.

`alteracao`, `exclusao` e `pesquisa` têm, cada uma, uma sobrecarga protegida — recebendo o agregado e as autorizações, ou a consulta e o resultado, já obtidos — que a subclasse pode sobrescrever para customizar o recurso devolvido. Por padrão, a sobrecarga de `exclusao` delega à de `alteracao`.

```java title="AgregadoControladorAbstrato.java"
--8<-- "framework/ui-backend/src/main/java/dev/sauloaraujo/d5/framework/ui/AgregadoControladorAbstrato.java"
```

Os métodos mapeados para `POST` — `incluir`, `alterar` e `excluir` — são executados dentro de uma transação automaticamente; veja [Gerenciamento de transações](comportamentos.md#gerenciamento-de-transacoes).

No tutorial, [`UnidadeFederativaControlador`](../../tutorial/1-unidade-federativa.md#controlador) estende `AgregadoControladorAbstrato`, e [`MunicipioControlador`](../../tutorial/2-municipio.md#controlador) acrescenta às operações herdadas a ação de extinguir.

## Tratamento de erros

### `ExcecaoManipulador`
Converte as exceções lançadas pelos controladores — e, através deles, pelos serviços e repositórios — em uma resposta JSON — `{"mensagem": "..."}` — com o status HTTP correspondente, sem que controladores, serviços ou repositórios precisem tratar nada:

| Exceção | Status |
| --- | --- |
| `AccessDeniedException` (Spring Security), [`NaoAutorizadoExcecao`](dominio.md#naoautorizadoexcecao) | [403 Forbidden](https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Reference/Status/403){target="_blank"} |
| `AuthenticationException` (Spring Security), [`NaoAutenticadoExcecao`](dominio.md#naoautenticadoexcecao) | [401 Unauthorized](https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Reference/Status/401){target="_blank"} |
| `IllegalArgumentException`, `NullPointerException` | [422 Unprocessable Content](https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Reference/Status/422){target="_blank"} |
| `IllegalStateException` — inclui [`AgregadoExistenteExcecao`](dominio.md#agregadoexistenteexcecao) | [409 Conflict](https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Reference/Status/409){target="_blank"} |
| `NoSuchElementException` — inclui [`AgregadoInexistenteExcecao`](dominio.md#agregadoinexistenteexcecao) | [404 Not Found](https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Reference/Status/404){target="_blank"} |
| `HttpMessageNotReadableException` (corpo da requisição ilegível) | a causa é percorrida em busca de uma das exceções acima; sem correspondência, [400 Bad Request](https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Reference/Status/400){target="_blank"} |
| `TypeMismatchException` (parâmetro de caminho ou de consulta não convertido) | [422 Unprocessable Content](https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Reference/Status/422){target="_blank"} — veja abaixo a mensagem; exceto `ConversionNotSupportedException` (nenhum conversor para o tipo do parâmetro), que é um erro de programação: [500 Internal Server Error](https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Reference/Status/500){target="_blank"}, registrada em log |
| `BindException` — inclui `MethodArgumentNotValidException` (parâmetros de um `@ModelAttribute` não associados) | para um parâmetro não convertido, o mesmo tratamento de `TypeMismatchException`; para outros erros de associação, a causa é percorrida em busca de uma das exceções acima; sem correspondência, [400 Bad Request](https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Reference/Status/400){target="_blank"} |
| `BeanInstantiationException` (construtor de um `@ModelAttribute` lançou uma exceção) | a causa é percorrida em busca de uma das exceções acima; sem correspondência, [500 Internal Server Error](https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Reference/Status/500){target="_blank"}, registrada em log |
| `ConstraintViolationException` (Bean Validation) — lançada ao validar uma entidade JPA antes de gravá-la; veja [Validação das entidades JPA](comportamentos.md#validacao-das-entidades-jpa) | [422 Unprocessable Content](https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Reference/Status/422){target="_blank"} — veja abaixo a mensagem |
| `TransactionSystemException` (Spring) — lançada, por exemplo, quando a validação de uma entidade JPA ocorre na confirmação da transação | se a cadeia de causas contém uma `ConstraintViolationException`, o mesmo tratamento dela; caso contrário, [500 Internal Server Error](https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Reference/Status/500){target="_blank"}, registrada em log |
| `DataIntegrityViolationException` (Spring) | se a cadeia de causas contém uma `SQLException` com SQLSTATE da classe `22` (dado inválido) — por exemplo, `22001`, valor maior que o tamanho da coluna, e `22003`, número fora do intervalo do tipo da coluna —, [422 Unprocessable Content](https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Reference/Status/422){target="_blank"}; caso contrário, [500 Internal Server Error](https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Reference/Status/500){target="_blank"}, registrada em log |
| exceção do Spring MVC que implementa `ErrorResponse` com status 4xx — por exemplo, `NoResourceFoundException` (caminho sem controlador), `HttpRequestMethodNotSupportedException` (verbo não suportado) e `HttpMediaTypeNotSupportedException` (`Content-Type` não suportado) | o status declarado pela exceção — no exemplo, [404 Not Found](https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Reference/Status/404){target="_blank"}, [405 Method Not Allowed](https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Reference/Status/405){target="_blank"} e [415 Unsupported Media Type](https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Reference/Status/415){target="_blank"}, respectivamente |
| qualquer outra `Exception` | [500 Internal Server Error](https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Reference/Status/500){target="_blank"}, registrada em log |

Ao percorrer a cadeia de causas, prevalece a primeira exceção mapeada encontrada, e a mensagem da resposta é a dela. Assim, a mensagem de validação lançada pelo construtor de uma [consulta](aplicacao.md#consulta-classe) ou de um objeto de valor recebido no corpo chega intacta ao cliente.

Nos casos a seguir, a resposta usa uma mensagem em português em vez da mensagem gerada pela JDK ou pelo Spring, que viria em inglês e com detalhes de implementação:

- quando um parâmetro de caminho ou de consulta não é convertido, a resposta preserva a mensagem do domínio apenas se o parâmetro é um [objeto de valor](dominio.md#objetovalorstringabstrato) — isto é, uma subclasse de `ObjetoValorStringAbstrato`, cujo construtor lançou a exceção. Para os demais tipos — `enum`, números, booleanos, datas —, a mensagem é fixa e identifica o parâmetro e o valor recebido: `Valor inválido para o parâmetro ordenacao: FOO`;
- a mensagem de uma `ConstraintViolationException` reúne as violações, ordenadas e separadas por `; `, cada uma no formato `propriedade: mensagem` — por exemplo, `nome: tamanho deve ser entre 0 e 100; numero: tamanho deve ser entre 0 e 10`;
- quando o banco de dados rejeita um valor com SQLSTATE da classe `22`, a resposta tem a mensagem fixa `Um ou mais valores informados excedem o tamanho ou o intervalo permitidos.`, em vez da mensagem do driver JDBC, que exporia nomes de tabelas e colunas. Esse tratamento cobre os limites que a [validação das entidades JPA](comportamentos.md#validacao-das-entidades-jpa) não verifica — por exemplo, os de uma entidade sem as anotações do Bean Validation;
- as exceções que implementam `ErrorResponse` respondem com uma mensagem fixa associada ao status — por exemplo, `Recurso não encontrado.` para 404, `Método HTTP não suportado.` para 405 e `Formato do corpo da requisição não suportado.` para 415;
- uma exceção mapeada sem mensagem — como a lançada por `Objects.requireNonNull` sem argumento de mensagem — responde com a mesma mensagem fixa associada ao seu status — por exemplo, `Dados inválidos.` para 422.

`AccessDeniedException` e `AuthenticationException` têm tratamento explícito porque, sem ele, uma dessas exceções lançada de dentro de um controlador — por exemplo, por uma verificação de segurança em nível de método — seria capturada pelo handler genérico de `Exception` e viraria [500 Internal Server Error](https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Reference/Status/500){target="_blank"}, em vez de 403 ou 401.

Exceções lançadas fora dos controladores, na cadeia de filtros do Spring Security — como a negação de um endpoint pelas regras de [`SegurancaConfiguracaoAbstrata`](#segurancaconfiguracaoabstrata), a ausência de autenticação ou a ausência do token de CSRF — não passam por `ExcecaoManipulador`. Elas também respondem no formato `{"mensagem": "..."}`, mas através de [`EntradaAutenticacaoSemDesafio`](#entradaautenticacaosemdesafio), com 401, e de [`ManipuladorAcessoNegado`](#manipuladoracessonegado), com 403.

```java title="ExcecaoManipulador.java"
--8<-- "framework/ui-backend/src/main/java/dev/sauloaraujo/d5/framework/ui/ExcecaoManipulador.java"
```

## Segurança

### `SegurancaConfiguracaoAbstrata`
Configura o Spring Security para o backend através do bean `filtroSeguranca`, um `SecurityFilterChain`. A subclasse deve ser anotada com `@Configuration` e implementar o método abstrato `autorizar`, que recebe o registro de regras de `authorizeHttpRequests`. As regras são avaliadas na ordem em que são declaradas, e prevalece a primeira que corresponde à requisição:

1. os recursos do Swagger UI e da documentação OpenAPI, sob `/swagger-ui/` e `/v3/api-docs`, são liberados a todos;
2. as regras declaradas por `autorizar`;
3. `POST` em `SAIR` exige autenticação;
4. qualquer outra requisição sob `BACKEND` — a constante protegida `"/" + Backend.PREFIXO + "**"`, no formato exigido por `authorizeHttpRequests` — é **negada**.

Portanto, um endpoint sob [`Backend.PREFIXO`](#backend) só é acessível se `autorizar` o liberar explicitamente. Os caminhos do Swagger UI são fixos e correspondem aos padrões do springdoc-openapi; se `springdoc.swagger-ui.path` ou `springdoc.api-docs.path` forem alterados, o Swagger UI deixa de ser liberado.

Além disso, `filtroSeguranca`:

- autentica via HTTP Basic, com [`EntradaAutenticacaoSemDesafio`](#entradaautenticacaosemdesafio) no lugar do desafio padrão;
- adota a política de sessão `SessionCreationPolicy.IF_REQUIRED`;
- configura o logout em `SAIR` — a constante protegida `"/" + Backend.PREFIXO + "sair"` —, que responde [204 No Content](https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Reference/Status/204){target="_blank"};
- protege contra CSRF com o cookie `XSRF-TOKEN`, não `HttpOnly`, lido pelo cliente e devolvido no cabeçalho `X-XSRF-TOKEN`, através de [`ManipuladorRequisicaoCsrfSpa`](#manipuladorrequisicaocsrfspa) e [`FiltroCookieCsrf`](#filtrocookiecsrf). Uma requisição de escrita sem token válido é rejeitada com 403 e a mensagem `Token CSRF ausente ou inválido.`, antes mesmo da verificação das credenciais;
- responde às negações de acesso na cadeia de filtros — por CSRF ou pelas regras de autorização — através de [`ManipuladorAcessoNegado`](#manipuladoracessonegado).

Também expõe, como bean, o `PasswordEncoder` devolvido por `codificadorSenha`, usado para codificar e verificar as senhas dos usuários da aplicação. É um `DelegatingPasswordEncoder`: codifica com bcrypt e prefixa a senha codificada com o identificador do algoritmo — `{bcrypt}...` —, prefixo que toda senha armazenada deve trazer para ser verificada.

```java title="SegurancaConfiguracaoAbstrata.java"
--8<-- "framework/ui-backend/src/main/java/dev/sauloaraujo/d5/framework/ui/seguranca/SegurancaConfiguracaoAbstrata.java"
```

No tutorial, [`SegurancaConfiguracao`](../../tutorial/1-unidade-federativa.md#seguranca) estende `SegurancaConfiguracaoAbstrata`, implementando `autorizar` e declarando os usuários da aplicação.

### `EntradaAutenticacaoSemDesafio`
Por padrão, a autenticação HTTP Basic do Spring Security responde a uma requisição não autenticada com o cabeçalho `WWW-Authenticate`, que faz o navegador abrir o diálogo nativo de login — inadequado para uma SPA ou para o Swagger UI, que devem tratar a autenticação por conta própria. Esta implementação de `AuthenticationEntryPoint` responde `401` com um corpo JSON, sem esse cabeçalho.

```java title="EntradaAutenticacaoSemDesafio.java"
--8<-- "framework/ui-backend/src/main/java/dev/sauloaraujo/d5/framework/ui/seguranca/EntradaAutenticacaoSemDesafio.java"
```

### `ManipuladorAcessoNegado`
Por padrão, o Spring Security responde a uma negação de acesso na cadeia de filtros com `sendError(403)`, que encaminha a requisição ao endpoint de erro, `/error`. Como as regras de [`SegurancaConfiguracaoAbstrata`](#segurancaconfiguracaoabstrata) não liberam `/error`, esse encaminhamento também seria negado:

- uma falha de CSRF, verificada antes da autenticação, chegaria a `/error` com o usuário ainda anônimo e resultaria em 401 com a mensagem de usuário não autenticado, mesmo com credenciais válidas;
- a negação de um endpoint por `autorizar` resultaria em 403, mas com corpo vazio.

Esta implementação de `AccessDeniedHandler` escreve a resposta diretamente, sem encaminhá-la a `/error`: 403 com um corpo JSON cuja mensagem é `Token CSRF ausente ou inválido.`, para falhas de CSRF, ou `Ação não autorizada.`, para as demais negações. Assim, o cliente distingue a falta do token — que se resolve obtendo um novo cookie — da falta de credenciais, que responde 401.

```java title="ManipuladorAcessoNegado.java"
--8<-- "framework/ui-backend/src/main/java/dev/sauloaraujo/d5/framework/ui/seguranca/ManipuladorAcessoNegado.java"
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

A seção [CSRF no Swagger UI](comportamentos.md#csrf-no-swagger-ui) descreve o efeito de `FiltroCookieCsrf` e `ManipuladorRequisicaoCsrfSpa` na prática.

### `AutenticacaoControladorAbstrato`
Expõe, através de `GET` no caminho base da subclasse, o recurso devolvido pelo método protegido `recurso` — permitindo ao cliente descobrir, por exemplo, o nome do usuário autenticado. Por padrão, `recurso` devolve um [`AutenticacaoRecurso`](#autenticacaorecurso) com o usuário obtido do [`ISegurancaServico`](dominio.md#isegurancaservico) recebido no construtor, disponível às subclasses pelo atributo protegido `segurancaServico`; a subclasse pode sobrescrevê-lo para devolver outras informações. Assim como em [`AgregadoControladorAbstrato`](#agregadocontroladorabstrato), a subclasse deve ser anotada com `@RestController` e `@RequestMapping`. Sem usuário autenticado, a resposta é 401.

```java title="AutenticacaoControladorAbstrato.java"
--8<-- "framework/ui-backend/src/main/java/dev/sauloaraujo/d5/framework/ui/seguranca/AutenticacaoControladorAbstrato.java"
```

No tutorial, [`AutenticacaoControlador`](../../tutorial/1-unidade-federativa.md#autenticacao) estende `AutenticacaoControladorAbstrato`.

### `AutenticacaoRecurso`
Recurso devolvido por `AutenticacaoControladorAbstrato`, com o nome do usuário autenticado.

```java title="AutenticacaoRecurso.java"
--8<-- "framework/ui-backend/src/main/java/dev/sauloaraujo/d5/framework/ui/seguranca/AutenticacaoRecurso.java"
```

## Conversão

### `ConversaoConfiguracao` e `JacksonConfiguracao`
Fazem todo [objeto de valor](dominio.md#objetovalorstringabstrato) que estende `ObjetoValorStringAbstrato` trafegar como a `String` que representa — sem que a aplicação escreva nenhum código de conversão. `JacksonConfiguracao` o serializa como essa `String` no corpo JSON, através de `ObjetoValorStringMixin`; `ConversaoConfiguracao` registra `ObjetoValorStringConversorFactory`, que o converte a partir de parâmetros de caminho e de consulta. Veja [Serialização de objetos de valor](comportamentos.md#serializacao-de-objetos-de-valor).

## Transações

### `TransacaoConfiguracao` e `PostMappingTransactionAttributeSource`
Envolvem em uma transação todo método de controlador mapeado para `POST`, sem `@Transactional` nem `@EnableTransactionManagement`. Veja [Gerenciamento de transações](comportamentos.md#gerenciamento-de-transacoes).
