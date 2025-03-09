<#ftl output_format="plainText">
package ${packagePrefix}.domain.${upperCamelToLowerUnderscore(boundedContext.name)}.${upperCamelToLowerUnderscore(aggregate.name)};

import java.util.*;

public interface ${entity.name}Repository {
	${entity.name} save(${entity.name} ${upperCamelToLowerCamel(entity.name)});
<#assign entityId = entityId(entity)>	
	Optional<${entity.name}> findById(${entityId.type} ${entityId.name});
	void deleteById(${entityId.type} ${entityId.name});
}