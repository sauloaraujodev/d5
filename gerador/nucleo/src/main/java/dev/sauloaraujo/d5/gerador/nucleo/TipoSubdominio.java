package dev.sauloaraujo.d5.gerador.nucleo;

/**
 * A classificação estratégica de um subdomínio, com os nomes usados pelo
 * Context Mapper no atributo type do Subdomain.
 */
public enum TipoSubdominio {
	CORE_DOMAIN("subdomínio principal"), SUPPORTING_DOMAIN("subdomínio de suporte"),
			GENERIC_SUBDOMAIN("subdomínio genérico");

	private final String rotulo;

	TipoSubdominio(String rotulo) {
		this.rotulo = rotulo;
	}

	public String getRotulo() {
		return rotulo;
	}
}
