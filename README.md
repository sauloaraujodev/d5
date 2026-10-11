# D5

D5, trocadilho com as cinco letras D de **DDD**-**D**riven **D**evelopment, agiliza o desenvolvimento de aplicações web com DDD, arquitetura limpa, BDD, CQRS, programação defensiva, Spring Boot e Spring Data JPA.

Documentação: <https://d5.sauloaraujo.dev>

## Uso
O framework é publicado no [Maven Central](https://central.sonatype.com/namespace/dev.sauloaraujo) com `groupId` `dev.sauloaraujo` e requer Java 25.

Enquanto a versão for `1.0.0-SNAPSHOT`, declare também o repositório de snapshots no `pom.xml` do projeto raiz:

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

Os módulos `d5-framework-*-teste` e `d5-framework-bdd` têm `packaging` `pom`; ao depender deles, informe `<type>pom</type>`.

## Estrutura
- `framework`: módulos publicados no Maven Central.
- `exemplo`: aplicação de exemplo construída no tutorial.
- `gerador`: gerador de projetos D5.
- `documentacao`: fonte do site.

## Publicação
Somente o `framework` é publicado. Os módulos `exemplo` e `gerador` ficam fora do build com `-Dpublicar`:

```bash
mvn -Prelease -Dpublicar clean deploy -DskipTests
```

## Licença
[Apache License 2.0](LICENSE)
