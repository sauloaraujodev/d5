# Convenções
Nas convenções a seguir, `${aplicacao}` é o nome da aplicação e `${subdominio}` é o nome de um subdomínio.

## Maven
| Módulo | `artifactId` | Diretório |
| --- | --- | --- |
| Projeto raiz | `${aplicacao}-pai` | `${aplicacao}` |
| Domínio compartilhado | `${aplicacao}-dominio-compartilhado` | `${aplicacao}/dominio-compartilhado` |
| Domínio de um subdomínio | `${aplicacao}-dominio-${subdominio}` | `${aplicacao}/dominio-${subdominio}` |
| Infraestrutura | `${aplicacao}-infraestrutura` | `${aplicacao}/infraestrutura` |
| Aplicação | `${aplicacao}-aplicacao` | `${aplicacao}/aplicacao` |
| UI — backend | `${aplicacao}-ui-backend` | `${aplicacao}/ui-backend` |
| UI — frontend | `${aplicacao}-ui-frontend` | `${aplicacao}/ui-frontend` |

### Dependências entre módulos de domínio
O domínio compartilhado contém apenas os conceitos que não pertencem a nenhum subdomínio específico e são comuns a todos eles — como os papéis de autorização. Ser usado por mais de um subdomínio não basta: um conceito que pertence a um subdomínio fica no módulo dele, mesmo que outros subdomínios o usem.

Todo módulo de subdomínio depende do domínio compartilhado. Um módulo de subdomínio pode depender também de outro, quando seus conceitos referenciam os do outro, desde que as dependências entre os módulos de subdomínio não formem um ciclo.

## Pacotes
Nesta seção, `${groupId}` é o `groupId` do projeto raiz e de seus módulos. Já `${agregado}` é o nome de um agregado.

| Elemento | Módulo | Pacote |
| --- | --- | --- |
| Agregado `${agregado}` e seus objetos de valor | `${aplicacao}-dominio-${subdominio}` | `${groupId}.${aplicacao}.dominio.${subdominio}.${agregado}` |
| Interface do repositório de um agregado `${agregado}` | `${aplicacao}-dominio-${subdominio}` | `${groupId}.${aplicacao}.dominio.${subdominio}.${agregado}.spi` |
| Papéis de autorização, compartilhados por todos os subdomínios | `${aplicacao}-dominio-compartilhado` | `${groupId}.${aplicacao}.dominio` |
| Projeção, ordenação, consulta e serviço de aplicação de um agregado `${agregado}` | `${aplicacao}-aplicacao` | `${groupId}.${aplicacao}.aplicacao.${subdominio}.${agregado}` |
| Interface do repositório de aplicação de um agregado `${agregado}` | `${aplicacao}-aplicacao` | `${groupId}.${aplicacao}.aplicacao.${subdominio}.${agregado}.spi` |
| Implementação do repositório, entidade JPA e interface Spring Data JPA de um agregado `${agregado}` | `${aplicacao}-infraestrutura` | `${groupId}.${aplicacao}.infraestrutura.${subdominio}.${agregado}` |
| Implementação do repositório de aplicação de um agregado `${agregado}` | `${aplicacao}-infraestrutura` | `${groupId}.${aplicacao}.infraestrutura.${subdominio}.${agregado}` |
| Controlador REST de um agregado `${agregado}` | `${aplicacao}-ui-backend` | `${groupId}.${aplicacao}.ui.backend.${subdominio}.${agregado}` |
| Configuração de segurança e controlador de autenticação | `${aplicacao}-ui-backend` | `${groupId}.${aplicacao}.ui.backend.seguranca` |
| Classe com o `main` da aplicação Spring Boot | `${aplicacao}-ui-backend` | `${groupId}.${aplicacao}` |
| Suíte do Cucumber de um subdomínio | `${aplicacao}-dominio-${subdominio}`, em `src/test/java` | `${groupId}.${aplicacao}.dominio.${subdominio}` |
| Step definitions de um agregado `${agregado}` | `${aplicacao}-dominio-${subdominio}`, em `src/test/java` | `${groupId}.${aplicacao}.dominio.${subdominio}.${agregado}` |
| Testes do repositório e do repositório de aplicação de um agregado `${agregado}` | `${aplicacao}-infraestrutura`, em `src/test/java` | `${groupId}.${aplicacao}.infraestrutura.${subdominio}.${agregado}` |
| Configuração Spring Boot dos testes de infraestrutura | `${aplicacao}-infraestrutura`, em `src/test/java` | `${groupId}.${aplicacao}.infraestrutura` |

## Caminhos
Nesta seção, `${agregado-hifenizado}` é o nome de um agregado em minúsculas, com as palavras separadas por hífen — por exemplo, `unidade-federativa`.

| Elemento | Caminho |
| --- | --- |
| Controlador REST de um agregado | `@RequestMapping(Backend.PREFIXO + "${subdominio}/${agregado-hifenizado}")` |
| Controlador de autenticação | `@RequestMapping(Backend.PREFIXO + "autenticacao")` |
| Cenários Gherkin de um agregado | `${aplicacao}/dominio-${subdominio}/src/test/resources/features/${agregado-hifenizado}.feature` |

## Nomenclatura
Nesta seção, `${Agregado}` é o nome de um agregado, em `PascalCase`.

| Elemento | Nome |
| --- | --- |
| Interface | Prefixo `I` — `I${Agregado}Repositorio` |
| Classe abstrata | Sufixo `Abstrato`/`Abstrata` — `AgregadoServicoAbstrato` |
| Agregado | `${Agregado}` |
| Serviço de domínio | `${Agregado}Servico` |
| Repositório de domínio (interface e implementação) | `I${Agregado}Repositorio` e `${Agregado}Repositorio` |
| Serviço de aplicação | `${Agregado}AplicacaoServico` |
| Repositório de aplicação (interface e implementação) | `I${Agregado}AplicacaoRepositorio` e `${Agregado}AplicacaoRepositorio` |
| Consulta | `${Agregado}Consulta` |
| Ordenação | `${Agregado}Ordenacao` |
| Projeção | `${Agregado}Projecao` |
| Entidade JPA | `${Agregado}Jpa` |
| Repositório Spring Data JPA | `I${Agregado}RepositorioJpa` |
| Controlador | `${Agregado}Controlador` |
| Papéis de autorização (`enum`) | `Autorizacao` |
| Configuração de segurança | `SegurancaConfiguracao` |
| Controlador de autenticação | `AutenticacaoControlador` |
| Classe com o `main` da aplicação Spring Boot | `BackendAplicacao` |
| Suíte do Cucumber | `CucumberTests` |
| Step definitions | `${Agregado}StepDefinitions` |
| Testes do repositório e do repositório de aplicação | `${Agregado}RepositorioTest` e `${Agregado}AplicacaoRepositorioTest` |
| Configuração Spring Boot dos testes de infraestrutura | `InfraestruturaTesteAplicacao` |

## Entidades JPA
As restrições de uma coluna que o Bean Validation consegue expressar são declaradas com suas anotações — `@NotNull` em vez de `nullable = false` e `@Size(max = ...)` em vez de `length` —, e a `@Column` declara apenas o que elas não expressam, como `name`, `unique` e `updatable`. Assim, o Hibernate usa as mesmas anotações para gerar o esquema e para validar a entidade antes de gravá-la; veja [Validação das entidades JPA](framework/comportamentos.md#validacao-das-entidades-jpa).

Essas anotações expressam apenas restrições da persistência, como o tamanho de uma coluna. Regras do domínio — como um nome não poder estar em branco ou um código seguir um formato — ficam no agregado e em seus objetos de valor e não são repetidas na entidade JPA.

Quando o tamanho de uma coluna é o de um objeto de valor, `@Size` usa a constante do objeto de valor — por exemplo, `@Size(max = UnidadeFederativaSigla.TAMANHO)`. Os demais tamanhos são escritos diretamente na anotação, sem constante.
