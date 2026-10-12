package com.example.exemplo.dominio.geografia.municipio;

import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.Validate;

import com.example.exemplo.dominio.geografia.unidadefederativa.UnidadeFederativaCodigoIbge;

import dev.sauloaraujo.d5.framework.dominio.IAgregado;

public final class Municipio implements IAgregado<MunicipioCodigoIbge> {
	private final MunicipioCodigoIbge identificador;
	private final String nome;
	private final boolean extinto;

	public Municipio(MunicipioCodigoIbge identificador, String nome) {
		this(identificador, nome, false);
	}

	private Municipio(MunicipioCodigoIbge identificador, String nome, boolean extinto) {
		this.identificador = Objects.requireNonNull(identificador);
		this.nome = Validate.notBlank(nome, "Nome do município em branco");
		this.extinto = extinto;
	}

	public static Municipio extinto(MunicipioCodigoIbge identificador, String nome) {
		return new Municipio(identificador, nome, true);
	}

	public Municipio extinguir() {
		return extinto(identificador, nome);
	}

	@Override
	public Optional<MunicipioCodigoIbge> getIdentificador() {
		return Optional.of(identificador);
	}

	public String getNome() {
		return nome;
	}

	public UnidadeFederativaCodigoIbge getUnidadeFederativa() {
		return identificador.getUnidadeFederativa();
	}

	public boolean isExtinto() {
		return extinto;
	}
}