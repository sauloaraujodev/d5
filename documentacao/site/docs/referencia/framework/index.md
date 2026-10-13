# Framework

## Projeto pai
O `pom.xml` raiz de uma aplicação D5 herda de `d5-pai`, que:

- herda de `spring-boot-starter-parent` 4.1 — portanto, a aplicação usa Spring Boot 4.1;
- define Java 25 como versão do Java;
- gerencia as versões dos módulos do framework, que são declarados sem `<version>`;
- declara a dependência de [JSpecify](https://jspecify.dev){target="_blank"} em todos os módulos e configura a verificação de nulidade em tempo de compilação; veja [Evite `null`](../programacao.md#evite-null).

## Módulos principais
O framework possui módulos para as camadas de domínio, aplicação, infraestrutura e UI.

Todos têm `groupId` `dev.sauloaraujo` e estão no [Maven Central](https://central.sonatype.com/namespace/dev.sauloaraujo){target="_blank"}. Na versão `1.0.0-SNAPSHOT`, é preciso declarar o repositório de snapshots, como no [`pom.xml` raiz do tutorial](../../tutorial/1-unidade-federativa.md#arquivos-pomxml).

<div class="tabela-centralizada" markdown="1">

| Módulo | Camada |
| --- | --- |
| [`d5-framework-dominio`](dominio.md) | Domínio |
| [`d5-framework-aplicacao`](aplicacao.md) | Aplicação |
| [`d5-framework-infraestrutura`](infraestrutura.md) | Infraestrutura |
| [`d5-framework-ui-backend`](ui-backend.md) | UI |
| [`d5-framework-springdoc`](springdoc.md) | UI |

</div>

## Módulos de teste
Cada camada tem também um módulo de teste correspondente, que traz as dependências necessárias para testá-la — assim, um módulo de domínio, aplicação, infraestrutura ou UI de uma aplicação D5 não precisa declarar individualmente as dependências de teste que usa, bastando depender do módulo de teste da sua camada.

<div class="tabela-centralizada" markdown="1">

| Módulo | Camada | Dependências |
| --- | --- | --- |
| `d5-framework-dominio-teste` | Domínio | Cucumber, JUnit (incluindo JUnit Platform Suite) e Mockito |
| `d5-framework-aplicacao-teste` | Aplicação | Cucumber, JUnit (incluindo JUnit Platform Suite) e Mockito |
| `d5-framework-infraestrutura-teste` | Infraestrutura | `spring-boot-starter-test`, `spring-boot-starter-data-jpa-test` e H2 |
| `d5-framework-ui-backend-teste` | UI | `spring-boot-starter-test` e `spring-security-test` |

</div>

Esses módulos não têm código próprio: são agregadores de dependências, publicados com `packaging` `pom`. Por isso, ao declará-los em um `pom.xml`, é necessário informar `<type>pom</type>`:

```xml
<dependency>
    <groupId>dev.sauloaraujo</groupId>
    <artifactId>d5-framework-dominio-teste</artifactId>
    <type>pom</type>
    <scope>test</scope>
</dependency>
```
