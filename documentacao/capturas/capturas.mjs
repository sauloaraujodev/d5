// Captura as telas do Swagger UI usadas no tutorial, controlando o Chrome pelo DevTools Protocol.
// Não tem dependências: usa fetch e WebSocket nativos do Node.js 22 ou superior.
//
// Uso: node capturas.mjs <lição> <diretório de destino>
// A aplicação da lição deve estar em execução em $BASE (padrão: http://localhost:8080).
// O Chrome é procurado em locais usuais; defina $CHROME para informar outro executável.
import { spawn } from 'node:child_process';
import { existsSync, mkdirSync, rmSync, writeFileSync } from 'node:fs';
import { tmpdir } from 'node:os';
import { join } from 'node:path';

const [, , licao, destino] = process.argv;
const BASE = process.env.BASE ?? 'http://localhost:8080';
const PORTA_DEPURACAO = 9333;
const LARGURA = 1280;
const ALTURA = 900;

const CHROMES = [
	process.env.CHROME,
	'C:/Program Files/Google/Chrome/Application/chrome.exe',
	'C:/Program Files (x86)/Google/Chrome/Application/chrome.exe',
	'/Applications/Google Chrome.app/Contents/MacOS/Google Chrome',
	'/usr/bin/google-chrome',
	'/usr/bin/chromium',
	'/usr/bin/chromium-browser',
];
const chromeExecutavel = CHROMES.find(c => c && existsSync(c));
if (!chromeExecutavel) {
	throw new Error('Chrome não encontrado; defina a variável de ambiente CHROME');
}

const dormir = ms => new Promise(r => setTimeout(r, ms));
const perfil = join(tmpdir(), `d5-capturas-${Date.now()}`);
const chrome = spawn(chromeExecutavel, ['--headless=new', `--remote-debugging-port=${PORTA_DEPURACAO}`,
	'--no-first-run', '--no-default-browser-check', `--user-data-dir=${perfil}`,
	`--window-size=${LARGURA},${ALTURA}`, 'about:blank'], { stdio: 'ignore' });

async function urlDaPagina() {
	for (let i = 0; i < 100; i++) {
		try {
			const alvos = await (await fetch(`http://127.0.0.1:${PORTA_DEPURACAO}/json/list`)).json();
			const pagina = alvos.find(a => a.type === 'page');
			if (pagina) return pagina.webSocketDebuggerUrl;
		} catch { }
		await dormir(200);
	}
	throw new Error('O Chrome não respondeu');
}

const ws = new WebSocket(await urlDaPagina());
await new Promise((res, rej) => { ws.onopen = res; ws.onerror = rej; });
let ultimoId = 0;
const pendentes = new Map();
ws.onmessage = e => {
	const m = JSON.parse(e.data);
	if (m.id && pendentes.has(m.id)) {
		const { res, rej } = pendentes.get(m.id);
		pendentes.delete(m.id);
		m.error ? rej(new Error(JSON.stringify(m.error))) : res(m.result);
	}
};
const cdp = (method, params = {}) => new Promise((res, rej) => {
	const id = ++ultimoId;
	pendentes.set(id, { res, rej });
	ws.send(JSON.stringify({ id, method, params }));
});

async function avaliar(expressao) {
	const r = await cdp('Runtime.evaluate', { expression: expressao, awaitPromise: true, returnByValue: true });
	if (r.exceptionDetails) throw new Error(`${expressao}\n${JSON.stringify(r.exceptionDetails)}`);
	return r.result.value;
}

async function aguardar(expressao, ms = 15000) {
	for (let t = 0; t < ms; t += 200) {
		if (await avaliar(`!!(${expressao})`)) return;
		await dormir(200);
	}
	throw new Error(`Tempo esgotado: ${expressao}`);
}

const AUXILIARES = `
window.__op = (metodo, caminho) => [...document.querySelectorAll('.opblock.opblock-' + metodo)]
	.find(o => o.querySelector('.opblock-summary-path')?.getAttribute('data-path') === caminho);
window.__valor = (el, v) => {
	const proto = el.tagName === 'TEXTAREA' ? HTMLTextAreaElement.prototype : HTMLInputElement.prototype;
	// Limpa antes, porque o React ignora um evento que não muda o valor que ele conhece.
	for (const valor of ['', v]) {
		Object.getOwnPropertyDescriptor(proto, 'value').set.call(el, valor);
		el.dispatchEvent(new Event('input', { bubbles: true }));
		el.dispatchEvent(new Event('change', { bubbles: true }));
	}
};
window.__api = async (metodo, caminho, corpo) => {
	const token = decodeURIComponent((document.cookie.match(/XSRF-TOKEN=([^;]+)/) || [])[1] || '');
	const r = await fetch(caminho, { method: metodo, headers: { Authorization: 'Basic ' + btoa('a:a'),
		'X-XSRF-TOKEN': token, 'Content-Type': 'application/json' }, body: corpo && JSON.stringify(corpo) });
	return r.status;
};
`;

async function abrirSwagger() {
	await cdp('Page.enable');
	await cdp('Emulation.setDeviceMetricsOverride', { width: LARGURA, height: ALTURA, deviceScaleFactor: 1, mobile: false });
	await cdp('Page.navigate', { url: `${BASE}/swagger-ui/index.html` });
	await aguardar(`window.ui && document.querySelector('.opblock')`);
	// Obtém o cookie XSRF-TOKEN e recarrega, porque o Swagger UI só lê o cookie ao carregar a página.
	await avaliar(`fetch('/backend/autenticacao', { headers: { Authorization: 'Basic ' + btoa('a:a') } }).then(r => r.status)`);
	await cdp('Page.reload');
	await dormir(500);
	await aguardar(`window.ui && document.querySelector('.opblock')`);
	await avaliar(AUXILIARES);
}

// Autentica pelo diálogo Authorize, como o tutorial descreve, opcionalmente capturando o diálogo preenchido.
async function autorizar(captura) {
	await avaliar(`window.scrollTo(0, 0)`);
	await avaliar(`document.querySelector('.auth-wrapper .authorize').click()`);
	await aguardar(`document.querySelector('.modal-ux input[type=password]')`);
	await avaliar(`__valor(document.querySelector('.modal-ux input[type=text]'), 'a')`);
	await avaliar(`__valor(document.querySelector('.modal-ux input[type=password]'), 'a')`);
	await avaliar(`document.querySelector('.modal-ux input[type=password]').focus()`);
	await dormir(300);
	if (captura) await capturar(captura);
	await avaliar(`document.querySelector('.modal-ux .auth-btn-wrapper .authorize').click()`);
	await aguardar(`document.querySelector('.modal-ux .auth-btn-wrapper .btn-done')`);
	await avaliar(`document.querySelector('.modal-ux .auth-btn-wrapper .btn-done').click()`);
	await dormir(300);
}

const api = (metodo, caminho, corpo) => avaliar(`__api(${JSON.stringify(metodo)}, ${JSON.stringify(caminho)}, ${JSON.stringify(corpo)})`);

async function abrir(metodo, caminho) {
	const op = `__op('${metodo}', '${caminho}')`;
	await aguardar(op);
	if (!await avaliar(`${op}.classList.contains('is-open')`)) {
		await avaliar(`${op}.querySelector('.opblock-summary-control').click()`);
	}
	await aguardar(`${op}.querySelector('.opblock-body')`);
	const botao = `${op}.querySelector('.try-out__btn')`;
	await aguardar(botao);
	if (await avaliar(`${botao}.textContent.includes('Try it out')`)) {
		await avaliar(`${botao}.click()`);
	}
	await aguardar(`${botao}.textContent.includes('Cancel')`);
	await dormir(300);
	return op;
}

async function parametro(op, nome, valor) {
	await avaliar(`__valor(${op}.querySelector('tr[data-param-name="${nome}"] input'), ${JSON.stringify(valor)})`);
	// Os campos de parâmetro do Swagger UI só atualizam o estado depois de um debounce.
	await dormir(1000);
}

async function corpo(op, json) {
	await aguardar(`${op}.querySelector('textarea.body-param__text')`);
	await avaliar(`__valor(${op}.querySelector('textarea.body-param__text'), ${JSON.stringify(JSON.stringify(json, null, 2))})`);
	await dormir(500);
}

let ultimaOp = null;

async function executar(op) {
	ultimaOp = op;
	await avaliar(`${op}.querySelector('button.execute').click()`);
	await aguardar(`${op}.querySelector('.live-responses-table')`);
	await dormir(600);
}

async function rolar(expressao, margem = 0) {
	await avaliar(`(() => { const el = ${expressao}; el.scrollIntoView({ block: 'start' }); window.scrollBy(0, -${margem}); })()`);
	await dormir(300);
}

const rolarParaResposta = op => rolar(`${op}.querySelector('.responses-wrapper')`);

async function capturar(nome) {
	mkdirSync(destino, { recursive: true });
	const { data } = await cdp('Page.captureScreenshot', { format: 'png' });
	writeFileSync(join(destino, nome), Buffer.from(data, 'base64'));
	console.log('capturada', nome);
}

const UF = '/backend/geografia/unidade-federativa';
const MUNICIPIO = '/backend/geografia/municipio';
const CLIENTE = '/backend/crm/cliente';
const UF_SP = { identificador: '35', sigla: 'SP', nome: 'São Paulo' };
const UF_RJ = { identificador: '33', sigla: 'RJ', nome: 'Rio de Janeiro' };
const MUNICIPIO_SP = { identificador: '3550308', nome: 'São Paulo' };

const licoes = {
	async '1-unidade-federativa'() {
		await capturar('01-visao-geral.png');
		await autorizar('02-autorizar.png');

		let op = await abrir('post', `${UF}/incluir`);
		await corpo(op, UF_SP);
		await rolar(op);
		await capturar('03-incluir-corpo.png');
		await executar(op);
		await rolarParaResposta(op);
		await capturar('03-incluir-resposta.png');

		await api('POST', `${UF}/incluir`, UF_RJ);
		op = await abrir('get', `${UF}/pesquisa`);
		await parametro(op, 'nome', 'Paulo');
		await rolar(op);
		await capturar('04-pesquisa-parametros.png');
		await executar(op);
		await rolarParaResposta(op);
		await capturar('04-pesquisa-resposta.png');

		op = await abrir('get', `${UF}/{identificador}/alteracao`);
		await parametro(op, 'identificador', '35');
		await rolar(op);
		await capturar('05-alteracao-parametro.png');
		await executar(op);
		await rolarParaResposta(op);
		await capturar('05-alteracao-resposta.png');

		op = await abrir('post', `${UF}/{identificador}/alterar`);
		await parametro(op, 'identificador', '35');
		await corpo(op, { ...UF_SP, nome: 'São Paulo (alterado)' });
		await rolar(op);
		await capturar('06-alterar-corpo.png');
		await executar(op);
		await rolarParaResposta(op);
		await capturar('06-alterar-resposta.png');

		op = await abrir('get', `${UF}/{identificador}/exclusao`);
		await parametro(op, 'identificador', '35');
		await rolar(op);
		await capturar('07-exclusao-parametro.png');
		await executar(op);
		await rolarParaResposta(op);
		await capturar('07-exclusao-resposta.png');

		op = await abrir('post', `${UF}/{identificador}/excluir`);
		await parametro(op, 'identificador', '35');
		await rolar(op);
		await capturar('08-excluir-parametro.png');
		await executar(op);
		await rolarParaResposta(op);
		await capturar('08-excluir-resposta.png');
	},

	async '2-municipio'() {
		await autorizar();
		await api('POST', `${UF}/incluir`, UF_SP);
		await rolar(`document.querySelector('#operations-tag-municipio-controlador')`, 10);
		await capturar('01-visao-geral.png');

		let op = await abrir('post', `${MUNICIPIO}/incluir`);
		await corpo(op, MUNICIPIO_SP);
		await rolar(op);
		await capturar('02-incluir-corpo.png');
		await executar(op);

		op = await abrir('post', `${MUNICIPIO}/{identificador}/extinguir`);
		await parametro(op, 'identificador', '3550308');
		await rolar(op);
		await capturar('03-extinguir-parametro.png');
		await executar(op);

		op = await abrir('get', `${MUNICIPIO}/{identificador}/alteracao`);
		await parametro(op, 'identificador', '3550308');
		await executar(op);
		await rolarParaResposta(op);
		await capturar('04-alteracao-resposta.png');

		op = await abrir('post', `${UF}/{identificador}/excluir`);
		await parametro(op, 'identificador', '35');
		await executar(op);
		await rolarParaResposta(op);
		await capturar('05-excluir-unidade-federativa-resposta.png');
	},

	async '3-cliente'() {
		await autorizar();
		await api('POST', `${UF}/incluir`, UF_SP);
		await api('POST', `${MUNICIPIO}/incluir`, MUNICIPIO_SP);
		await rolar(`document.querySelector('#operations-tag-cliente-controlador')`, 10);
		await capturar('01-visao-geral.png');

		let op = await abrir('post', `${CLIENTE}/incluir`);
		await corpo(op, {
			cpf: '11111111111', nome: 'Maria', endereco: {
				logradouro: 'Avenida Paulista', numero: '1000', bairro: 'Bela Vista', cep: '01310100',
				municipio: '3550308'
			}
		});
		await rolar(op);
		await capturar('02-incluir-corpo.png');
		await executar(op);

		op = await abrir('get', `${CLIENTE}/pesquisa`);
		await executar(op);
		await rolarParaResposta(op);
		await capturar('03-pesquisa-resposta.png');

		op = await abrir('post', `${MUNICIPIO}/{identificador}/excluir`);
		await parametro(op, 'identificador', '3550308');
		await executar(op);
		await rolarParaResposta(op);
		await capturar('04-excluir-municipio-resposta.png');
	},
};

if (!licoes[licao]) {
	throw new Error(`Lição desconhecida: ${licao}; use uma de ${Object.keys(licoes).join(', ')}`);
}

try {
	await abrirSwagger();
	await licoes[licao]();
} catch (erro) {
	// Guarda a tela no momento da falha, rolada até a última operação executada, para diagnóstico.
	if (ultimaOp) await avaliar(`${ultimaOp}?.scrollIntoView({ block: 'start' })`).catch(() => { });
	const { data } = await cdp('Page.captureScreenshot', { format: 'png' });
	const arquivo = join(tmpdir(), 'd5-capturas-erro.png');
	writeFileSync(arquivo, Buffer.from(data, 'base64'));
	console.error(`Falha; tela salva em ${arquivo}`);
	throw erro;
} finally {
	ws.close();
	chrome.kill();
	await dormir(500);
	try { rmSync(perfil, { recursive: true, force: true }); } catch { }
}
