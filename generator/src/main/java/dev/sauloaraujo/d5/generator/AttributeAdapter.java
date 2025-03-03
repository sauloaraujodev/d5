package dev.sauloaraujo.d5.generator;

import org.contextmapper.tactic.dsl.tacticdsl.Attribute;

public class AttributeAdapter implements AttributeOrReference {
	Attribute attribute;

	AttributeAdapter(Attribute attribute) {
		this.attribute = attribute;
	}

	public String getName() {
		return attribute.getName();
	}

	public String getType() {
		return attribute.getType();
	}

	public boolean isNullable() {
		return attribute.isNullable();
	}

	public String getImport() {
		return null;
	}
}