# Documentação de D5
Este documento descreve cada página da documentação de D5.

## Convenções gerais
O leitor do tutorial sabe Java e Maven. Os demais conceitos usados por D5 — DDD, arquitetura limpa, CQRS, programação defensiva e o framework — são explicados nas páginas de referência; o tutorial só linka para elas.

Cada página usa H1 para o título e H2, H3... para as seções, refletindo a hierarquia do conteúdo.

Blocos de código incluem o arquivo-fonte real via snippet (`--8<--`) em vez de colar o código, para nunca ficarem desatualizados.

Por padrão, as páginas descrevem em tom direto e impessoal — as lições do tutorial são a exceção, com tom imperativo.

Ao adicionar, remover ou alterar uma interface, classe ou convenção do framework, a página de referência correspondente é atualizada no mesmo commit.

## index.md
Menciona os principais conceitos de engenharia de software e tecnologias utilizados em D5.

## tutorial/index.md
Apresenta os subdomínios da aplicação de exemplo e as lições do tutorial, indicando o que cada uma acrescenta e o diretório do repositório com o projeto ao final dela.

## tutorial/*.md (lições)
Em tom imperativo, cada lição descreve, passo a passo, como desenvolver um agregado da aplicação de exemplo com o framework de D5: `1-unidade-federativa.md`, `2-municipio.md` e `3-cliente.md`. Cada lição parte do projeto deixado pela anterior e explica apenas o que acrescenta ou altera.

Os snippets de cada lição incluem os arquivos do diretório com o projeto ao final dela — `exemplo1`, `exemplo2` e `exemplo`, respectivamente —, mas o título de cada bloco de código mostra o caminho em `exemplo`, onde o leitor desenvolve o projeto.

Um arquivo presente em duas versões consecutivas é idêntico nelas, exceto os que a lição seguinte altera de propósito, listados em `ALTERACOES`, no hook `site/hooks/exemplos.py`. O hook verifica essa regra a cada build da documentação e o interrompe quando ela é violada. Ao alterar um arquivo de uma versão, replique a alteração nas demais versões em que ele aparece igual; ao fazer uma lição alterar um arquivo da anterior, registre-o em `ALTERACOES`.

As capturas do Swagger UI de cada lição ficam em `assets/tutorial/<lição>` e são geradas por `documentacao/capturas/capturar.sh`, nunca manualmente; veja `documentacao/capturas/README.md`.

Deve explicar apenas o necessário acerca do framework de D5 para que o leitor entenda o código de exemplo completamente.

Conceitos de DDD são ser mencionados com links para eles em `referencia/ddd.md`.

Convenções são mencionadas com links para elas em `referencia/convencoes.md`.

Práticas de programação defensiva são mencionadas com links para elas em `referencia/programacao.md`.

Interfaces e classes do framework são mencionadas com links para elas em uma das páginas `referencia/framework/*.md`.

Comportamentos automáticos são mencionados com links para eles em `referencia/framework/comportamentos.md`.

## referencia/ddd.md
Descrever, sucintamente, os conceitos de DDD utilizados em D5.

## referencia/convencoes.md
Detalhar todas as convenções de D5.

## referencia/framework/index.md
Descreve, sucintamente, os módulos do framework de D5, exceto o módulo d5-framework-bdd, que não deve ser utilizado diretamente, por ser um detalhe de implementação.

## referencia/framework/*.md
Detalha as interfaces e classes do framework do módulo do framework de D5 associado à página. Todos os parâmetros de tipo, construtores, métodos públicos e protegidos são documentados. Contém links para uso dessas interfaces e classes no tutorial.

Quando uma interface ou classe realiza um conceito de DDD, a página menciona esse conceito com link para ele em `referencia/ddd.md`.

## referencia/framework/comportamentos.md
Detalha os comportamentos automáticos implementado pelo framework.

## referencia/programacao.md
Detalha as práticas de programação defensiva adotadas em D5.