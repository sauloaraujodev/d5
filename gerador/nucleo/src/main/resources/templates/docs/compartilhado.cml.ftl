Domain dominio {
	domainVisionStatement = "${descricao}"

<#list subdominios as subdominio>
	Subdomain ${subdominio.nome} {
		type = ${subdominio.tipo}
		domainVisionStatement = "${subdominio.descricao}"
	}
<#sep>

</#list>
}

BoundedContext compartilhado {
	Module compartilhado {
		Aggregate compartilhado {
			enum Autorizacao {
				ADMINISTRADOR, USUARIO
			}
		}
	}
}
