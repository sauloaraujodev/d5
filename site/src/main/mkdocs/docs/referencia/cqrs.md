# CQRS
CQRS é uma sigla para [**C**ommand **Q**uery **R**esponsibility **S**egregation](https://martinfowler.com/bliki/CQRS.html){target="_blank"}: a separação entre o modelo usado para alterar os dados — o lado de comando — e o modelo usado para consultá-los — o lado de consulta.

Um [agregado](ddd.md#agregado) existe para garantir as regras de negócio ao ser alterado. Uma pesquisa, porém, não altera nada, e carregar agregados inteiros apenas para listar alguns de seus atributos, filtrados, ordenados e paginados, seria ineficiente. Por isso, cada lado tem seu próprio modelo.

## Lado de comando
Fica na camada de domínio; veja [Arquitetura limpa](arquitetura-limpa.md#camadas). As operações que alteram um agregado — incluir, alterar e excluir — passam pelo seu [serviço](ddd.md#servico), que verifica as autorizações e as regras de negócio, e pelo seu [repositório](ddd.md#repositorio), que converte o agregado de e para a sua representação persistida.

No framework de D5, o lado de comando é realizado por [`IAgregadoServico`](framework/dominio.md#iagregadoservico) e [`IAgregadoRepositorio`](framework/dominio.md#iagregadorepositorio). `IAgregadoServico` também expõe `buscar`, que devolve um agregado inteiro a partir do seu identificador — por exemplo, para preencher o formulário de alteração ou de confirmação da exclusão.

## Lado de consulta
Fica na camada de aplicação. A pesquisa devolve [projeções](framework/aplicacao.md#iprojecao) — objetos somente leitura com apenas os atributos de que o cliente precisa —, obtidas diretamente do banco de dados por uma consulta que filtra, ordena e pagina os registros, sem passar pelo agregado.

No framework de D5, o lado de consulta é realizado pelos tipos de [`d5-framework-aplicacao`](framework/aplicacao.md): a [consulta](framework/aplicacao.md#iconsulta), com os parâmetros da pesquisa, o [serviço de aplicação](framework/aplicacao.md#iagregadoaplicacaoservico) e o [repositório de aplicação](framework/aplicacao.md#iagregadoaplicacaorepositorio). O serviço de aplicação recorre ao serviço do lado de comando apenas para verificar se o usuário está autorizado a buscar o agregado.

Os dois lados usam o mesmo banco de dados: o que é separado são os modelos e as classes que os manipulam, e não o armazenamento. O backend reúne os dois lados em um único controlador, [`AgregadoControladorAbstrato`](framework/ui-backend.md#agregadocontroladorabstrato), que recebe o serviço e o serviço de aplicação do agregado.
