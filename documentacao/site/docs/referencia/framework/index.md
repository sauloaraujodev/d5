# Framework

## Módulos principais
O framework possui módulos para as camadas de domínio, aplicação, infraestrutura e UI.

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

| Módulo | Camada |
| --- | --- |
| `d5-framework-dominio-teste` | Domínio |
| `d5-framework-aplicacao-teste` | Aplicação |
| `d5-framework-infraestrutura-teste` | Infraestrutura |
| `d5-framework-ui-backend-teste` | UI |

</div>