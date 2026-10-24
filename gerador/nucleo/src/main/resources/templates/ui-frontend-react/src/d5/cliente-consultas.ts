import { QueryClient } from '@tanstack/react-query';

/**
 * Instância única, fora de componentes, porque os loaders das rotas e a camada HTTP precisam
 * consultar o estado da autenticação sem passar pela árvore React.
 */
export const clienteConsultas = new QueryClient({
  // O staleTime evita que o componente da rota busque de novo, ao montar, os dados que o loader
  // da rota acabou de buscar.
  defaultOptions: { queries: { retry: false, staleTime: 5000 } },
});

/** Chave da consulta que guarda o usuário autenticado (`null` quando anônimo). */
export const chaveAutenticacao = ['autenticacao'] as const;
