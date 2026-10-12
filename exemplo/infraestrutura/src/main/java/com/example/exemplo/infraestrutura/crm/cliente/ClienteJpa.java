package com.example.exemplo.infraestrutura.crm.cliente;

import org.jspecify.annotations.Nullable;

import com.example.exemplo.dominio.crm.cliente.Cliente;
import com.example.exemplo.dominio.crm.cliente.ClienteCpf;
import com.example.exemplo.dominio.crm.cliente.EnderecoCep;
import com.example.exemplo.dominio.geografia.municipio.MunicipioCodigoIbge;

import dev.sauloaraujo.d5.framework.infraestrutura.IAgregadoJpa;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "CLI_CLIENTE")
public class ClienteJpa implements IAgregadoJpa<Cliente> {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "CLI_ID", updatable = false)
	private @Nullable Long identificador;

	@Column(name = "CLI_CPF", length = ClienteCpf.TAMANHO, nullable = false, unique = true)
	private String cpf;

	@Column(name = "CLI_NM", length = 100, nullable = false)
	private String nome;

	@Column(name = "CLI_LOGRADOURO", length = 100, nullable = false)
	private String logradouro;

	@Column(name = "CLI_NUMERO", length = 10, nullable = false)
	private String numero;

	@Column(name = "CLI_COMPLEMENTO", length = 50)
	private @Nullable String complemento;

	@Column(name = "CLI_BAIRRO", length = 50, nullable = false)
	private String bairro;

	@Column(name = "CLI_CEP", length = EnderecoCep.TAMANHO, nullable = false)
	private String cep;

	@Column(name = "CLI_MUN_ID", length = MunicipioCodigoIbge.TAMANHO, nullable = false)
	private String municipio;

	protected ClienteJpa() {
	}

	public ClienteJpa(@Nullable Long identificador, String cpf, String nome, String logradouro, String numero,
			@Nullable String complemento, String bairro, String cep, String municipio) {
		this.identificador = identificador;
		this.cpf = cpf;
		this.nome = nome;
		this.logradouro = logradouro;
		this.numero = numero;
		this.complemento = complemento;
		this.bairro = bairro;
		this.cep = cep;
		this.municipio = municipio;
	}

	public @Nullable Long getIdentificador() {
		return identificador;
	}

	public String getCpf() {
		return cpf;
	}

	public String getNome() {
		return nome;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public String getNumero() {
		return numero;
	}

	public @Nullable String getComplemento() {
		return complemento;
	}

	public String getBairro() {
		return bairro;
	}

	public String getCep() {
		return cep;
	}

	public String getMunicipio() {
		return municipio;
	}
}