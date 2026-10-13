---
name: programar-defensivamente
description: Programar defensivamente
---
Estas regras valem para o código principal, e não para o de teste, que precisa provocar situações inválidas e guardar estado mutável entre passos e testes.

Entidades JPA são exceção às regras de Optional, de atributos final e de validação de argumentos: o JPA exige atributos não final e um construtor sem argumentos, e um atributo opcional da entidade é null, anotado com org.jspecify.annotations.Nullable.

Pacotes são anotados com org.jspecify.annotations.NullMarked.

Atributos e parâmetros opcionais, por exemplo, aqueles declarados como nullable nos modelos de projeto de software, são Optional.

Métodos que pesquisam um único objeto não retornam Optional, lançam NoSuchElementException.

Validações de que parâmetros não são nulos são feitas com Objects.requireNonNull.

Atributos imutáveis são final.

Construtores e métodos validam argumentos e, em caso negativo, lançam IllegalArgumentException.

Métodos lançam NoSuchElementException quando um argumento identifica um objeto inexistente.

Métodos validam se o estado do sistema permite suas execuções e, em caso negativo, lançam IllegalStateException.