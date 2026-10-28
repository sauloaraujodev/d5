// Validação e inclusão de subdomínios do formulário de gerador.md. O envio é um POST comum para /api/gerador.
// Os campos de cada subdomínio são nomeados subdominios[i].nome, .tipo e .descricao, e o índice i é
// renumerado quando um subdomínio é incluído ou excluído.
function iniciarGerador() {
	const formulario = document.getElementById("gerador")
	if (!formulario || formulario.dataset.iniciado) {
		return
	}
	formulario.dataset.iniciado = "true"
	const subdominios = document.getElementById("gerador-subdominios")
	const modelo = subdominios.firstElementChild.cloneNode(true)

	const campos = (bloco) => bloco.querySelectorAll('input[type="text"], select')

	const renumerar = () => {
		;[...subdominios.children].forEach((bloco, indice) => {
			campos(bloco).forEach((campo) => {
				campo.name = campo.name.replace(/^subdominios\[\d+\]/, `subdominios[${indice}]`)
			})
		})
	}

	const validar = (campo) => {
		const mensagem = campo.closest(".gerador-campo").querySelector(".gerador-erro")
		mensagem.dataset.formato ??= mensagem.innerHTML
		mensagem.innerHTML = campo.validity.valueMissing ? mensagem.dataset.vazio : mensagem.dataset.formato
		campo.classList.toggle("invalido", !campo.validity.valid)
		mensagem.classList.toggle("visivel", !campo.validity.valid)
		return campo.validity.valid
	}

	formulario.addEventListener("input", (evento) => {
		if (evento.target.classList.contains("invalido")) {
			validar(evento.target)
		}
	})

	formulario.addEventListener("click", (evento) => {
		const botao = evento.target.closest("button")
		if (!botao) {
			return
		}
		if (botao.classList.contains("gerador-incluir")) {
			const validos = [...campos(subdominios.lastElementChild)].map(validar)
			if (validos.includes(false)) {
				return
			}
			const novo = modelo.cloneNode(true)
			const remover = novo.querySelector("button")
			remover.textContent = "−"
			remover.title = "Excluir subdomínio"
			remover.className = "md-button gerador-excluir"
			campos(novo).forEach((campo) => {
				campo.value = ""
				campo.classList.remove("invalido")
			})
			novo.querySelectorAll(".gerador-erro").forEach((erro) => erro.classList.remove("visivel"))
			subdominios.append(novo)
			renumerar()
		} else if (botao.classList.contains("gerador-excluir")) {
			botao.closest(".gerador-subdominio").remove()
			renumerar()
		}
	})

	formulario.addEventListener("submit", (evento) => {
		const validos = [...campos(formulario)].map(validar)
		if (validos.includes(false)) {
			evento.preventDefault()
		}
	})
}

// O tema Material troca de página sem recarregar quando navigation.instant está ativo; document$ cobre os dois casos.
if (typeof document$ !== "undefined") {
	document$.subscribe(iniciarGerador)
} else {
	document.addEventListener("DOMContentLoaded", iniciarGerador)
}
