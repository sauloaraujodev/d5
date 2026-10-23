import { QueryClientProvider } from '@tanstack/react-query';
import { render, screen } from '@testing-library/react';
import { createMemoryRouter } from 'react-router';
import { RouterProvider } from 'react-router/dom';
import { clienteConsultas } from '../d5';
import { rotas } from './rotas';

function renderizar(caminho: string) {
  const roteador = createMemoryRouter(rotas, { initialEntries: [caminho] });
  render(
    <QueryClientProvider client={clienteConsultas}>
      <RouterProvider router={roteador} />
    </QueryClientProvider>,
  );
  return roteador;
}

describe('App', () => {
  afterEach(() => {
    clienteConsultas.clear();
    vi.unstubAllGlobals();
  });

  it('envia o usuário anônimo para o login', async () => {
    vi.stubGlobal('fetch', vi.fn(async () => new Response(null, { status: 401 })));

    const roteador = renderizar('/inicio');

    expect(await screen.findByRole('heading', { name: 'Entrar' })).toBeInTheDocument();
    expect(roteador.state.location.pathname).toBe('/login');
    expect(roteador.state.location.search).toBe('?retorno=%2Finicio');
    expect(document.querySelector('.navbar-brand')).toHaveTextContent('${nome?cap_first}');
  });

  it('mostra o início para o usuário autenticado', async () => {
    vi.stubGlobal(
      'fetch',
      vi.fn(async () => Response.json({ usuario: 'maria' })),
    );

    renderizar('/');

    expect(await screen.findByText('maria')).toBeInTheDocument();
    expect(screen.getByRole('button', { name: 'Sair' })).toBeInTheDocument();
  });
});
