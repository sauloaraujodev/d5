# Capturas do tutorial
Regenera as capturas do Swagger UI usadas nas lições do tutorial, em `site/src/main/mkdocs/docs/assets/tutorial/<lição>`.

```bash
site/capturas/capturar.sh
```

Sem argumentos, regenera as três lições; para regenerar apenas algumas, informe-as como argumentos — `1-unidade-federativa`, `2-municipio` ou `3-cliente`.

Para cada lição, `capturar.sh` empacota o projeto correspondente (`exemplo1`, `exemplo2` ou `exemplo`), sobe o jar do `ui-backend` na porta 8080 e executa `capturas.mjs`, que controla o Chrome sem interface através do DevTools Protocol: autentica pelo diálogo **Authorize**, executa as operações descritas na lição e grava cada tela com 1280×900 pixels.

Requer Java, Maven, Node.js 22 ou superior e Chrome. O Chrome é procurado nos locais usuais de Windows, macOS e Linux; defina a variável de ambiente `CHROME` para informar outro executável. Se uma captura falhar, a tela no momento da falha é gravada em `d5-capturas-erro.png`, no diretório temporário do sistema.

Ao alterar uma lição de forma que mude o que o Swagger UI exibe, ajuste o roteiro da lição em `capturas.mjs` e regenere suas capturas.
