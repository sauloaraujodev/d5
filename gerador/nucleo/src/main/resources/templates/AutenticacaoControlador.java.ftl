package ${pacote}.ui.backend.seguranca;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.sauloaraujo.d5.framework.dominio.spi.ISegurancaServico;
import dev.sauloaraujo.d5.framework.ui.Backend;
import dev.sauloaraujo.d5.framework.ui.seguranca.AutenticacaoControladorAbstrato;

@RestController
@RequestMapping(Backend.PREFIXO + "autenticacao")
public class AutenticacaoControlador extends AutenticacaoControladorAbstrato {
	public AutenticacaoControlador(ISegurancaServico segurancaServico) {
		super(segurancaServico);
	}
}
