package dev.sauloaraujo.d5.framework.ui.seguranca;

import java.util.Objects;

import org.springframework.web.bind.annotation.GetMapping;

import dev.sauloaraujo.d5.framework.dominio.spi.ISegurancaServico;

public abstract class AutenticacaoControladorAbstrato {
	protected final ISegurancaServico segurancaServico;

	protected AutenticacaoControladorAbstrato(ISegurancaServico segurancaServico) {
		this.segurancaServico = Objects.requireNonNull(segurancaServico);
	}

	@GetMapping
	public Object autenticacao() {
		return recurso();
	}

	protected Object recurso() {
		return new AutenticacaoRecurso(segurancaServico.getUsuario());
	}
}