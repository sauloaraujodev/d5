# DDD
DDD é uma sigla para [**D**omain-**D**riven **D**esign](https://www.domainlanguage.com/ddd/reference/){target="_blank"}, uma técnica de projeto de software guiado pelo domínio. 

## Projeto de software
Softwares são desenvolvidos para resolver problemas complexos. Projetar software consiste em:

- decompor o problema complexo que o software deve resolver em subproblemas mais simples;
- resolver os subproblemas;
- compor as soluções dos subproblemas na solução do problema original.

## Domínio
Domínio é a área de negócio e/ou conhecimento para a qual um software é desenvolvido. De maneira geral, domínios possuem muitos conceitos e é necessário decompô-los em subdomínios.

## Subdomínio
Subdomínios são subconjuntos coesos de conceitos de um domínio.

## Objeto de valor
Objetos de valor são conceitos que não possuem identidade. Por exemplo:

- CPF;
- CNPJ;
- ISBN;
- CEP;
- endereço, formado por logradouro, número, complemento, bairro, município, unidade federativa e CEP;
- telefone, formado por código de área e número.

No framework de D5, classes que representam objetos de valor implementam [`IObjetoValor`](framework/dominio.md#iobjetovalor).

## Entidade
Entidades são conceitos que possuem identidade, ou seja, um atributo imutável que identifica unicamente cada uma de suas instâncias. São exemplos de entidades:

- pessoa física, identificada por CPF;
- pessoa jurídica, identificada por CNPJ;
- livro, identificado por ISBN.

No framework de D5, classes que representam entidades implementam [`IEntidade`](framework/dominio.md#ientidade).

## Agregado
Agregados são conjuntos coesos de entidades e objetos de valor. A entidade mais importante de um agregado é chamada de entidade raiz.

No framework de D5, classes que representam entidades raiz de agregados implementam [`IAgregado`](framework/dominio.md#iagregado).

## Serviço
Sempre que possível, operações devem ser encapsuladas em métodos de classes que representam objetos de valor e entidades. Caso contrário, elas devem ser encapsuladas em serviços. Por exemplo:

- incluir pessoa física;
- alterar pessoa jurídica;
- excluir livro;
- realizar empréstimo de exemplar;
- devolver exemplar.

No framework de D5, classes que representam serviços implementam [`IAgregadoServico`](framework/dominio.md#iagregadoservico).

## Repositório
Serviços não acessam mecanismos de persistência (arquivos, bancos de dados, etc.) diretamente. Eles os acessam através de métodos de repositórios. Por exemplo, serviços incluem pessoas físicas, alteram dados de pessoas jurídicas, excluem livros e registram empréstimos e devoluções no mecanismo de persistência através de métodos de repositórios.

No framework de D5, classes que representam repositórios implementam [`IAgregadoRepositorio`](framework/dominio.md#iagregadorepositorio).