# Framework

## Módulos principais
O framework possui módulos para as camadas de domínio, aplicação, infraestrutura e UI.

Todos têm `groupId` `dev.sauloaraujo` e estão no [Maven Central](https://central.sonatype.com/namespace/dev.sauloaraujo){target="_blank"}. Na versão `1.0.0-SNAPSHOT`, é preciso declarar o [repositório de snapshots](../../index.md#como-usar).

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
