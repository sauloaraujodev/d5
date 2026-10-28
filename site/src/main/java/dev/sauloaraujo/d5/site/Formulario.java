package dev.sauloaraujo.d5.site;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import dev.sauloaraujo.d5.gerador.nucleo.Frontend;
import dev.sauloaraujo.d5.gerador.nucleo.Subdominio;

public class Formulario {
	private String groupId;
	private String nome;
	private String descricao;
	private String versao = "1.0.0-SNAPSHOT";
	private Set<Subdominio> subdominios;
	private boolean framework = true;
	private boolean agentes = true;
	private Frontend frontend = Frontend.ANGULAR;

	// Os subdomínios chegam como subdominios[0].nome, subdominios[0].tipo, etc.
	public Formulario(String groupId, String nome, String descricao, List<Subdominio> subdominios) {
		this.groupId = Objects.requireNonNull(groupId);
		this.nome = Objects.requireNonNull(nome);
		this.descricao = Objects.requireNonNull(descricao);
		this.subdominios = new LinkedHashSet<>(Objects.requireNonNull(subdominios));
	}

	public String getGroupId() {
		return groupId;
	}

	public String getNome() {
		return nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public String getVersao() {
		return versao;
	}

	public void setVersao(String versao) {
		this.versao = Objects.requireNonNull(versao);
	}

	public Set<Subdominio> getSubdominios() {
		return subdominios;
	}

	public boolean isFramework() {
		return framework;
	}

	public void setFramework(boolean framework) {
		this.framework = framework;
	}

	public boolean isAgentes() {
		return agentes;
	}

	public void setAgentes(boolean agentes) {
		this.agentes = agentes;
	}

	public Frontend getFrontend() {
		return frontend;
	}

	public void setFrontend(Frontend frontend) {
		this.frontend = Objects.requireNonNull(frontend);
	}
}
