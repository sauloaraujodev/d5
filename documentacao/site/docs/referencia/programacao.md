# Programação defensiva
No contexto de uma linguagem orientada a objetos, [programação defensiva](https://en.wikipedia.org/wiki/Defensive_programming){target="_blank"} consiste em o código de uma classe não permitir que seus objetos sejam criados ou colocados em um estado inválido. As regras a seguir contribuem para que isso ocorra.

## Evite `null`
Todo pacote não vazio deve ser anotado com [`@NullMarked`](https://jspecify.dev/docs/user-guide/#nullmarked){target="_blank"}.

Em projetos que herdam de `d5-pai`, essa regra é verificada na compilação do código principal — o de teste não é verificado: o `nullability-maven-plugin` configura o [Error Prone](https://errorprone.info){target="_blank"} e o [NullAway](https://github.com/uber/NullAway){target="_blank"}, que fazem o build falhar quando uma classe está em um pacote não anotado com `@NullMarked` ou quando o código pode atribuir ou devolver `null` onde ele não é admitido.

Atributos, parâmetros ou tipos de retorno não obrigatórios devem ser do tipo [`Optional`](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/util/Optional.html){target="_blank"}.

## Atributos `final`
Atributos devem ser declarados como `final`.

## Validação de argumentos
Construtores e métodos devem validar seus argumentos com [`Objects.requireNonNull`](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/util/Objects.html){target="_blank"} e os métodos da classe [`Validate`](https://commons.apache.org/proper/commons-lang/apidocs/org/apache/commons/lang3/Validate.html){target="_blank"} da biblioteca [Commons Lang](https://commons.apache.org/proper/commons-lang/){target="_blank"}.

## Validação de estado
Métodos devem validar se o estado atual do objeto e da aplicação como um todo permitem sua execução. Em caso negativo, devem lançar [`IllegalStateException`](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/lang/IllegalStateException.html){target="_blank"}.

Os argumentos são validados antes do estado: um argumento inválido torna a execução impossível em qualquer estado, e validá-lo primeiro faz a exceção lançada apontar o motivo real da rejeição.