package dev.sauloaraujo.d5.generator;

import java.util.ArrayList;
import java.util.List;

import org.contextmapper.tactic.dsl.tacticdsl.DomainObject;

import freemarker.ext.util.WrapperTemplateModel;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

class AttributesOrReferencesMethodModel implements TemplateMethodModelEx {
	String packagePrefix;

	AttributesOrReferencesMethodModel(String packagePrefix) {
		this.packagePrefix = packagePrefix;
	}

	@Override
	public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
		if (arguments.size() != 1) {
			throw new IllegalArgumentException("arguments.size() != 1");
		}
		var argument = arguments.get(0);
		if (argument == null) {
			throw new NullPointerException("arguments.get(0) == null");
		}
		var model = (WrapperTemplateModel) argument;
		var domainObject = (DomainObject) model.getWrappedObject();
		var attributesOrReferences = new ArrayList<AttributeOrReference>();
		attributesOrReferences.addAll(domainObject.getAttributes().stream().map(a -> new AttributeAdapter(a)).toList());
		attributesOrReferences.addAll(
				domainObject.getReferences().stream().map(r -> new ReferenceAdapter(packagePrefix, r)).toList());
		return attributesOrReferences;
	}
}

interface AttributeOrReference {
	String getName();

	String getType();

	boolean isNullable();

	String getImport();
}