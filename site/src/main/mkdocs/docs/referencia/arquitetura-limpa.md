# Arquitetura limpa
A [arquitetura limpa](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html){target="_blank"} organiza o código em camadas, das regras de negócio, no centro, aos detalhes técnicos — banco de dados, framework web, interface com o usuário —, na periferia. Seu princípio é a **regra da dependência**: o código de uma camada só depende do código das camadas mais internas. Assim, as regras de negócio não mudam quando um detalhe técnico muda e podem ser testadas sem ele.

## Camadas
Uma aplicação D5 tem quatro camadas, cada uma com seus módulos Maven, nomeados conforme as [convenções](convencoes.md#maven), e com o módulo do [framework](framework/index.md) correspondente:

| Camada | Responsabilidade | Módulos da aplicação | Módulo do framework |
| --- | --- | --- | --- |
| Domínio | regras de negócio: [agregados](ddd.md#agregado), [objetos de valor](ddd.md#objeto-de-valor), [serviços](ddd.md#servico) e as interfaces dos [repositórios](ddd.md#repositorio) | `${aplicacao}-dominio-compartilhado` e um `${aplicacao}-dominio-${subdominio}` por [subdomínio](ddd.md#subdominio) | [`d5-framework-dominio`](framework/dominio.md) |
| Aplicação | o lado de consulta dos agregados, separado do lado de comando segundo o [CQRS](cqrs.md): projeções, consultas, serviços de aplicação e as interfaces dos repositórios de aplicação | `${aplicacao}-aplicacao` | [`d5-framework-aplicacao`](framework/aplicacao.md) |
| Infraestrutura | as implementações, com Spring Data JPA, dos repositórios declarados pelo domínio e pela aplicação | `${aplicacao}-infraestrutura` | [`d5-framework-infraestrutura`](framework/infraestrutura.md) |
| UI | a API REST que expõe os serviços (backend) e a interface com o usuário (frontend) | `${aplicacao}-ui-backend` e `${aplicacao}-ui-frontend` | [`d5-framework-ui-backend`](framework/ui-backend.md) e [`d5-framework-springdoc`](framework/springdoc.md) |

Os módulos de domínio e de aplicação, assim como `d5-framework-dominio` e `d5-framework-aplicacao`, não dependem do Spring nem do JPA. Por isso, os serviços de domínio são testados com dublês das interfaces de que dependem, sem banco de dados nem contexto do Spring.

## Regra da dependência
As dependências entre os módulos seguem a regra da dependência: a aplicação depende do domínio; a infraestrutura depende da aplicação — e, através dela, do domínio —, cujas interfaces implementa; e o backend depende da aplicação, cujos serviços expõe. Nenhum módulo de domínio ou de aplicação depende da infraestrutura ou da UI.

O backend depende da infraestrutura apenas em tempo de execução (`<scope>runtime</scope>`): o código do backend não pode usar as classes da infraestrutura, que só estão presentes quando a aplicação é executada, para fornecer as implementações dos repositórios.

## Inversão de dependência e pacotes `spi`
Um serviço de domínio precisa persistir agregados, mas o domínio não pode depender da infraestrutura. A solução é inverter a dependência: o domínio declara, como uma interface, o que precisa — o repositório do agregado —, e a infraestrutura implementa essa interface. Em tempo de execução, o [registro de beans](framework/comportamentos.md#registro-de-beans) injeta no serviço a implementação da infraestrutura, sem que o domínio a conheça.

Essas interfaces ficam em pacotes `spi`, sigla de *Service Provider Interface*: interfaces que uma camada usa e que outra camada provê. Assim, o pacote deixa claro quais interfaces são implementadas fora da camada que as declara; veja [Pacotes](convencoes.md#pacotes). Por exemplo:

- [`IAgregadoRepositorio`](framework/dominio.md#iagregadorepositorio), estendida pela interface do repositório de cada agregado, no domínio, e implementada na infraestrutura;
- [`IAgregadoAplicacaoRepositorio`](framework/aplicacao.md#iagregadoaplicacaorepositorio), estendida pela interface do repositório de aplicação de cada agregado, na aplicação, e implementada na infraestrutura;
- [`ISegurancaServico`](framework/dominio.md#isegurancaservico), declarada no domínio e implementada pelo próprio framework na infraestrutura, por [`SegurancaServico`](framework/infraestrutura.md#segurancaservico), sobre o Spring Security.
