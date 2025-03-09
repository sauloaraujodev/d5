<#ftl output_format="plainText">
package ${packagePrefix};

import static org.springframework.boot.SpringApplication.run;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VaadinApplication {
	public static void main(String[] args) {
		run(VaadinApplication.class, args);
	}
}