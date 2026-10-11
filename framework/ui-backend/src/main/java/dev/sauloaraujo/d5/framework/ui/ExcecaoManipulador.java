package dev.sauloaraujo.d5.framework.ui;

import java.util.Map;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import dev.sauloaraujo.d5.framework.dominio.spi.NaoAutenticadoExcecao;
import dev.sauloaraujo.d5.framework.dominio.spi.NaoAutorizadoExcecao;

@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class ExcecaoManipulador {
	private static final Logger LOG = LoggerFactory.getLogger(ExcecaoManipulador.class);

	@ExceptionHandler(AccessDeniedException.class)
	ResponseEntity<Object> tratarAcessoNegado(AccessDeniedException excecao) {
		return corpo(HttpStatus.FORBIDDEN, excecao);
	}

	@ExceptionHandler(NaoAutorizadoExcecao.class)
	ResponseEntity<Object> tratarAcessoNegado(NaoAutorizadoExcecao excecao) {
		return corpo(HttpStatus.FORBIDDEN, excecao);
	}

	@ExceptionHandler(AuthenticationException.class)
	ResponseEntity<Object> tratarNaoAutenticado(AuthenticationException excecao) {
		return corpo(HttpStatus.UNAUTHORIZED, excecao);
	}

	@ExceptionHandler(NaoAutenticadoExcecao.class)
	ResponseEntity<Object> tratarNaoAutenticado(NaoAutenticadoExcecao excecao) {
		return corpo(HttpStatus.UNAUTHORIZED, excecao);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	ResponseEntity<Object> tratarArgumentoInvalido(IllegalArgumentException excecao) {
		return corpo(HttpStatus.UNPROCESSABLE_CONTENT, excecao);
	}

	@ExceptionHandler(NullPointerException.class)
	ResponseEntity<Object> tratarValorAusente(NullPointerException excecao) {
		return corpo(HttpStatus.UNPROCESSABLE_CONTENT, excecao);
	}

	@ExceptionHandler(IllegalStateException.class)
	ResponseEntity<Object> tratarConflito(IllegalStateException excecao) {
		return corpo(HttpStatus.CONFLICT, excecao);
	}

	@ExceptionHandler(NoSuchElementException.class)
	ResponseEntity<Object> tratarInexistente(NoSuchElementException excecao) {
		return corpo(HttpStatus.NOT_FOUND, excecao);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	ResponseEntity<Object> tratarCorpoInvalido(HttpMessageNotReadableException excecao) {
		for (var causa = excecao.getCause(); causa != null; causa = causa.getCause()) {
			if (causa instanceof IllegalStateException illegalStateException) {
				return tratarConflito(illegalStateException);
			}

			if (causa instanceof NoSuchElementException noSuchElementException) {
				return tratarInexistente(noSuchElementException);
			}

			if (causa instanceof IllegalArgumentException illegalArgumentException) {
				return tratarArgumentoInvalido(illegalArgumentException);
			}

			if (causa instanceof NullPointerException nullPointerException) {
				return tratarValorAusente(nullPointerException);
			}
		}

		return ResponseEntity.badRequest().body(Map.of("mensagem", "Corpo da requisição inválido."));
	}

	@ExceptionHandler(Exception.class)
	ResponseEntity<Object> tratarExcecaoNaoMapeada(Exception excecao) {
		LOG.error("Erro não tratado", excecao);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(Map.of("mensagem", "Erro interno do servidor."));
	}

	private ResponseEntity<Object> corpo(HttpStatus status, Exception excecao) {
		var mensagem = excecao.getMessage();
		return ResponseEntity.status(status)
				.body(Map.of("mensagem", mensagem != null ? mensagem : status.getReasonPhrase()));
	}
}
