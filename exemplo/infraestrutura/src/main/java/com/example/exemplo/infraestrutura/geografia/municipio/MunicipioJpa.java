package com.example.exemplo.infraestrutura.geografia.municipio;

import com.example.exemplo.dominio.geografia.municipio.Municipio;
import com.example.exemplo.dominio.geografia.municipio.MunicipioCodigoIbge;
import com.example.exemplo.dominio.geografia.unidadefederativa.UnidadeFederativaCodigoIbge;

import dev.sauloaraujo.d5.framework.infraestrutura.IAgregadoJpa;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "MUN_MUNICIPIO", uniqueConstraints = @UniqueConstraint(columnNames = { "MUN_UFI_ID", "MUN_NM" }))
public class MunicipioJpa implements IAgregadoJpa<Municipio> {
	@Id
	@Column(name = "MUN_ID", length = MunicipioCodigoIbge.TAMANHO, nullable = false, updatable = false)
	private String identificador;

	@Column(name = "MUN_NM", length = 100, nullable = false)
	private String nome;

	@Column(name = "MUN_UFI_ID", length = UnidadeFederativaCodigoIbge.TAMANHO, nullable = false, updatable = false)
	private String unidadeFederativa;

	@Column(name = "MUN_EX", nullable = false)
	private boolean extinto;

	protected MunicipioJpa() {
	}

	public MunicipioJpa(String identificador, String nome, String unidadeFederativa, boolean extinto) {
		this.identificador = identificador;
		this.nome = nome;
		this.unidadeFederativa = unidadeFederativa;
		this.extinto = extinto;
	}

	public String getIdentificador() {
		return identificador;
	}

	public String getNome() {
		return nome;
	}

	public String getUnidadeFederativa() {
		return unidadeFederativa;
	}

	public boolean isExtinto() {
		return extinto;
	}
}