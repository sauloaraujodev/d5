package com.example.exemplo.dominio.crm.cliente;

import dev.sauloaraujo.d5.framework.dominio.impl.ObjetoValorRegexAbstrato;

public final class ClienteCpf extends ObjetoValorRegexAbstrato {
	public static final int TAMANHO = 11;

	private static final String FORMATO = "\\d{" + TAMANHO + "}";
	private static final String MENSAGEM = "O CPF do cliente deve conter " + TAMANHO + " dígitos";

	public ClienteCpf(String valor) {
		super(valor, FORMATO, MENSAGEM);
	}
}
