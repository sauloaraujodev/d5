---
name: programar-defensivamente
description: Programar defensivamente
---
Pacotes são anotados com org.jspecify.annotations.NullMarked.

Atributos e parâmetros opcionais, por exemplo, aqueles declarados como nullable nos modelos de projeto de software, são Optional.

Métodos que pesquisam um único objeto não retornam Optional, lançam NoSuchElementException.

Validações de que parâmetros não são nulos são feitas com Objects.requireNonNull.

Atributos imutáveis são final.

Construtores e métodos validam argumentos e, em caso negativo, lançam IllegalArgumentException.

Métodos lançam NoSuchElementException quando um argumento identifica um objeto inexistente.

Métodos validam se o estado do sistema permite suas execuções e, em caso negativo, lançam IllegalStateException.