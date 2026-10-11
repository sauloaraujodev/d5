# Documentação de D5
Este documento descreve cada página da documentação de D5.

## Convenções gerais
O leitor do tutorial sabe Java e Maven. Os demais conceitos usados por D5 — DDD, arquitetura limpa, CQRS, programação defensiva e o framework — são explicados nas páginas de referência; o tutorial só linka para elas.

Cada página usa H1 para o título e H2, H3... para as seções, refletindo a hierarquia do conteúdo.

Blocos de código incluem o arquivo-fonte real via snippet (`--8<--`) em vez de colar o código, para nunca ficarem desatualizados.

Por padrão, as páginas descrevem em tom direto e impessoal — `tutorial.md` é a exceção, com tom imperativo.

Ao adicionar, remover ou alterar uma interface, classe ou convenção do framework, a página de referência correspondente é atualizada no mesmo commit.

## index.md
Menciona os principais conceitos de engenharia de software e tecnologias utilizados em D5.

## tutorial.md
Em tom imperativo, descreve, passo a passo, como desenvolver uma aplicação de exemplo com o framework de D5.

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