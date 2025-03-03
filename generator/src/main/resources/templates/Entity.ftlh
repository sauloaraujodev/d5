package ${packagePrefix}.domain.${upperCamelToLowerUnderscore(boundedContext.name)}.${upperCamelToLowerUnderscore(aggregate.name)};

import java.util.*;

<#list attributesOrReferences(entity) as attribute>
<#if attribute.import??>
import ${attribute.import};
</#if>
</#list>

public class ${entity.name}<#if entity.extends??> extends ${entity.extends.name}</#if> {
<#list attributesOrReferences(entity) as attribute>
	private <#if attribute.type??>${attribute.type}</#if> <#if attribute.name??>${attribute.name}</#if>;
	<#if attribute?is_last>

	</#if>
</#list>
	public ${entity.name}(<#if entity.extends?? && entity.extends.attributes?has_content><#list entity.extends.attributes as attribute>${attribute.type} ${attribute.name}<#sep>, </#list><#if entity.attributes?has_content>, </#if></#if><#list entity.attributes as attribute>${attribute.type} ${attribute.name}<#sep>, </#list>) {
<#if entity.extends??>
		super(<#list entity.extends.attributes as attribute>${attribute.name}<#sep>, </#list>);
</#if>
<#list entity.attributes as attribute>
		set${lowerCamelToUpperCamel(attribute.name)}(${attribute.name});
</#list>
	}
<#list entity.attributes as attribute>

	public void set${lowerCamelToUpperCamel(attribute.name)}(${attribute.type} ${attribute.name}) {
		this.${attribute.name} = <#if !attribute.nullable>Objects.requireNonNull(</#if>${attribute.name}<#if !attribute.nullable>)</#if>;
	}

	public ${attribute.type} get${lowerCamelToUpperCamel(attribute.name)}() {
		return ${attribute.name};
	}
</#list>
}