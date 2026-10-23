import { useMutation } from '@tanstack/react-query';
import { Button, Card, Form } from 'react-bootstrap';
import { useForm } from 'react-hook-form';
import { useNavigate, useSearchParams } from 'react-router';
import { entrar, mensagemErroAutenticacao, useNotificacoes } from '../../d5';

interface Credenciais {
  usuario: string;
  senha: string;
}

/** Só aceita retorno para um caminho da própria aplicação, nunca para outro site. */
function destinoDeRetorno(retorno: string | null): string {
  return retorno?.startsWith('/') && !retorno.startsWith('//') ? retorno : '/';
}

export function Login() {
  const navegar = useNavigate();
  const [parametros] = useSearchParams();
  const { publicar } = useNotificacoes();
  const {
    register,
    handleSubmit,
    formState: { isValid },
  } = useForm<Credenciais>({ mode: 'onChange' });

  const { mutate, isPending } = useMutation({
    mutationFn: ({ usuario, senha }: Credenciais) => entrar(usuario, senha),
    // replace evita que /login fique no histórico do navegador — senão um "voltar" retornaria
    // para a tela de login.
    onSuccess: () => navegar(destinoDeRetorno(parametros.get('retorno')), { replace: true }),
    onError: (erro) => publicar('erro', mensagemErroAutenticacao(erro), 0),
  });

  return (
    <div
      className="d-flex justify-content-center align-items-center"
      style={{ minHeight: '80vh' }}
    >
      <Card className="shadow-sm" style={{ width: '24rem' }}>
        <Card.Body>
          <h1 className="h4 mb-3 text-center">Entrar</h1>
          <Form onSubmit={handleSubmit((credenciais) => mutate(credenciais))} noValidate>
            <Form.Group className="mb-3" controlId="usuario">
              <Form.Label>Usuário</Form.Label>
              <Form.Control autoFocus {...register('usuario', { required: true })} />
            </Form.Group>
            <Form.Group className="mb-3" controlId="senha">
              <Form.Label>Senha</Form.Label>
              <Form.Control type="password" {...register('senha', { required: true })} />
            </Form.Group>
            <Button type="submit" className="w-100" disabled={!isValid || isPending}>
              Entrar
            </Button>
          </Form>
        </Card.Body>
      </Card>
    </div>
  );
}
