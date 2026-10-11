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

## Nomenclatura
Nesta seção, `${Agregado}` é o nome de um agregado e `${Atributo}` é o nome de um atributo, ambos em `PascalCase`.

| Elemento | Nome |
| --- | --- |
| Interface | Prefixo `I` — `I${Agregado}Repositorio` |
| Classe abstrata | Sufixo `Abstrato`/`Abstrata` — `AgregadoServicoAbstrato` |
| Agregado | `${Agregado}` |
| Objeto de valor de um atributo do agregado | `${Agregado}${Atributo}` |
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
