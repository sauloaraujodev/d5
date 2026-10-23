import { QueryClient } from '@tanstack/react-query';

/**
 * Instância única, fora de componentes, porque os loaders das rotas e a camada HTTP precisam
 * consultar o estado da autenticação sem passar pela árvore React.
 */
export const clienteConsultas = new QueryClient({
  defaultOptions: { queries: { retry: false } },
});

/** Chave da consulta que guarda o usuário autenticado (`null` quando anônimo). */
export const chaveAutenticacao = ['autenticacao'] as const;
