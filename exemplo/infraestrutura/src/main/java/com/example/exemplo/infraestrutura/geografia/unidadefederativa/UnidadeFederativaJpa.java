package com.example.exemplo.infraestrutura.geografia.unidadefederativa;

import com.example.exemplo.dominio.geografia.unidadefederativa.UnidadeFederativa;
import com.example.exemplo.dominio.geografia.unidadefederativa.UnidadeFederativaCodigoIbge;
import com.example.exemplo.dominio.geografia.unidadefederativa.UnidadeFederativaSigla;

import dev.sauloaraujo.d5.framework.infraestrutura.IAgregadoJpa;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "UFI_UNIDADE_FEDERATIVA")
public class UnidadeFederativaJpa implements IAgregadoJpa<UnidadeFederativa> {
	@Id
	@Column(name = "UFI_ID", length = UnidadeFederativaCodigoIbge.TAMANHO, nullable = false)
	private String identificador;

	@Column(name = "UFI_SG", length = UnidadeFederativaSigla.TAMANHO, nullable = false)
	private String sigla;

	@Column(name = "UFI_NM", length = 50, nullable = false)
	private String nome;

	protected UnidadeFederativaJpa() {
	}

	public UnidadeFederativaJpa(String identificador, String sigla, String nome) {
		this.identificador = identificador;
		this.sigla = sigla;
		this.nome = nome;
	}

	public String getIdentificador() {
		return identificador;
	}

	public String getSigla() {
		return sigla;
	}

	public String getNome() {
		return nome;
	}
}