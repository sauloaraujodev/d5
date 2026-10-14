---
name: desenvolver-frontend
description: Desenvolve em Angular as rotas e componentes de uma funcionalidade e especifica as operações do backend que eles usam. Use depois de desenvolver o backend.
---
Desenvolver o frontend de uma funcionalidade **F** associada a um agregado **A** de um subdomínio **S** consiste em:

- desenvolver as rotas e componentes através das quais usuários utilizam **F**;
- especificar as operações da API do backend que as rotas utilizam.

O nome de uma rota **R** é um substantivo singular, por exemplo, inclusao, alteracao, exclusao, pesquisa, visualizacao, etc.

Se **R** não está associada a uma instância específica de um agregado, seu caminho é /**S**/**A**/**R**. Caso contrário, seu caminho é /**S**/**A**/:**ID**/**R** onde **ID** é o identificador de **A**.

Para cada rota, existe uma operação GET com mesmo caminho da rota, que responde os dados que a rota exibe inicialmente e um resolvedor que executa essa operação e é utilizado pelo componente da rota.

O caminho de todas as operações é prefixado por /backend.

Para cada ação que pode ser executada a partir de uma rota, existe uma operação POST cujo caminho substitui o último segmento do caminho da rota pelo nome da ação, por exemplo, incluir, alterar, excluir, etc.

<#if d5Project.framework>
O framework D5 já fornece, para todo agregado **A**, as operações padrão de inclusão (GET /**S**/**A**/inclusao e POST /**S**/**A**/incluir), alteração (GET /**S**/**A**/:**ID**/alteracao e POST /**S**/**A**/:**ID**/alterar), exclusão (GET /**S**/**A**/:**ID**/exclusao e POST /**S**/**A**/:**ID**/excluir) e pesquisa (GET /**S**/**A**/pesquisa). Apenas rotas e ações além dessas (por exemplo, bloquear, desbloquear) precisam ter suas operações especificadas.

</#if>
Especificações de operações possuem exemplos.

Os estilos, template e código de um componente ficam em arquivos distintos.