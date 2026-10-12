package com.example.exemplo.aplicacao.geografia.municipio;

import java.util.Objects;

import com.example.exemplo.dominio.geografia.municipio.Municipio;
import com.example.exemplo.dominio.geografia.municipio.MunicipioCodigoIbge;
import com.example.exemplo.dominio.geografia.unidadefederativa.UnidadeFederativaCodigoIbge;

import dev.sauloaraujo.d5.framework.aplicacao.IProjecao;

public final class MunicipioProjecao implements IProjecao<Municipio> {
	private final MunicipioCodigoIbge identificador;
	private final String nome;
	private final UnidadeFederativaCodigoIbge unidadeFederativa;
	private final boolean extinto;

	public MunicipioProjecao(MunicipioCodigoIbge identificador, String nome,
			UnidadeFederativaCodigoIbge unidadeFederativa, boolean extinto) {
		this.identificador = Objects.requireNonNull(identificador);
		this.nome = Objects.requireNonNull(nome);
		this.unidadeFederativa = Objects.requireNonNull(unidadeFederativa);
		this.extinto = extinto;
	}

	public MunicipioProjecao(String identificador, String nome, String unidadeFederativa, boolean extinto) {
		this(new MunicipioCodigoIbge(identificador), nome, new UnidadeFederativaCodigoIbge(unidadeFederativa), extinto);
	}

	public MunicipioCodigoIbge getIdentificador() {
		return identificador;
	}

	public String getNome() {
		return nome;
	}

	public UnidadeFederativaCodigoIbge getUnidadeFederativa() {
		return unidadeFederativa;
	}

	public boolean isExtinto() {
		return extinto;
	}
}