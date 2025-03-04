package ${packagePrefix}.domain.${upperCamelToLowerUnderscore(boundedContext.name)}.${upperCamelToLowerUnderscore(aggregate.name)};

import java.util.*;

<#list attributesOrReferences(valueObject) as attribute>
<#if attribute.import??>
import ${attribute.import};
</#if>
<#if attribute?is_last>

</#if>
</#list>
public class ${valueObject.name}<#if valueObject.extends??> extends ${valueObject.extends.name}</#if> {
<#list attributesOrReferences(valueObject) as attribute>
	private final ${attribute.type} ${attribute.name};
	<#if attribute?is_last>

	</#if>
</#list>
	public ${valueObject.name}(<#if valueObject.extends?? && attributesOrReferences(valueObject.extends)?has_content><#list attributesOrReferences(valueObject.extends) as attribute>${attribute.type} ${attribute.name}<#sep>, </#list><#if attributesOrReferences(valueObject)?has_content>, </#if></#if><#list attributesOrReferences(valueObject) as attribute>${attribute.type} ${attribute.name}<#sep>, </#list>) {
<#if valueObject.extends??>
		super(<#list attributesOrReferences(valueObject.extends) as attribute>${attribute.name}<#sep>, </#list>);
</#if>
<#list attributesOrReferences(valueObject) as attribute>
		this.${attribute.name} = ${attribute.name};
</#list>
	}
<#list attributesOrReferences(valueObject) as attribute>

	public ${attribute.type} get${lowerCamelToUpperCamel(attribute.name)}() {
		return ${attribute.name};
	}
</#list>

	@Override
	public boolean equals(Object object) {
		if (this == object) {
			return true;
		}
		if (object == null) {
			return false;
		}
		if (getClass() != object.getClass()) {
			return false;
		}
<#if valueObject.extends??>		
		if (!super.equals(object)) {
			return false;
		}
</#if>
<#if attributesOrReferences(valueObject)?has_content>
		var that = (${valueObject.name}) object;
<#list attributesOrReferences(valueObject) as attribute>
		if (!Objects.deepEquals(${attribute.name}, that.${attribute.name})) {
			return false;
		}
</#list>
</#if>
		return true;
	}

	@Override
	public int hashCode() {
		return <#if valueObject.extends??>31 * super.hashCode() + </#if>Objects.hash(<#list attributesOrReferences(valueObject) as attribute>${attribute.name}<#if !attribute?is_last>, </#if></#list>);
	}	
}