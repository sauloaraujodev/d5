<#ftl output_format="plainText">
package ${packagePrefix}.domain.${upperCamelToLowerUnderscore(boundedContext.name)}.${upperCamelToLowerUnderscore(aggregate.name)};

import java.util.*;

public class ${entity.name}DomainService {
	private final ${entity.name}Repository repository;
	
	public ${entity.name}DomainService(${entity.name}Repository repository) {
		this.repository = Objects.requireNonNull(repository);
	}
	
	public ${entity.name} save(${entity.name} ${upperCamelToLowerCamel(entity.name)}) {
		Objects.requireNonNull(${upperCamelToLowerCamel(entity.name)});	
		return repository.save(${upperCamelToLowerCamel(entity.name)});
	}
	
<#assign entityId = entityId(entity)>	
	public Optional<${entity.name}> findById(${entityId.type} ${entityId.name}) {
		Objects.requireNonNull(${entityId.name});
		return repository.findById(${entityId.name});
	}
	
	public void deleteById(${entityId.type} ${entityId.name}) {
		Objects.requireNonNull(${entityId.name});	
		repository.deleteById(${entityId.name});
	}
}