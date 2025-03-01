package dev.sauloaraujo.d5.generator;

import static com.google.common.base.CaseFormat.LOWER_CAMEL;
import static com.google.common.base.CaseFormat.UPPER_CAMEL;

import java.util.List;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;

public class LowerCamelToUpperCamelMethodModel implements TemplateMethodModelEx {
	public static String convert(String string) {
		return LOWER_CAMEL.to(UPPER_CAMEL, string);
	}

	@Override
	public Object exec(@SuppressWarnings("rawtypes") List arguments) throws TemplateModelException {
		var argument = arguments.get(0);
		if (argument != null) {
			var string = argument.toString();
			return convert(string);
		} else {
			return null;
		}
	}
}