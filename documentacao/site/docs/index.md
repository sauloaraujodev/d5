# D5
D5, trocadilho com as cinco letras D de **DDD**-**D**riven **D**evelopment, agiliza o desenvolvimento de aplicações web com:

- [DDD](referencia/ddd.md) (**D**omain-**D**riven **D**esign);
- [arquitetura limpa](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html){target="_blank"};
- [BDD (**B**ehaviour-**D**riven **D**evelopment)](https://cucumber.io/docs/bdd/){target="_blank"};
- CQRS ([**C**ommand **Q**uery **R**esponsibility **S**egregation](https://en.wikipedia.org/wiki/Command_Query_Responsibility_Segregation){target="_blank"});
- [programação defensiva](referencia/programacao.md);
- [Spring Boot](https://spring.io/projects/spring-boot){target="_blank"};
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa){target="_blank"}.

Siga o [tutorial](tutorial/index.md) para aprender a desenvolver com o framework de D5.

## Como usar
O framework é publicado no [Maven Central](https://central.sonatype.com/namespace/dev.sauloaraujo){target="_blank"}, com `groupId` `dev.sauloaraujo`, e requer Java 25. Enquanto a versão for `1.0.0-SNAPSHOT`, declare também o repositório de snapshots no `pom.xml` do projeto raiz:

```xml
<parent>
    <groupId>dev.sauloaraujo</groupId>
    <artifactId>d5-pai</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</parent>

<repositories>
    <repository>
        <id>central-snapshots</id>
        <url>https://central.sonatype.com/repository/maven-snapshots/</url>
        <releases>
            <enabled>false</enabled>
        </releases>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
    </repository>
</repositories>
```

O `d5-pai`:

- herda de `spring-boot-starter-parent` 4.1.1 — portanto, a aplicação usa Spring Boot 4.1;
- gerencia as versões dos módulos do framework, que são declarados sem `<version>`;
- declara a dependência de [JSpecify](https://jspecify.dev){target="_blank"} em todos os módulos e configura a verificação de nulidade em tempo de compilação; veja [Evite `null`](referencia/programacao.md#evite-null).

O [tutorial](tutorial/index.md) mostra o projeto completo.
