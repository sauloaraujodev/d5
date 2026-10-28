import "../compartilhado/projeto.cml"

ContextMap {
	contains ${nome}, compartilhado

	${nome} Downstream-Upstream compartilhado
}

BoundedContext ${nome} implements ${nome} {
	Module ${nome} {
	}
}
