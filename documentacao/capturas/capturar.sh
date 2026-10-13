#!/usr/bin/env bash
# Regenera as capturas do Swagger UI usadas no tutorial.
#
# Uso: documentacao/capturas/capturar.sh [lição...]
# Sem argumentos, regenera as três lições: 1-unidade-federativa, 2-municipio e 3-cliente.
#
# Para cada lição, empacota o projeto correspondente (exemplo1, exemplo2 ou exemplo), sobe o jar do
# ui-backend na porta 8080 e grava as capturas em documentacao/site/docs/assets/tutorial/<lição>.
# Requer Java, Maven, Node.js 22 ou superior e Chrome; veja capturas.mjs.
set -euo pipefail

DIRETORIO="$(cd "$(dirname "$0")" && pwd)"
RAIZ="$(cd "$DIRETORIO/../.." && pwd)"
ASSETS="$RAIZ/documentacao/site/docs/assets/tutorial"

exemplo_da_licao() {
	case "$1" in
	1-unidade-federativa) echo exemplo1 ;;
	2-municipio) echo exemplo2 ;;
	3-cliente) echo exemplo ;;
	*) echo "Lição desconhecida: $1" >&2; exit 1 ;;
	esac
}

capturar() {
	local licao="$1"
	local exemplo
	exemplo="$(exemplo_da_licao "$licao")"
	local log
	log="$(mktemp)"

	echo "== $licao ($exemplo)"
	# Empacota no reactor do próprio exemplo: as três versões têm as mesmas coordenadas Maven, e o
	# spring-boot:run usaria os módulos instalados no repositório local, que podem ser de outra versão.
	mvn -q -f "$RAIZ/$exemplo" package -DskipTests

	java -jar "$RAIZ/$exemplo/ui-backend/target/exemplo-ui-backend-1.0.0-SNAPSHOT.jar" > "$log" 2>&1 &
	local aplicacao=$!
	trap "kill $aplicacao 2>/dev/null || true" RETURN

	for _ in $(seq 1 120); do
		grep -q "Started BackendAplicacao" "$log" && break
		if ! kill -0 "$aplicacao" 2>/dev/null; then
			echo "A aplicação terminou antes de subir:" >&2
			tail -20 "$log" >&2
			return 1
		fi
		sleep 1
	done

	rm -rf "${ASSETS:?}/$licao"
	node "$DIRETORIO/capturas.mjs" "$licao" "$ASSETS/$licao"
}

if [ $# -eq 0 ]; then
	set -- 1-unidade-federativa 2-municipio 3-cliente
fi

for licao in "$@"; do
	capturar "$licao"
done
