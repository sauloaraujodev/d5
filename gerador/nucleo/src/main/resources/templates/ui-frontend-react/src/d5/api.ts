import { chaveAutenticacao, clienteConsultas } from './cliente-consultas';

const PREFIXO_BACKEND = '/backend/';
const NOME_COOKIE_CSRF = 'XSRF-TOKEN';
const NOME_HEADER_CSRF = 'X-XSRF-TOKEN';

/** Resposta do backend com status fora de 2xx; `corpo` é o JSON da resposta, quando houver. */
export class ErroHttp extends Error {
  readonly status: number;
  readonly corpo: unknown;

  constructor(status: number, corpo: unknown) {
    super(`O backend respondeu ${status}`);
    this.name = 'ErroHttp';
    this.status = status;
    this.corpo = corpo;
  }
}

/**
 * Mensagem para o usuário de um erro do backend, que responde `{ "mensagem": "..." }`, ou uma
 * mensagem genérica quando o erro não traz uma.
 */
export function mensagemErro(erro: unknown): string {
  const corpo = erro instanceof ErroHttp ? erro.corpo : null;
  if (typeof corpo === 'object' && corpo !== null && 'mensagem' in corpo) {
    if (typeof corpo.mensagem === 'string') {
      return corpo.mensagem;
    }
  }
  return 'Não foi possível concluir a operação. Tente novamente.';
}

export interface OpcoesApi {
  method?: 'GET' | 'POST' | 'PUT' | 'DELETE';
  /** Serializado como JSON. */
  corpo?: unknown;
  headers?: Record<string, string>;
  signal?: AbortSignal;
}

/**
 * Pedido de reautenticação: resolve quando o usuário se autentica de novo e rejeita quando ele
 * desiste. Quem sabe exibi-lo (o kit de UI escolhido pela aplicação) o registra em
 * `registrarReautenticador`.
 */
export type Reautenticador = () => Promise<void>;

let reautenticador: Reautenticador | null = null;

/** Devolve a função que desfaz o registro, própria para o retorno de um `useEffect`. */
export function registrarReautenticador(novo: Reautenticador): () => void {
  reautenticador = novo;
  return () => {
    if (reautenticador === novo) {
      reautenticador = null;
    }
  };
}

function lerCookie(nome: string): string | null {
  const correspondencia = document.cookie.match(new RegExp(`(?:^|; )${nome}=([^;]*)`));
  return correspondencia ? decodeURIComponent(correspondencia[1]!) : null;
}

/**
 * Lê o cookie CSRF a cada envio, em vez de guardá-lo — essencial na retentativa após
 * reautenticação, quando o token pode ter mudado.
 */
async function enviar<T>(caminho: string, opcoes: OpcoesApi): Promise<T> {
  const ehBackend = caminho.startsWith(PREFIXO_BACKEND);
  const headers: Record<string, string> = { Accept: 'application/json', ...opcoes.headers };
  let body: string | undefined;

  if (opcoes.corpo !== undefined) {
    headers['Content-Type'] = 'application/json';
    body = JSON.stringify(opcoes.corpo);
  }

  const tokenCsrf = ehBackend ? lerCookie(NOME_COOKIE_CSRF) : null;
  if (tokenCsrf) {
    headers[NOME_HEADER_CSRF] = tokenCsrf;
  }

  const resposta = await fetch(caminho, {
    method: opcoes.method ?? 'GET',
    headers,
    body,
    signal: opcoes.signal,
    credentials: ehBackend ? 'include' : 'same-origin',
  });

  const texto = await resposta.text();
  let conteudo: unknown;
  if (texto) {
    try {
      conteudo = JSON.parse(texto);
    } catch {
      conteudo = texto;
    }
  }

  if (!resposta.ok) {
    throw new ErroHttp(resposta.status, conteudo);
  }
  return conteudo as T;
}

/**
 * Chamada ao backend (`/backend/...`) com a sessão por cookie e o CSRF. Quando a sessão de um
 * usuário autenticado expira (401), pede a reautenticação sem sair da página e repete a chamada
 * — o que o usuário preencheu num formulário não se perde. Se ele desiste, a sessão é encerrada
 * e o erro original é lançado.
 */
export async function chamarApi<T = void>(caminho: string, opcoes: OpcoesApi = {}): Promise<T> {
  const estavaAutenticado = Boolean(clienteConsultas.getQueryData(chaveAutenticacao));
  // As chamadas de login/reautenticação carregam Authorization explícito e tratam o próprio
  // erro; jamais devem originar um novo ciclo de reautenticação, senão esperariam pelo mesmo
  // modal que as originou.
  const ehTentativaDeLogin = 'Authorization' in (opcoes.headers ?? {});

  try {
    return await enviar<T>(caminho, opcoes);
  } catch (erro) {
    if (
      ehTentativaDeLogin ||
      !estavaAutenticado ||
      !reautenticador ||
      !(erro instanceof ErroHttp) ||
      erro.status !== 401
    ) {
      throw erro;
    }

    try {
      await reautenticador();
    } catch {
      await enviar('/backend/sair', { method: 'POST' }).catch(() => undefined);
      clienteConsultas.setQueryData(chaveAutenticacao, null);
      throw erro;
    }

    // O login troca o token CSRF, mas a resposta do login só apaga o antigo — o novo só é de
    // fato gravado numa requisição GET seguinte. Por isso, antes de repetir a chamada original,
    // forçamos essa renovação com uma chamada leve e idempotente.
    await enviar('/backend/autenticacao', {});
    return enviar<T>(caminho, opcoes);
  }
}
