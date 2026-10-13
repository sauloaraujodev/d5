# Comportamentos automáticos
O framework aplica, de forma transparente, alguns comportamentos que independem de qualquer agregado específico — nenhuma classe precisa fazer nada para habilitá-los, mas é importante saber que existem:

- [registro de beans](#registro-de-beans);
- [serialização de objetos de valor](#serializacao-de-objetos-de-valor);
- [gerenciamento de transações](#gerenciamento-de-transacoes);
- [validação das entidades JPA](#validacao-das-entidades-jpa);
- [open session in view desativado](#open-session-in-view-desativado);
- [CSRF no Swagger UI](#csrf-no-swagger-ui);
- [expansão da consulta no Swagger UI](#expansao-da-consulta-no-swagger-ui);
- [objetos de valor no Swagger UI](#objetos-de-valor-no-swagger-ui).

## Registro de beans
Implementações de [`IAgregadoRepositorio`](dominio.md#iagregadorepositorio), [`IAgregadoAplicacaoRepositorio`](aplicacao.md#iagregadoaplicacaorepositorio), [`IAgregadoServico`](dominio.md#iagregadoservico) e [`IAgregadoAplicacaoServico`](aplicacao.md#iagregadoaplicacaoservico) nunca precisam de `@Component`, `@Service` ou `@Repository`: `AgregadoRegistrador`, do módulo `d5-framework-infraestrutura`, varre os pacotes de autoconfiguração da aplicação — o pacote da classe anotada com `@SpringBootApplication` e seus subpacotes — em busca de classes concretas que implementem uma dessas quatro interfaces e não estejam anotadas, direta ou indiretamente, com `@Component` — o que inclui `@Service` e `@Repository` —, e registra cada uma como um bean com autowiring por construtor. Classes fora desses pacotes não são registradas.

O nome de cada bean é gerado a partir do nome completo da classe — por exemplo, `com.example.exemplo.dominio.geografia.unidadefederativa.UnidadeFederativaServico#0` —, o que só importa quando outro bean precisa referenciá-lo pelo nome.

```java title="AgregadoRegistrador.java"
--8<-- "framework/infraestrutura/src/main/java/dev/sauloaraujo/d5/framework/infraestrutura/AgregadoRegistrador.java"
```

No tutorial, [`UnidadeFederativaServico`](../../tutorial/1-unidade-federativa.md#servico), [`UnidadeFederativaRepositorio`](../../tutorial/1-unidade-federativa.md#implementacao-do-repositorio), [`UnidadeFederativaAplicacaoServico`](../../tutorial/1-unidade-federativa.md#servico-de-aplicacao) e [`UnidadeFederativaAplicacaoRepositorio`](../../tutorial/1-unidade-federativa.md#implementacao-do-repositorio-de-aplicacao) são registrados dessa forma — nenhum leva anotação alguma do Spring.

## Serialização de objetos de valor
Um [objeto de valor](dominio.md#objetovalorstringabstrato) representado por uma única `String` — um código IBGE, uma sigla — trafega pela API como essa `String`, sem nenhum código de conversão no exemplo, em ambas as direções.

Na saída, `JacksonConfiguracao` registra um mixin que anota `toString()` com `@JsonValue`, fazendo o Jackson serializar a instância como o valor por trás dela, e não como um objeto com um atributo.

```java title="JacksonConfiguracao.java"
--8<-- "framework/ui-backend/src/main/java/dev/sauloaraujo/d5/framework/ui/JacksonConfiguracao.java"
```

```java title="ObjetoValorStringMixin.java"
--8<-- "framework/ui-backend/src/main/java/dev/sauloaraujo/d5/framework/ui/ObjetoValorStringMixin.java"
```

Na entrada, em parâmetros de caminho e de consulta, `ConversaoConfiguracao` registra `ObjetoValorStringConversorFactory`, que converte a `String` para qualquer subclasse de `ObjetoValorStringAbstrato` por reflexão, chamando seu construtor **público** de um único argumento `String` — uma subclasse sem esse construtor não pode ser recebida dessa forma. Se o construtor lançar uma exceção — por exemplo, porque o valor não atende ao formato de um [`ObjetoValorRegexAbstrato`](dominio.md#objetovalorregexabstrato) —, a fábrica a relança sem alteração, e o Spring MVC a embrulha em uma `MethodArgumentTypeMismatchException`; [`ExcecaoManipulador`](ui-backend.md#excecaomanipulador) percorre a cadeia de causas e, encontrando a `IllegalArgumentException`, responde [422 Unprocessable Content](https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Reference/Status/422){target="_blank"}, com a mensagem original.

```java title="ConversaoConfiguracao.java"
--8<-- "framework/ui-backend/src/main/java/dev/sauloaraujo/d5/framework/ui/ConversaoConfiguracao.java"
```

```java title="ObjetoValorStringConversorFactory.java"
--8<-- "framework/ui-backend/src/main/java/dev/sauloaraujo/d5/framework/ui/ObjetoValorStringConversorFactory.java"
```

No corpo JSON, a conversão de entrada é feita pelo próprio Jackson, sem configuração adicional: um objeto de valor é construído a partir da `String` através de seu construtor público de um único argumento `String`, e um agregado, através de seu construtor público, associando cada propriedade JSON ao parâmetro do construtor de mesmo nome. Essa associação depende de as classes serem compiladas com `-parameters`, o que o `spring-boot-starter-parent`, herdado através de `d5-pai`, já configura. Nesse caso, uma `IllegalArgumentException` lançada por um construtor chega a `ExcecaoManipulador` como causa de uma `HttpMessageNotReadableException` e resulta em [422 Unprocessable Content](https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Reference/Status/422){target="_blank"}, com a mensagem original.

No tutorial, é por causa desse comportamento que o parâmetro `identificador` de [`UnidadeFederativaControlador`](../../tutorial/1-unidade-federativa.md#controlador) é declarado como `UnidadeFederativaCodigoIbge`, e não como `String`, e que o corpo de uma requisição de inclusão ou alteração usa `"identificador": "35"` em vez de `"identificador": {"valor": "35"}`.

## Gerenciamento de transações
Toda escrita sobre um agregado — incluir, alterar ou excluir — precisa ocorrer dentro de uma transação, para que as verificações feitas pelo [serviço](dominio.md#iagregadoservico) e a persistência propriamente dita sejam confirmadas ou desfeitas em conjunto. Em vez de anotar cada método de cada serviço ou repositório com `@Transactional`, o módulo `d5-framework-ui-backend` decide se abre uma transação a partir do verbo HTTP do endpoint: qualquer método de um controlador mapeado para `POST` — a convenção usada por [`AgregadoControladorAbstrato`](ui-backend.md#agregadocontroladorabstrato) para `incluir`, `alterar` e `excluir` — ou mapeado com `@RequestMapping` sem verbo, que atende a todos eles, é envolvido por um advisor transacional; métodos mapeados para `GET`, como `pesquisa` e os formulários de inclusão, alteração e exclusão, não são.

A transação não impede, por si só, que duas requisições concorrentes passem pela mesma verificação de unicidade antes que qualquer uma delas grave: sob o nível de isolamento padrão dos bancos de dados usuais, ambas enxergam o estado anterior. Para garantir a unicidade sob concorrência, declare também a restrição no banco de dados — por exemplo, com `unique = true` na `@Column` da entidade JPA. A requisição que perder a corrida tem a gravação rejeitada pelo banco de dados e, como a que é rejeitada pelo serviço, responde [409 Conflict](https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Reference/Status/409){target="_blank"}, embora com uma mensagem genérica, já que a mensagem do banco de dados não é exposta; veja [`ExcecaoManipulador`](ui-backend.md#excecaomanipulador).

```java title="PostMappingTransactionAttributeSource.java"
--8<-- "framework/ui-backend/src/main/java/dev/sauloaraujo/d5/framework/ui/transacao/PostMappingTransactionAttributeSource.java"
```

A configuração abaixo registra esse `TransactionAttributeSource` e o conecta a um `TransactionInterceptor`, aplicado a todo bean do contexto através de um `BeanFactoryTransactionAttributeSourceAdvisor` — dispensando a anotação `@EnableTransactionManagement` e qualquer `@Transactional` explícito.

Como o `DefaultAdvisorAutoProxyCreator` é um `BeanPostProcessor`, ele e os beans de que depende são criados antes dos demais, quando nem todos os `BeanPostProcessor` estão registrados. Por isso, como na configuração de transações do próprio Spring, os métodos `@Bean` são estáticos e os beans são marcados com `@Role(BeanDefinition.ROLE_INFRASTRUCTURE)`, e o `TransactionInterceptor` não recebe o gerenciador de transações: ele o obtém do contexto apenas na primeira chamada transacional. Assim, o gerenciador de transações e o `EntityManagerFactory` não são criados antecipadamente, e o Spring não registra avisos de beans que deixaram de ser processados por todos os `BeanPostProcessor`.

```java title="TransacaoConfiguracao.java"
--8<-- "framework/ui-backend/src/main/java/dev/sauloaraujo/d5/framework/ui/transacao/TransacaoConfiguracao.java"
```

## Validação das entidades JPA
O módulo `d5-framework-infraestrutura` depende de `spring-boot-starter-validation`, que traz o [Hibernate Validator](https://hibernate.org/validator/){target="_blank"}. Com ele no classpath, o Hibernate usa as anotações do Bean Validation de uma entidade JPA — como `@NotNull` e `@Size` — de duas formas, sem nenhuma configuração na aplicação:

- ao gerar o esquema do banco de dados, `@NotNull` torna a coluna `not null` — inclusive a de um atributo de tipo primitivo, que, sem a anotação, o Hibernate declara anulável — e o `max` de `@Size` define seu tamanho, dispensando `nullable` e `length` na `@Column`;
- antes de cada `insert` e `update`, valida a entidade e, se alguma restrição for violada, lança `ConstraintViolationException` sem enviar o comando ao banco de dados — o que independe do banco e do driver JDBC. [`ExcecaoManipulador`](ui-backend.md#excecaomanipulador) responde [422 Unprocessable Content](https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Reference/Status/422){target="_blank"}, com a mensagem de cada violação precedida da propriedade da entidade — por exemplo, `nome: tamanho deve ser entre 0 e 100`.

Para entidades cujo identificador é atribuído pela aplicação, a validação ocorre na confirmação da transação, e não na chamada a `save`; nesse caso, a exceção chega a `ExcecaoManipulador` como causa de uma `TransactionSystemException`.

O Hibernate Validator escolhe o idioma das mensagens pela localidade padrão da JVM. Para que as mensagens fiquem em português, como as demais respostas do backend, independentemente do servidor, [`ValidacaoConfiguracao`](infraestrutura.md#validacaoconfiguracao) fornece ao Hibernate uma `ValidatorFactory` com a localidade `pt-BR`.

Essas anotações expressam apenas as restrições da persistência — como o tamanho de uma coluna —, e não as regras do domínio, que o agregado e seus objetos de valor já garantem; veja [Entidades JPA](../convencoes.md#entidades-jpa).

No tutorial, [`UnidadeFederativaJpa`](../../tutorial/1-unidade-federativa.md#entidade-jpa) declara dessa forma as restrições de suas colunas.

## Open session in view desativado
O Spring Boot ativa por padrão o *open session in view*: um `EntityManager` permanece aberto durante toda a requisição HTTP, permitindo que associações preguiçosas sejam carregadas depois do serviço, inclusive durante a serialização da resposta — e registra um aviso ao iniciar a aplicação quando a propriedade `spring.jpa.open-in-view` não é definida. Uma aplicação D5 não precisa desse padrão: as escritas ocorrem em uma transação que termina com o método do controlador, e as pesquisas devolvem [projeções](aplicacao.md#iprojecao), e não entidades JPA — que, além disso, não têm associações entre si.

Por isso, o módulo `d5-framework-ui-backend` define `false` como valor padrão dessa propriedade através de `JpaEnvironmentPostProcessor`, registrado em `META-INF/spring.factories` pelo mesmo motivo que [`SpringDocEnvironmentPostProcessor`](springdoc.md#springdocenvironmentpostprocessor). Como a propriedade é acrescentada com a menor precedência, qualquer outra fonte de configuração a redefine — por exemplo, `spring.jpa.open-in-view=true` no `application.properties` da aplicação.

```java title="JpaEnvironmentPostProcessor.java"
--8<-- "framework/ui-backend/src/main/java/dev/sauloaraujo/d5/framework/ui/JpaEnvironmentPostProcessor.java"
```

## CSRF no Swagger UI
Toda operação de escrita exposta pelo backend exige um token de CSRF, enviado pelo cliente através do cabeçalho `X-XSRF-TOKEN` — mas o Swagger UI só anexa esse cabeçalho automaticamente às requisições quando a propriedade `springdoc.swagger-ui.csrf.enabled` está `true`. O módulo `d5-framework-springdoc` define esse valor como padrão através de um `EnvironmentPostProcessor`, dispensando essa configuração em quem depende dele.

```java title="SpringDocEnvironmentPostProcessor.java"
--8<-- "framework/springdoc/src/main/java/dev/sauloaraujo/d5/framework/springdoc/SpringDocEnvironmentPostProcessor.java"
```

Veja [`d5-framework-springdoc`](springdoc.md#springdocenvironmentpostprocessor) para mais detalhes, incluindo por que esse comportamento é registrado em `META-INF/spring.factories`, e não em um arquivo `.imports`. O cookie que esse cabeçalho reflete só existe porque [`FiltroCookieCsrf`](ui-backend.md#filtrocookiecsrf) força sua criação antecipadamente — já na resposta à página do Swagger UI. O Swagger UI lê o cookie a cada requisição, e não apenas ao carregar a página; mas a resposta à primeira requisição autenticada apaga o cookie, porque o Spring Security renova o token de CSRF ao autenticar o usuário, e só a resposta seguinte traz um novo. Sem o cookie ou sem o cabeçalho, uma requisição de escrita é rejeitada com 403 e a mensagem `Token CSRF ausente ou inválido.`; veja [`ManipuladorAcessoNegado`](ui-backend.md#manipuladoracessonegado).

## Expansão da consulta no Swagger UI
`pesquisa` de [`AgregadoControladorAbstrato`](ui-backend.md#agregadocontroladorabstrato) recebe a [consulta](aplicacao.md#consulta) através de um parâmetro anotado com `@ModelAttribute`, que o Spring MVC associa a partir de parâmetros de requisição individuais — `nome`, `ordenacao`, `descendente`, `indicePagina` e `tamanhoPagina`, por exemplo — e não de um único parâmetro serializado em JSON. Sem nenhum ajuste, o Swagger UI documentaria `pesquisa` com um único parâmetro `consulta`, do tipo objeto, editável como uma caixa de texto JSON — que pareceria funcional, mas cujo conteúdo o Spring MVC nunca chega a interpretar.

O módulo `d5-framework-springdoc` corrige essa representação com um `OperationCustomizer` que localiza, em cada operação, os parâmetros de método anotados com `@ModelAttribute` e os substitui, na documentação, pelos parâmetros individuais de suas propriedades — refletindo exatamente os parâmetros que o Spring MVC de fato associa. Por atuar apenas sobre parâmetros `@ModelAttribute`, o customizador não afeta nenhum outro parâmetro — em particular, um futuro endpoint que realmente precise receber um objeto serializado em JSON por parâmetro de consulta permanece intocado.

```java title="ModelAttributeCustomizador.java"
--8<-- "framework/springdoc/src/main/java/dev/sauloaraujo/d5/framework/springdoc/ModelAttributeCustomizador.java"
```

Veja [`d5-framework-springdoc`](springdoc.md#springdocconfiguracao) para como esse customizador é registrado.

## Objetos de valor no Swagger UI
Os objetos de valor de string são serializados em JSON como uma string simples, conforme descrito em [serialização de objetos de valor](#serializacao-de-objetos-de-valor). O springdoc-openapi não conhece essa configuração do Jackson e, sem ajuste, documentaria esses tipos a partir de seus getters: um identificador com `getUnidadeFederativa()`, como o código IBGE de um município, apareceria no Swagger UI como um objeto `{ "unidadeFederativa": "string" }`, em vez de uma string.

O módulo `d5-framework-springdoc` corrige essa representação com um `ModelConverter` que descreve como `string` todo tipo que estende `ObjetoValorStringAbstrato`.

```java title="ObjetoValorStringConversor.java"
--8<-- "framework/springdoc/src/main/java/dev/sauloaraujo/d5/framework/springdoc/ObjetoValorStringConversor.java"
```

Veja [`d5-framework-springdoc`](springdoc.md#objetovalorstringconversor) para como esse conversor é registrado.
