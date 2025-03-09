package dev.sauloaraujo.d5.generator;

import java.util.List;
import java.util.Objects;

import org.contextmapper.tactic.dsl.tacticdsl.Entity;

import freemarker.ext.util.WrapperTemplateModel;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

public class EntityIdMethodModel implements TemplateMethodModelEx {
	private static final String ID = "id";

	private String packagePrefix;

	public EntityIdMethodModel(String packagePrefix) {
		this.packagePrefix = Objects.requireNonNull(packagePrefix);
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
		var entity = (Entity) model.getWrappedObject();
		for (var attribute : entity.getAttributes()) {
			if (attribute.getName().equals(ID)) {
				return new AttributeAdapter(attribute);
			}
		}
		for (var reference : entity.getReferences()) {
			if (reference.getName().equals(ID)) {
				return new ReferenceAdapter(packagePrefix, reference);
			}
		}
		return null;
	}
}