import { Link, Outlet, useNavigate } from 'react-router';
import { NotificacoesProvider, sair, useAutenticacao } from '../d5';
import { Notificacoes } from '../d5/bootstrap/Notificacoes';
import { ReautenticacaoProvider } from '../d5/bootstrap/ReautenticacaoProvider';

export function App() {
  const { autenticado } = useAutenticacao();
  const navegar = useNavigate();

  async function aoSair() {
    await sair();
    navegar('/login');
  }

  return (
    <NotificacoesProvider>
      <ReautenticacaoProvider>
        <nav className="navbar navbar-expand-lg navbar-dark bg-dark shadow-sm">
          <div className="container">
            <Link className="navbar-brand d-flex align-items-center gap-2" to="/">
              <strong>${nome?cap_first}</strong>
            </Link>
            {autenticado && (
              <div className="navbar-collapse show">
                <ul className="navbar-nav">
                  {/* Um item de menu por agregado, protegido por autorizacoesDe('agregado')?.buscar */}
                </ul>
                <ul className="navbar-nav ms-auto">
                  <li className="nav-item">
                    <button className="btn btn-link nav-link" type="button" onClick={aoSair}>
                      Sair
                    </button>
                  </li>
                </ul>
              </div>
            )}
          </div>
        </nav>
        <Notificacoes />
        <Outlet />
      </ReautenticacaoProvider>
    </NotificacoesProvider>
  );
}
