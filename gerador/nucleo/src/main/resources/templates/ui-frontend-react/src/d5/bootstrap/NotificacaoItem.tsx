import { useEffect } from 'react';
import { Alert } from 'react-bootstrap';
import type { Notificacao, TipoNotificacao } from '../NotificacoesProvider';

const DURACAO_SUCESSO_MS = 10000;

const VARIANTE_POR_TIPO: Record<TipoNotificacao, string> = {
  erro: 'danger',
  sucesso: 'success',
};

export function NotificacaoItem({
  notificacao,
  remover,
}: {
  notificacao: Notificacao;
  remover: (id: number) => void;
}) {
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
