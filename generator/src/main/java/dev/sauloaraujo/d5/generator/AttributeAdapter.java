package dev.sauloaraujo.d5.generator;

import java.util.Objects;

import org.contextmapper.tactic.dsl.tacticdsl.Attribute;

public class AttributeAdapter implements AttributeOrReference {
	private final Attribute attribute;

	AttributeAdapter(Attribute attribute) {
		this.attribute = Objects.requireNonNull(attribute);
	}

	public String getName() {
		return attribute.getName();
	}

	public String getType() {
		var type = attribute.getType();
		var collectionType = attribute.getCollectionType();
		switch (collectionType) {
		case NONE:
			return type;
		default:
			return collectionType.getName() + "<" + type + ">";
		}
	}

	public boolean isNullable() {
		return attribute.isNullable();
	}

	public String getImport() {
		return null;
	}
}