import { Alert } from 'react-bootstrap';
import { isRouteErrorResponse, Link, useRouteError } from 'react-router';
import { mensagemErro } from '../api';

/**
 * Exibe, no lugar da rota, o erro lançado por seu loader (por exemplo, um 404 do backend para um
 * identificador inexistente) ou o de um caminho que não corresponde a nenhuma rota.
 */
export function ErroRota() {
  const erro = useRouteError();
  const mensagem =
    isRouteErrorResponse(erro) && erro.status === 404
      ? 'Página não encontrada.'
      : mensagemErro(erro);

  return (
    <div className="container mt-5">
      <Alert variant="danger">{mensagem}</Alert>
      <Link to="/">Voltar ao início</Link>
    </div>
  );
}
