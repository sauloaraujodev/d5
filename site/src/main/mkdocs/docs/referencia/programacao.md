# Programação defensiva
No contexto de uma linguagem orientada a objetos, [programação defensiva](https://en.wikipedia.org/wiki/Defensive_programming){target="_blank"} consiste em o código de uma classe não permitir que seus objetos sejam criados ou colocados em um estado inválido. As regras a seguir contribuem para que isso ocorra.

As regras valem para o código principal, e não para o de teste. Um teste precisa, justamente, provocar situações inválidas — passar `null` para verificar que um construtor o rejeita, por exemplo —, e os frameworks de teste exigem atributos mutáveis: as step definitions do Cucumber guardam, entre um passo e outro, o resultado da operação e a exceção capturada, e os testes do JUnit recebem as dependências por `@Autowired` e as preparam em `@BeforeEach`.

As [entidades JPA](convencoes.md#entidades-jpa) são outra exceção às regras de `Optional`, de atributos `final` e de validação de argumentos. O JPA exige atributos não `final` e um construtor sem argumentos, e um atributo opcional da entidade é `null`, anotado com [`@Nullable`](https://jspecify.dev/docs/api/org/jspecify/annotations/Nullable.html){target="_blank"}. Suas restrições são verificadas pelo Bean Validation antes de cada gravação, e não pelo construtor; veja [Validação das entidades JPA](framework/comportamentos.md#validacao-das-entidades-jpa). As regras do domínio continuam garantidas pelo agregado e por seus objetos de valor, a partir dos quais a entidade JPA é criada.

## Evite `null`
Todo pacote não vazio deve ser anotado com [`@NullMarked`](https://jspecify.dev/docs/user-guide/#nullmarked){target="_blank"}.

Em projetos que herdam de `d5-pai`, essa regra é verificada na compilação do código principal — o de teste não é verificado: o `nullability-maven-plugin` configura o [Error Prone](https://errorprone.info){target="_blank"} e o [NullAway](https://github.com/uber/NullAway){target="_blank"}, que fazem o build falhar quando uma classe está em um pacote não anotado com `@NullMarked` ou quando o código pode atribuir ou devolver `null` onde ele não é admitido.

Atributos, parâmetros ou tipos de retorno não obrigatórios devem ser do tipo [`Optional`](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/util/Optional.html){target="_blank"}.

A exceção são os contratos de bibliotecas e frameworks que não admitem `Optional`: neles, um valor ausente é `null`, anotado com [`@Nullable`](https://jspecify.dev/docs/api/org/jspecify/annotations/Nullable.html){target="_blank"}. É o caso do parâmetro de `equals`, que sobrescreve `Object.equals`, como em [`IObjetoValor`](framework/dominio.md#iobjetovalor), e dos parâmetros das consultas Spring Data JPA declaradas com `@Query`, em que um filtro ausente é `null` — por isso, [`paraLike`](framework/infraestrutura.md#agregadoaplicacaorepositorioabstrato) devolve `null` na ausência de filtro.

## Atributos `final`
Atributos devem ser declarados como `final`.

## Validação de argumentos
Construtores e métodos devem validar seus argumentos com [`Objects.requireNonNull`](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/util/Objects.html){target="_blank"} e os métodos da classe [`Validate`](https://commons.apache.org/proper/commons-lang/apidocs/org/apache/commons/lang3/Validate.html){target="_blank"} da biblioteca [Commons Lang](https://commons.apache.org/proper/commons-lang/){target="_blank"}.

## Validação de estado
Métodos devem validar se o estado atual do objeto e da aplicação como um todo permitem sua execução. Em caso negativo, devem lançar [`IllegalStateException`](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/lang/IllegalStateException.html){target="_blank"}.

Os argumentos são validados antes do estado: um argumento inválido torna a execução impossível em qualquer estado, e validá-lo primeiro faz a exceção lançada apontar o motivo real da rejeição. A exceção é a existência do objeto sobre o qual a operação age: se ele não existe, a operação fica sem objeto, e essa ausência é o motivo real da rejeição, ainda que os argumentos também sejam inválidos. Por isso, na alteração de um agregado, [`AgregadoServicoAbstrato`](framework/dominio.md#agregadoservicoabstrato) verifica a existência antes de validar o agregado recebido.