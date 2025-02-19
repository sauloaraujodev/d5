package dev.sauloaraujo.d5.generator;

import static com.google.common.base.CaseFormat.LOWER_HYPHEN;
import static com.google.common.base.CaseFormat.UPPER_CAMEL;

import java.util.List;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

public class UpperCamelToLowerHyphenMethodModel implements TemplateMethodModelEx {
	public static String convert(String string) {
		return UPPER_CAMEL.to(LOWER_HYPHEN, string);
	}

	@Override
	public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
		var argument = arguments.get(0).toString();
		return convert(argument);
	}
}