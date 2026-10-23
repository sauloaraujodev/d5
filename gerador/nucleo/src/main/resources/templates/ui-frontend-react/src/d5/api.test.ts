import { chamarApi, ErroHttp, registrarReautenticador } from './api';
import { chaveAutenticacao, clienteConsultas } from './cliente-consultas';

function resposta(status: number, corpo?: unknown) {
  return corpo === undefined
    ? new Response(null, { status })
    : Response.json(corpo, { status });
}

describe('chamarApi', () => {
  let fetchSimulado: ReturnType<typeof vi.fn>;

  beforeEach(() => {
    fetchSimulado = vi.fn();
    vi.stubGlobal('fetch', fetchSimulado);
    document.cookie = 'XSRF-TOKEN=token-1';
  });

  afterEach(() => {
    clienteConsultas.clear();
    vi.unstubAllGlobals();
    document.cookie = 'XSRF-TOKEN=; expires=Thu, 01 Jan 1970 00:00:00 GMT';
  });

  it('envia a sessão e o token CSRF nas chamadas ao backend', async () => {
    fetchSimulado.mockResolvedValue(resposta(200, { ok: true }));

    const retorno = await chamarApi('/backend/pais', { method: 'POST', corpo: { nome: 'Brasil' } });

    expect(retorno).toEqual({ ok: true });
    const [caminho, opcoes] = fetchSimulado.mock.calls[0]!;
    expect(caminho).toBe('/backend/pais');
    expect(opcoes.credentials).toBe('include');
    expect(opcoes.headers['X-XSRF-TOKEN']).toBe('token-1');
    expect(opcoes.body).toBe('{"nome":"Brasil"}');
  });

  it('lança ErroHttp com o corpo da resposta de erro', async () => {
    fetchSimulado.mockResolvedValue(resposta(422, { mensagem: 'inválido' }));

    await expect(chamarApi('/backend/pais')).rejects.toMatchObject({
      status: 422,
      corpo: { mensagem: 'inválido' },
    });
  });

  it('não pede reautenticação de quem não estava autenticado', async () => {
    const reautenticador = vi.fn();
    const desfazer = registrarReautenticador(reautenticador);
    fetchSimulado.mockResolvedValue(resposta(401));

    await expect(chamarApi('/backend/pais')).rejects.toBeInstanceOf(ErroHttp);
    expect(reautenticador).not.toHaveBeenCalled();
    desfazer();
  });

  it('reautentica, renova o token CSRF e repete a chamada após um 401', async () => {
    clienteConsultas.setQueryData(chaveAutenticacao, { usuario: 'maria' });
    const desfazer = registrarReautenticador(async () => {
      document.cookie = 'XSRF-TOKEN=token-2';
    });
    fetchSimulado
      .mockResolvedValueOnce(resposta(401))
      .mockResolvedValueOnce(resposta(200, { usuario: 'maria' }))
      .mockResolvedValueOnce(resposta(200, { ok: true }));

    const retorno = await chamarApi('/backend/pais', { method: 'POST', corpo: {} });

    expect(retorno).toEqual({ ok: true });
    expect(fetchSimulado.mock.calls.map(([caminho]) => caminho)).toEqual([
      '/backend/pais',
      '/backend/autenticacao',
      '/backend/pais',
    ]);
    expect(fetchSimulado.mock.calls[2]![1].headers['X-XSRF-TOKEN']).toBe('token-2');
    desfazer();
  });

  it('encerra a sessão quando o usuário desiste de reautenticar', async () => {
    clienteConsultas.setQueryData(chaveAutenticacao, { usuario: 'maria' });
    const desfazer = registrarReautenticador(() => Promise.reject(new Error('cancelada')));
    fetchSimulado.mockResolvedValue(resposta(401));

    await expect(chamarApi('/backend/pais')).rejects.toMatchObject({ status: 401 });

    expect(clienteConsultas.getQueryData(chaveAutenticacao)).toBeNull();
    expect(fetchSimulado.mock.calls.at(-1)![0]).toBe('/backend/sair');
    desfazer();
  });
});
