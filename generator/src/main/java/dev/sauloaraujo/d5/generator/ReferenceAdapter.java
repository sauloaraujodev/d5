package dev.sauloaraujo.d5.generator;

import static dev.sauloaraujo.d5.generator.UpperCamelToLowerUnderscoreMethodModel.convert;
import static org.eclipse.xtext.EcoreUtil2.getContainerOfType;

import org.contextmapper.dsl.contextMappingDSL.Aggregate;
import org.contextmapper.dsl.contextMappingDSL.BoundedContext;
import org.contextmapper.tactic.dsl.tacticdsl.Reference;

public class ReferenceAdapter implements AttributeOrReference {
	String packagePrefix;
	Reference reference;

	ReferenceAdapter(String packagePrefix, Reference reference) {
		this.packagePrefix = packagePrefix;
		this.reference = reference;
	}

	public String getName() {
		return reference.getName();
	}

	public String getType() {
		return reference.getDomainObjectType().getName();
	}

	public boolean isNullable() {
		return reference.isNullable();
	}

	public String getImport() {
		var simpleDomainObject = reference.getDomainObjectType();
		var aggregate = getContainerOfType(simpleDomainObject, Aggregate.class);
		var boundedContext = getContainerOfType(aggregate, BoundedContext.class);
		return packagePrefix + ".domain." + convert(boundedContext.getName()) + "." + convert(aggregate.getName()) + "."
				+ simpleDomainObject.getName();
	}
}