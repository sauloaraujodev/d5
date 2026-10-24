import { useNotificacoes } from '../NotificacoesProvider';
import { NotificacaoItem } from './NotificacaoItem';

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
