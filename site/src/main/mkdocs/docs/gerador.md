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
</div>
<div class="gerador-coluna" id="gerador-subdominios">
<div class="gerador-campo gerador-subdominio">
<label>Subdomínio *</label>
<div class="gerador-grupo">
<input type="text" name="subdominios" required pattern="[a-z]+">
<button type="button" class="md-button md-button--primary gerador-incluir" title="Incluir subdomínio">+</button>
</div>
<small class="gerador-erro" data-vazio="Obrigatório">Deve conter apenas letras minúsculas</small>
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
<button type="submit" class="md-button md-button--primary">Gerar</button>
</form>
