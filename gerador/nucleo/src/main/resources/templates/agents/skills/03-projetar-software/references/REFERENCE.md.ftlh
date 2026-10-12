# docs/compartilhado/projeto.cml
```
Domain dominio {
    Subdomain subdominioA {
        type = CORE_DOMAIN
    }

    Subdomain subdominioB {
        type = SUPPORTING_DOMAIN
    }

    Subdomain subdominioC {
        type = GENERIC_SUBDOMAIN
    }
}

BoundedContext compartilhado {
    Module compartilhado {
    	Aggregate compartilhado {
			// artificial
			ValueObject EntidadeAIdentificador {
				String valor
			}
    	}
    }
}
```

# docs/subdominioA/projeto.cml
```
import "../compartilhado/projeto.cml"

ContextMap {
	contains subdominioA, compartilhado

	subdominioA Downstream-Upstream compartilhado
}

BoundedContext subdominioA implements subdominioA {
    Module subdominioA {
    	Aggregate EntidadeA {
    		Entity EntidadeA {
    			aggregateRoot
    			
    			-EntidadeAIdentificador identificador key
    			int atributoInt
    			long atributoLong
    			double atributoDouble
    			Date atributoDate
    			DateTime atributoDateTime
    			
	    		Repository EntidadeARepository {
	    			void incluir(@EntidadeA agregado);
	    			boolean existe(@EntidadeAIdentificador identificador);
					@EntidadeA buscar(@EntidadeAIdentificador identificador);
	    			void alterar(@EntidadeA agregado);
	    			void excluir(@EntidadeAIdentificador identificador);
				}
    		}
    		    		
    		Service EntidadeAServico {
    			void incluir(@EntidadeA agregado);
    			@EntidadeA buscar(@EntidadeAIdentificador identificador);
    			void alterar(@EntidadeA agregado);
    			void excluir(@EntidadeAIdentificador identificador);
    		}
    	}
    }
}
```

# docs/subdominioB/projeto.cml
```
import "../compartilhado/projeto.cml"

ContextMap {
	contains subdominioB, compartilhado

	subdominioB Downstream-Upstream compartilhado
}

BoundedContext subdominioB implements subdominioB {
    Module subdominioB {
    	Aggregate EntidadeB {
    		Entity EntidadeB {
    			aggregateRoot
    			
    			-EntidadeBCodigo codigo key
    			String nome

				// Associações
    			-List<EntidadeAIdentificador> entidadesA
    			
	    		Repository EntidadeBRepository {
	    			void incluir(@EntidadeB agregado);
	    			boolean existe(@EntidadeBCodigo identificador);
	    			@EntidadeB buscar(@EntidadeBCodigo identificador);
	    			void alterar(@EntidadeB agregado);
	    			void excluir(@EntidadeBCodigo identificador);
				}
    		}
    		
    		// natural
    		ValueObject EntidadeBCodigo {
    			String valor
    		}

    		Service EntidadeBServico {
    			void incluir(@EntidadeB agregado);
    			@EntidadeB buscar(@EntidadeBCodigo identificador);
    			void alterar(@EntidadeB agregado);
    			void excluir(@EntidadeBCodigo identificador);
    		}
    	}
    }
}
```

# docs/subdominioC/projeto.cml
```
import "../compartilhado/projeto.cml"

ContextMap {
	contains subdominioC, compartilhado

	subdominioC Downstream-Upstream compartilhado
}

BoundedContext subdominioC implements subdominioC {
    Module subdominioC {
    }
}
```

# docs/projeto.cml
```
import "compartilhado/projeto.cml"
import "subdominioA/projeto.cml"
import "subdominioB/projeto.cml"
import "subdominioC/projeto.cml"
```