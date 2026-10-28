# Gerador de projetos

<form id="gerador" class="gerador" action="/api/gerador" method="post" novalidate>
<div class="gerador-colunas">
<div class="gerador-coluna">
<div class="gerador-campo">
<label for="gerador-group-id">groupId *</label>
<input id="gerador-group-id" type="text" name="groupId" required pattern="[a-z][a-z0-9]*(\.[a-z][a-z0-9]*)*">
<small class="gerador-erro" data-vazio="Obrigatório">Deve seguir a <a href="https://maven.apache.org/guides/mini/guide-naming-conventions.html#project-group-identifier" target="_blank">convenção Maven</a></small>
</div>
<div class="gerador-campo">
<label for="gerador-nome">Nome do projeto *</label>
<input id="gerador-nome" type="text" name="nome" required pattern="[a-z]+">
<small class="gerador-erro" data-vazio="Obrigatório">Deve conter apenas letras minúsculas</small>
</div>
<div class="gerador-campo">
<label for="gerador-descricao">Descrição *</label>
<input id="gerador-descricao" type="text" name="descricao" required pattern=".*\S.*">
<small class="gerador-erro" data-vazio="Obrigatório">Obrigatório</small>
</div>
<div class="gerador-campo">
<label for="gerador-versao">Versão *</label>
<input id="gerador-versao" type="text" name="versao" value="1.0.0-SNAPSHOT" required pattern="(0|[1-9]\d*)\.(0|[1-9]\d*)\.(0|[1-9]\d*)(-[0-9A-Za-z\-]+(\.[0-9A-Za-z\-]+)*)?">
<small class="gerador-erro" data-vazio="Obrigatório">Deve seguir o <a href="https://semver.org/lang/pt-BR/" target="_blank">versionamento semântico</a>, por exemplo, 1.0.0-SNAPSHOT</small>
</div>
</div>
<div class="gerador-coluna" id="gerador-subdominios">
<div class="gerador-subdominio">
<div class="gerador-campo">
<label>Subdomínio *</label>
<div class="gerador-grupo">
<input type="text" name="subdominios[0].nome" required pattern="[a-z]+">
<button type="button" class="md-button md-button--primary gerador-incluir" title="Incluir subdomínio">+</button>
</div>
<small class="gerador-erro" data-vazio="Obrigatório">Deve conter apenas letras minúsculas</small>
</div>
<div class="gerador-campo">
<label>Tipo *</label>
<select name="subdominios[0].tipo" required>
<option value="">Selecione</option>
<option value="CORE_DOMAIN">Subdomínio principal</option>
<option value="SUPPORTING_DOMAIN">Subdomínio de suporte</option>
<option value="GENERIC_SUBDOMAIN">Subdomínio genérico</option>
</select>
<small class="gerador-erro" data-vazio="Obrigatório">Obrigatório</small>
</div>
<div class="gerador-campo">
<label>Descrição *</label>
<input type="text" name="subdominios[0].descricao" required pattern=".*\S.*">
<small class="gerador-erro" data-vazio="Obrigatório">Obrigatório</small>
</div>
</div>
</div>
</div>
<div class="gerador-opcao">
<input type="hidden" name="_framework" value="on">
<label><input id="gerador-framework" type="checkbox" name="framework" checked> Usar o framework D5</label>
</div>
<div class="gerador-opcao">
<input type="hidden" name="_agentes" value="on">
<label><input id="gerador-agentes" type="checkbox" name="agentes" checked> Incluir AGENTS.md e skills</label>
</div>
<div class="gerador-opcao">
<span>Frontend:</span>
<label><input type="radio" name="frontend" value="ANGULAR" checked> Angular</label>
<label><input type="radio" name="frontend" value="REACT"> React</label>
</div>
<button type="submit" class="md-button md-button--primary">Gerar</button>
</form>
