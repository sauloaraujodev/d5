package ${packagePrefix}.domain.${upperCamelToLowerUnderscore(boundedContext.name)}.${upperCamelToLowerUnderscore(aggregate.name)};

import java.util.Date;
import static java.util.Objects.deepEquals;
import static java.util.Objects.hash;

public class ${valueObject.name}<#if valueObject.extends??> extends ${valueObject.extends.name}</#if> {
<#list valueObject.attributes as attribute>
	private final ${attribute.type} ${attribute.name};
	<#if attribute?is_last>

	</#if>
</#list>
	public ${valueObject.name}(<#if valueObject.extends?? && valueObject.extends.attributes?has_content><#list valueObject.extends.attributes as attribute>${attribute.type} ${attribute.name}<#sep>, </#list><#if valueObject.attributes?has_content>, </#if></#if><#list valueObject.attributes as attribute>${attribute.type} ${attribute.name}<#sep>, </#list>) {
<#if valueObject.extends??>
		super(<#list valueObject.extends.attributes as attribute>${attribute.name}<#sep>, </#list>);
</#if>
<#list valueObject.attributes as attribute>
		this.${attribute.name} = ${attribute.name};
</#list>
	}
<#list valueObject.attributes as attribute>

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
<#if valueObject.attributes?has_content>
		var that = (${valueObject.name}) object;
<#list valueObject.attributes as attribute>
		if (!deepEquals(${attribute.name}, that.${attribute.name})) {
			return false;
		}
</#list>
</#if>
		return true;
	}

	@Override
	public int hashCode() {
		return <#if valueObject.extends??>31 * super.hashCode() + </#if>hash(<#list valueObject.attributes as attribute>${attribute.name}<#if !attribute?is_last>, </#if></#list>);
	}	
}