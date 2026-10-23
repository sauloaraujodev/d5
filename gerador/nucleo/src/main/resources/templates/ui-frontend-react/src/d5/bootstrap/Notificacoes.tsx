import { useEffect } from 'react';
import { Alert } from 'react-bootstrap';
import { useNotificacoes, type Notificacao, type TipoNotificacao } from '../notificacoes';

const DURACAO_SUCESSO_MS = 10000;

const VARIANTE_POR_TIPO: Record<TipoNotificacao, string> = {
  erro: 'danger',
  sucesso: 'success',
};

function NotificacaoItem({ notificacao, remover }: { notificacao: Notificacao; remover: (id: number) => void }) {
  const { id, tipo, mensagem } = notificacao;

  useEffect(() => {
    if (tipo !== 'sucesso') {
      return;
    }
    const temporizador = setTimeout(() => remover(id), DURACAO_SUCESSO_MS);
    return () => clearTimeout(temporizador);
  }, [id, tipo, remover]);

  return (
    <Alert variant={VARIANTE_POR_TIPO[tipo]} dismissible onClose={() => remover(id)}>
      {mensagem}
    </Alert>
  );
}

export function Notificacoes() {
  const { notificacoes, remover } = useNotificacoes();

  if (notificacoes.length === 0) {
    return null;
  }
  return (
    <div className="container mt-5">
      {notificacoes.map((notificacao) => (
        <NotificacaoItem key={notificacao.id} notificacao={notificacao} remover={remover} />
      ))}
    </div>
  );
}
