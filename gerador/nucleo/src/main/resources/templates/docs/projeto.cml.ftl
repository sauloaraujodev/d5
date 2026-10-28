import "compartilhado/projeto.cml"
<#list subdominios as subdominio>
import "${subdominio}/projeto.cml"
</#list>
