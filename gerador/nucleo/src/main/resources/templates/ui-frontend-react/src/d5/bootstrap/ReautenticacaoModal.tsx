import { useState } from 'react';
import { Alert, Button, Form, Modal } from 'react-bootstrap';
import { useForm } from 'react-hook-form';
import {
  autenticar,
  encerrarSessao,
  mensagemErroAutenticacao,
  usuarioAtual,
} from '../autenticacao';
import { chaveAutenticacao, clienteConsultas } from '../cliente-consultas';

interface Credenciais {
  usuario: string;
  senha: string;
}

export function ReautenticacaoModal({
  aoConcluir,
  aoCancelar,
}: {
  aoConcluir: () => void;
  aoCancelar: () => void;
}) {
  // Capturado na abertura do modal, antes de qualquer nova tentativa de login — impede que outra
  // pessoa reautentique com a própria conta e submeta, sem perceber, os dados que o usuário
  // original deixou preenchidos no formulário.
  const [usuarioAnterior] = useState(() => usuarioAtual()?.usuario ?? null);
  const [mensagemErro, definirMensagemErro] = useState<string | null>(null);
  const {
    register,
    handleSubmit,
    formState: { isValid, isSubmitting },
  } = useForm<Credenciais>({ mode: 'onChange' });

  async function entrar({ usuario, senha }: Credenciais) {
    definirMensagemErro(null);
    try {
      const autenticado = await autenticar(usuario, senha);

      if (usuarioAnterior && autenticado.usuario !== usuarioAnterior) {
        // A sessão no servidor já é da outra pessoa: encerra-a sem tocar no estado da aplicação,
        // para que a página do usuário original continue aberta.
        await encerrarSessao();
        definirMensagemErro(`Entre com o mesmo usuário que estava usando (${usuarioAnterior}).`);
        return;
      }

      clienteConsultas.setQueryData(chaveAutenticacao, autenticado);
      aoConcluir();
    } catch (erro) {
      definirMensagemErro(mensagemErroAutenticacao(erro));
    }
  }

  return (
    <Modal show backdrop="static" keyboard={false} centered>
      <Modal.Header>
        <Modal.Title as="h1" className="h5">
          Sessão expirada
        </Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <p className="text-muted small">
          Sua sessão expirou. Entre novamente para continuar de onde parou — o que você preencheu no
          formulário não será perdido.
        </p>
        <Form onSubmit={handleSubmit(entrar)} noValidate>
          {mensagemErro && (
            <Alert variant="danger" className="py-2">
              {mensagemErro}
            </Alert>
          )}
          <Form.Group className="mb-3" controlId="reautenticacao-usuario">
            <Form.Label>Usuário</Form.Label>
            <Form.Control
              autoFocus
              isInvalid={mensagemErro !== null}
              {...register('usuario', { required: true })}
            />
          </Form.Group>
          <Form.Group className="mb-3" controlId="reautenticacao-senha">
            <Form.Label>Senha</Form.Label>
            <Form.Control
              type="password"
              isInvalid={mensagemErro !== null}
              {...register('senha', { required: true })}
            />
          </Form.Group>
          <div className="d-flex gap-2">
            <Button type="submit" className="flex-grow-1" disabled={!isValid || isSubmitting}>
              Entrar
            </Button>
            <Button variant="outline-secondary" onClick={aoCancelar}>
              Voltar
            </Button>
          </div>
        </Form>
      </Modal.Body>
    </Modal>
  );
}
