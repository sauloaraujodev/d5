package dev.sauloaraujo.d5.gerador.web;

import java.util.Objects;
import java.util.Set;

public class Formulario {
	private String groupId;
	private String nome;
	private Set<String> subdominios;
	private boolean framework = true;

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

	public Set<String> getSubdominios() {
		return subdominios;
	}

	public boolean isFramework() {
		return framework;
	}

	public void setFramework(boolean framework) {
		this.framework = framework;
	}
}