package dev.sauloaraujo.d5.gerador.nucleo;

import java.util.Objects;

/**
 * Um subdomínio do domínio do projeto.
 *
 * @param nome      o nome, usado nos módulos Maven, nos pacotes e nos modelos
 * @param tipo      a classificação estratégica
 * @param descricao a descrição, que vai para a declaração do subdomínio no
 *                  modelo do Context Mapper
 */
public record Subdominio(String nome, TipoSubdominio tipo, String descricao) {
	public Subdominio {
		Objects.requireNonNull(nome);
		Objects.requireNonNull(tipo);
		Objects.requireNonNull(descricao);
		if (nome.isBlank()) {
			throw new IllegalArgumentException("O nome do subdomínio não pode estar em branco");
		}
		if (descricao.isBlank()) {
			throw new IllegalArgumentException("A descrição do subdomínio " + nome + " não pode estar em branco");
		}
	}
}
