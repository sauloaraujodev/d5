package dev.sauloaraujo.d5.framework.ui.seguranca;

import java.util.Objects;

public class AutenticacaoRecurso {
	private final String usuario;

	public AutenticacaoRecurso(String usuario) {
		this.usuario = Objects.requireNonNull(usuario);
	}

	public String getUsuario() {
		return usuario;
	}
}