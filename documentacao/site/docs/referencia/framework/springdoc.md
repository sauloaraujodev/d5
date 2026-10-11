# d5-framework-springdoc
Gera a documentação [OpenAPI](https://www.openapis.org){target="_blank"} da API REST do backend e a expõe através do [Swagger UI](https://swagger.io/tools/swagger-ui/){target="_blank"}, a partir de [springdoc-openapi](https://springdoc.org){target="_blank"}.

## `SpringDocConfiguracao`
Declara `basic` como o esquema de segurança da API, do tipo HTTP, e o aplica a toda a documentação gerada. É esse esquema que faz o Swagger UI exibir o botão **Authorize** e anexar o cabeçalho `Authorization` com usuário e senha a cada requisição, depois de autenticado. Também registra o `OperationCustomizer` que expande parâmetros `@ModelAttribute`, como o de `pesquisa`, em parâmetros individuais; veja [Expansão da consulta no Swagger UI](comportamentos.md#expansao-da-consulta-no-swagger-ui).

```java title="SpringDocConfiguracao.java"
--8<-- "framework/springdoc/src/main/java/dev/sauloaraujo/d5/framework/springdoc/SpringDocConfiguracao.java"
```

## `SpringDocEnvironmentPostProcessor`
Habilita, por padrão, o cabeçalho de CSRF nas requisições de escrita do Swagger UI; veja [CSRF no Swagger UI](comportamentos.md#csrf-no-swagger-ui).
