import { useEffect, useRef, useState, type ReactNode } from 'react';
import { registrarReautenticador } from '../api';
import { ReautenticacaoModal } from './ReautenticacaoModal';

interface Pedido {
  promessa: Promise<void>;
  resolver: () => void;
  rejeitar: (motivo: Error) => void;
}

/**
 * Implementação com Bootstrap do pedido de reautenticação da camada HTTP (`chamarApi`).
 * Garante um único modal por vez: chamadas concorrentes (várias requisições falhando com sessão
 * expirada ao mesmo tempo) compartilham o mesmo pedido em vez de abrir um modal cada.
 */
export function ReautenticacaoProvider({ children }: { children: ReactNode }) {
  const [aberto, definirAberto] = useState(false);
  const pedido = useRef<Pedido | null>(null);

  useEffect(
    () =>
      registrarReautenticador(() => {
        if (!pedido.current) {
          let resolver!: () => void;
          let rejeitar!: (motivo: Error) => void;
          const promessa = new Promise<void>((resolve, reject) => {
            resolver = resolve;
            rejeitar = reject;
          });
          pedido.current = { promessa, resolver, rejeitar };
          definirAberto(true);
        }
        return pedido.current.promessa;
      }),
    [],
  );

  function encerrar(acao: (atual: Pedido) => void) {
    const atual = pedido.current;
    pedido.current = null;
    definirAberto(false);
    if (atual) {
      acao(atual);
    }
  }

  return (
    <>
      {children}
      {aberto && (
        <ReautenticacaoModal
          aoConcluir={() => encerrar((atual) => atual.resolver())}
          aoCancelar={() =>
            encerrar((atual) => atual.rejeitar(new Error('Reautenticação cancelada.')))
          }
        />
      )}
    </>
  );
}
