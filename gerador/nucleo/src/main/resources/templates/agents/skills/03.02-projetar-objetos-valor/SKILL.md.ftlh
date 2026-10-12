---
name: projetar-objetos-valor
description: Projetar objetos de valor
---
Conceitos que não possuem identidade são projetados como objetos de valor. Eles podem ser formados por um único atributo, por exemplo, CEP, CNPJ, CPF, e-mail e ISBN ou por mais de um atributo, por exemplo, endereço é formado por logradouro, número, complemento, bairro, município, unidade federativa e CEP.

Para evitar o antipadrão objetos anêmicos, regras de negócio que dependem apenas dos atributos de um objeto de valor e/ou de parâmetros de métodos são encapsuladas em métodos do objeto de valor. De maneira similar, conceitos que podem ser representados apenas por uma cadeia de caracteres, mas que possuem regras de negócio associadas, são projetados como objetos de valor. É o caso, por exemplo, do CPF, que é formado por 11 dígitos, sendo os últimos dois calculados a partir dos anteriores. Entretanto, se a única regra de negócio associada ao conceito for sua obrigatoriedade ou tamanho máximo, ele deve ser projetado como um atributo do tipo String.

Quando existe um conceito no domínio que identifica a entidade **E** (por exemplo, ISBN, CPF ou CNPJ), a chave é um objeto de valor natural, cujo nome é o do próprio conceito.

Para cada entidade **E** que não possui um conceito no domínio que a identifique, existe um objeto de valor artificial chamado **E**Identificador que possui um único atributo do tipo String chamado valor.

Objetos de valor são declarados no módulo do contexto delimitado que corresponde ao subdomínio **S** no qual eles foram identificados ou no módulo compartilhado, caso eles sejam referenciados em mais de um subdomínio.

Atributos opcionais são declarados `nullable`.

Coleções em que a ordem é importante são declaradas como `List`, caso contrário, como `Collection`.

Ver [projetar-entidades](../03.03-projetar-entidades/SKILL.md).