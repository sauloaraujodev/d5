import {
  createContext,
  useCallback,
  useContext,
  useEffect,
  useMemo,
  useRef,
  useState,
  type ReactNode,
} from 'react';
import { useLocation } from 'react-router';

export type TipoNotificacao = 'erro' | 'sucesso';

export interface Notificacao {
  id: number;
  tipo: TipoNotificacao;
  mensagem: string;
}

interface NotificacaoInterna {
  notificacao: Notificacao;
  restante: number;
}

interface Notificacoes {
  notificacoes: Notificacao[];
  /** `preservarPorNavegacoes` é quantas navegações a notificação sobrevive. */
  publicar: (tipo: TipoNotificacao, mensagem: string, preservarPorNavegacoes?: number) => void;
  remover: (id: number) => void;
  limparTodas: () => void;
}

const ContextoNotificacoes = createContext<Notificacoes | null>(null);

/** Precisa ficar dentro do roteador, porque as notificações envelhecem a cada navegação. */
export function NotificacoesProvider({ children }: { children: ReactNode }) {
  const [internas, definirInternas] = useState<NotificacaoInterna[]>([]);
  const proximoId = useRef(0);
  const { key } = useLocation();
  const ultimaChave = useRef(key);

  useEffect(() => {
    if (key === ultimaChave.current) {
      return;
    }
    ultimaChave.current = key;
    definirInternas((atuais) =>
      atuais
        .map((interna) => ({ ...interna, restante: interna.restante - 1 }))
        .filter((interna) => interna.restante >= 0),
    );
  }, [key]);

  const publicar = useCallback<Notificacoes['publicar']>(
    (tipo, mensagem, preservarPorNavegacoes = 1) => {
      const notificacao = { id: proximoId.current++, tipo, mensagem };
      definirInternas((atuais) => [...atuais, { notificacao, restante: preservarPorNavegacoes }]);
    },
    [],
  );
  const remover = useCallback((id: number) => {
    definirInternas((atuais) => atuais.filter((interna) => interna.notificacao.id !== id));
  }, []);
  const limparTodas = useCallback(() => definirInternas([]), []);

  const valor = useMemo(
    () => ({
      notificacoes: internas.map((interna) => interna.notificacao),
      publicar,
      remover,
      limparTodas,
    }),
    [internas, publicar, remover, limparTodas],
  );

  return <ContextoNotificacoes value={valor}>{children}</ContextoNotificacoes>;
}

export function useNotificacoes(): Notificacoes {
  const contexto = useContext(ContextoNotificacoes);
  if (!contexto) {
    throw new Error('useNotificacoes exige um NotificacoesProvider');
  }
  return contexto;
}
