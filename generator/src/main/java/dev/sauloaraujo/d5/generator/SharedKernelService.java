package dev.sauloaraujo.d5.generator;

import static org.eclipse.xtext.EcoreUtil2.getAllContentsOfType;
import static org.eclipse.xtext.EcoreUtil2.getContainerOfType;

import java.util.HashSet;
import java.util.Set;

import org.contextmapper.dsl.contextMappingDSL.BoundedContext;
import org.contextmapper.dsl.contextMappingDSL.ContextMappingModel;
import org.contextmapper.tactic.dsl.tacticdsl.Reference;
import org.contextmapper.tactic.dsl.tacticdsl.ValueObject;
import org.springframework.stereotype.Service;

@Service
public class SharedKernelService {
	public Set<ValueObject> sharedValueObjects(ContextMappingModel model) {
		var sharedValueObjects = new HashSet<ValueObject>();
		var references = getAllContentsOfType(model, Reference.class);
		for (var reference : references) {
			var referenceBoundedContext = getContainerOfType(reference, BoundedContext.class);
			var simpleDomainObject = reference.getDomainObjectType();
			if (simpleDomainObject instanceof ValueObject) {
				var valueObject = (ValueObject) simpleDomainObject;
				var valueObjectBoundedContext = getContainerOfType(valueObject, BoundedContext.class);
				if (valueObjectBoundedContext != referenceBoundedContext) {
					sharedValueObjects.add(valueObject);
					addReferences(sharedValueObjects, valueObject);
				}
			}
		}
		return sharedValueObjects;
	}

	private void addReferences(Set<ValueObject> sharedValueObjects, ValueObject source) {
		for (var reference : source.getReferences()) {
			var simpleDomainObject = reference.getDomainObjectType();
			if (simpleDomainObject instanceof ValueObject) {
				var target = (ValueObject) simpleDomainObject;
				if (sharedValueObjects.add(target)) {
					addReferences(sharedValueObjects, source);
				}
			}
		}
	}
}