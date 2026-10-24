import { render, screen } from '@testing-library/react';
import { createMemoryRouter } from 'react-router';
import { RouterProvider } from 'react-router/dom';
import { ErroHttp } from '../api';
import { ErroRota } from './ErroRota';

function renderizar(caminho: string) {
  const roteador = createMemoryRouter(
    [
      {
        path: '/',
        ErrorBoundary: ErroRota,
        children: [
          {
            path: 'municipio/:codigo/alteracao',
            loader: () => {
              throw new ErroHttp(404, { mensagem: 'Município inexistente.' });
            },
            Component: () => null,
          },
        ],
      },
    ],
    { initialEntries: [caminho] },
  );
  render(<RouterProvider router={roteador} />);
}

describe('ErroRota', () => {
  it('exibe a mensagem do erro lançado pelo loader', async () => {
    renderizar('/municipio/1/alteracao');

    expect(await screen.findByText('Município inexistente.')).toBeInTheDocument();
  });

  it('exibe que a página não foi encontrada para um caminho sem rota', async () => {
    renderizar('/inexistente');

    expect(await screen.findByText('Página não encontrada.')).toBeInTheDocument();
  });
});
