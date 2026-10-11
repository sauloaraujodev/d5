package com.example.exemplo.aplicacao.geografia.unidadefederativa;

import java.util.Objects;

import com.example.exemplo.dominio.geografia.unidadefederativa.UnidadeFederativa;
import com.example.exemplo.dominio.geografia.unidadefederativa.UnidadeFederativaCodigoIbge;

import dev.sauloaraujo.d5.framework.aplicacao.IProjecao;

public final class UnidadeFederativaProjecao implements IProjecao<UnidadeFederativa> {
	private final UnidadeFederativaCodigoIbge identificador;
	private final String nome;

	public UnidadeFederativaProjecao(UnidadeFederativaCodigoIbge identificador, String nome) {
		this.identificador = Objects.requireNonNull(identificador);
		this.nome = Objects.requireNonNull(nome);
	}

	public UnidadeFederativaProjecao(String identificador, String nome) {
		this(new UnidadeFederativaCodigoIbge(identificador), nome);
	}

	public UnidadeFederativaCodigoIbge getIdentificador() {
		return identificador;
	}

	public String getNome() {
		return nome;
	}
}