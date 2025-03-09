package dev.sauloaraujo.d5.generator;

import static com.google.common.base.CaseFormat.LOWER_CAMEL;
import static com.google.common.base.CaseFormat.UPPER_CAMEL;

import java.util.List;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

public class UpperCamelToLowerCamelMethodModel implements TemplateMethodModelEx {
	public static String convert(String string) {
		return UPPER_CAMEL.to(LOWER_CAMEL, string);
	}

	@Override
	public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
		if (arguments.size() != 1) {
			throw new IllegalArgumentException("arguments.size() != 1");
		}
		var argument = arguments.get(0);
		if (argument != null) {
			var string = argument.toString();
			return convert(string);
		} else {
			return null;
		}
	}
}