import { createBrowserRouter, Navigate } from 'react-router';
import { exigirAutenticacao, RotaProtegida } from '../d5';
import { ErroRota } from '../d5/bootstrap/ErroRota';
import { App } from './App';

export const rotas = [
  {
    path: '/',
    Component: App,
    // Caminhos que não correspondem a nenhuma rota
    ErrorBoundary: ErroRota,
    children: [
      {
        path: 'login',
        lazy: async () => ({ Component: (await import('./login/Login')).Login }),
      },
      {
        // Adicione aqui as rotas de cada agregado, dentro desta rota protegida.
        Component: RotaProtegida,
        // Erros dos loaders das rotas filhas, exibidos dentro do layout, com o menu
        ErrorBoundary: ErroRota,
        loader: exigirAutenticacao,
        children: [
          { index: true, element: <Navigate to="/inicio" replace /> },
          {
            path: 'inicio',
            lazy: async () => ({ Component: (await import('./inicio/Inicio')).Inicio }),
          },
        ],
      },
    ],
  },
];

export const roteador = createBrowserRouter(rotas);
