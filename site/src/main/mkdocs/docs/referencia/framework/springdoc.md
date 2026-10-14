# d5-framework-springdoc
Gera a documentação [OpenAPI](https://www.openapis.org){target="_blank"} da API REST do backend e a expõe através do [Swagger UI](https://swagger.io/tools/swagger-ui/){target="_blank"}, a partir de [springdoc-openapi](https://springdoc.org){target="_blank"}.

Como nenhuma classe da aplicação depende dele, o módulo pode ser declarado apenas em tempo de execução (`<scope>runtime</scope>`), como no [tutorial](../../tutorial/1-unidade-federativa.md#arquivos-pomxml). Os caminhos do Swagger UI e da documentação OpenAPI são liberados sem autenticação por [`SegurancaConfiguracaoAbstrata`](ui-backend.md#segurancaconfiguracaoabstrata), que assume os caminhos padrão do springdoc-openapi.

## `SpringDocConfiguracao`
Declara `basic` como o esquema de segurança da API, do tipo HTTP, e o aplica a toda a documentação gerada. É esse esquema que faz o Swagger UI exibir o botão **Authorize** e anexar o cabeçalho `Authorization` com usuário e senha a cada requisição, depois de autenticado. Também registra o `OperationCustomizer` que expande parâmetros `@ModelAttribute`, como o de `pesquisa`, em parâmetros individuais; veja [Expansão da consulta no Swagger UI](comportamentos.md#expansao-da-consulta-no-swagger-ui). Por fim, registra o `ModelConverter` que documenta objetos de valor de string como `string`; veja [`ObjetoValorStringConversor`](#objetovalorstringconversor).

```java title="SpringDocConfiguracao.java"
--8<-- "framework/springdoc/src/main/java/dev/sauloaraujo/d5/framework/springdoc/SpringDocConfiguracao.java"
```

## `ObjetoValorStringConversor`
Descreve como `string`, na documentação OpenAPI, todo tipo que estende [`ObjetoValorStringAbstrato`](dominio.md#objetovalorstringabstrato). Esses objetos trafegam como uma string simples, mas o springdoc-openapi desconhece essa serialização e, sem o conversor, documentaria o tipo a partir de seus getters — por exemplo, um identificador com o método `getUnidadeFederativa()` apareceria como um objeto com a propriedade `unidadeFederativa`, tanto nos parâmetros de caminho quanto no corpo das requisições. Veja [Serialização de objetos de valor](comportamentos.md#serializacao-de-objetos-de-valor).

```java title="ObjetoValorStringConversor.java"
--8<-- "framework/springdoc/src/main/java/dev/sauloaraujo/d5/framework/springdoc/ObjetoValorStringConversor.java"
```

## `SpringDocEnvironmentPostProcessor`
Habilita, por padrão, o cabeçalho de CSRF nas requisições de escrita do Swagger UI, definindo a propriedade `springdoc.swagger-ui.csrf.enabled` como `true`; veja [CSRF no Swagger UI](comportamentos.md#csrf-no-swagger-ui). A propriedade é adicionada como a fonte de propriedades de menor precedência, de modo que a aplicação pode sobrescrevê-la — por exemplo, em `application.properties`.

```java title="SpringDocEnvironmentPostProcessor.java"
--8<-- "framework/springdoc/src/main/java/dev/sauloaraujo/d5/framework/springdoc/SpringDocEnvironmentPostProcessor.java"
```

É registrado em `META-INF/spring.factories`, e não em `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` como `SpringDocConfiguracao`, porque não é uma autoconfiguração: o Spring Boot executa os `EnvironmentPostProcessor` ao preparar o `Environment`, antes de criar o contexto da aplicação — e, portanto, antes de processar qualquer autoconfiguração —, e os descobre apenas através de `spring.factories`.
