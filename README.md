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

O `d5-pai`:

- herda de `spring-boot-starter-parent` 4.1.1 — portanto, a aplicação usa Spring Boot 4.1;
- gerencia as versões dos módulos do framework, que são declarados sem `<version>`;
- declara a dependência de [JSpecify](https://jspecify.dev) em todos os módulos e configura, com o `nullability-maven-plugin`, a verificação de nulidade do Error Prone e do NullAway em tempo de compilação: o build falha se uma classe do código principal estiver em um pacote sem `@NullMarked`.

Os módulos `d5-framework-*-teste` e `d5-framework-bdd` têm `packaging` `pom`; ao depender deles, informe `<type>pom</type>`.

## Estrutura
- `framework`: módulos publicados no Maven Central.
- `exemplo`: aplicação de exemplo construída no tutorial, no estado final, ao fim da lição 3.
- `exemplo1` e `exemplo2`: a mesma aplicação ao fim das lições 1 e 2 do tutorial. Ficam fora do build e são verificadas com `mvn -f exemplo1 verify` e `mvn -f exemplo2 verify`. O build da documentação interrompe-se se um arquivo divergir entre versões consecutivas sem que a lição o altere de propósito; veja `site/src/main/mkdocs/hooks/exemplos.py`.
- `gerador`: gerador de projetos D5.
- `site`: módulo `d5-site`, que constrói o site da documentação a partir de `site/src/main/mkdocs`. O build usa Docker (imagem `squidfunk/mkdocs-material`), pois executa o MkDocs em um contêiner; sem Docker, o módulo não é construído. No Windows, execute o Maven no WSL, onde o `docker` está disponível.

## Publicação
Somente o `d5-pai`, do `pom.xml` raiz, e os módulos de `framework` são publicados. A propriedade `publicacao` ativa o perfil de publicação e tira os módulos `exemplo`, `gerador` e `site` do build — use `-Dpublicacao`, e não `-Ppublicacao`, que ativa o perfil sem definir a propriedade:

```bash
mvn -Dpublicacao clean deploy -DskipTests
```

## Licença
[Apache License 2.0](LICENSE)
