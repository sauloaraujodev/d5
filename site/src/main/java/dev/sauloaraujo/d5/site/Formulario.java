package dev.sauloaraujo.d5.site;

import java.util.Objects;
import java.util.Set;

public class Formulario {
	private String groupId;
	private String nome;
	private String versao = "1.0.0-SNAPSHOT";
	private Set<String> subdominios;
	private boolean framework = true;
	private boolean agentes = true;

	public Formulario(String groupId, String nome, Set<String> subdominios) {
		this.groupId = Objects.requireNonNull(groupId);
		this.nome = Objects.requireNonNull(nome);
		this.subdominios = Objects.requireNonNull(subdominios);
	}

	public String getGroupId() {
		return groupId;
	}

	public String getNome() {
		return nome;
	}

	public String getVersao() {
		return versao;
	}

	public void setVersao(String versao) {
		this.versao = Objects.requireNonNull(versao);
	}

	public Set<String> getSubdominios() {
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
}