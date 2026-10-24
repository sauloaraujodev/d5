# ${nome?cap_first}

Frontend em React, gerado pelo D5. Usa Vite, TypeScript, React Router, TanStack Query, React Hook Form e React Bootstrap.

## Servidor de desenvolvimento

```bash
npm install
npm start
```

Abra `http://localhost:4200/`. As chamadas a `/backend` são encaminhadas para `http://localhost:8080` (veja `vite.config.ts`).

## Testes

```bash
npm test
```

## Build

```bash
npm run build
```

Os artefatos ficam em `dist/`.

## Organização

- `src/app`: a aplicação — layout, rotas (`rotas.tsx`) e uma pasta por funcionalidade.
- `src/d5`: infraestrutura independente de kit de UI — chamadas ao backend (`api.ts`), autenticação, rota protegida e notificações.
- `src/d5/bootstrap`: componentes que usam o React Bootstrap — notificações e o modal de reautenticação.

## Como a sessão funciona

- `chamarApi` envia o cookie de sessão e o token CSRF a toda chamada a `/backend`. Para consumir o backend, use `chamarApi` dentro de `useQuery`/`useMutation` (ou de loaders das rotas).
- Se a sessão expira durante o uso (401), `chamarApi` pede a reautenticação em um modal, sem sair da página, e repete a chamada. Se o usuário desiste, a sessão é encerrada e ele volta ao login.
- O estado da autenticação é uma consulta do TanStack Query; leia-o com `useAutenticacao()`.
- Rotas que exigem autenticação ficam dentro da rota `RotaProtegida` em `src/app/rotas.tsx`. Um erro lançado pelo loader de uma delas é exibido por `ErroRota`, sem sair do layout; `mensagemErro` extrai a mensagem respondida pelo backend.
