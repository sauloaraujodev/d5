<project xmlns="http://maven.apache.org/POM/4.0.0"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>

	<parent>
		<groupId>${groupId}</groupId>
		<artifactId>${nome}-pai</artifactId>
		<version>1.0.0-SNAPSHOT</version>
	</parent>

	<artifactId>${nome}-ui-backend</artifactId>

	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>

	<dependencies>
<#if framework>
		<dependency>
			<groupId>dev.sauloaraujo</groupId>
			<artifactId>d5-framework-ui-backend</artifactId>
		</dependency>

<#else>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-security</artifactId>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>

</#if>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-devtools</artifactId>
			<optional>true</optional>
		</dependency>

		<dependency>
			<groupId>${groupId}</groupId>
			<artifactId>${nome}-aplicacao</artifactId>
			<version>1.0.0-SNAPSHOT</version>
		</dependency>

		<dependency>
			<groupId>${groupId}</groupId>
			<artifactId>${nome}-dominio-compartilhado</artifactId>
			<version>1.0.0-SNAPSHOT</version>
		</dependency>

<#if framework>
		<dependency>
			<groupId>dev.sauloaraujo</groupId>
			<artifactId>d5-framework-ui-backend-teste</artifactId>
			<type>pom</type>
			<scope>test</scope>
		</dependency>

		<dependency>
			<groupId>dev.sauloaraujo</groupId>
			<artifactId>d5-framework-springdoc</artifactId>
			<scope>runtime</scope>
		</dependency>
<#else>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>

		<dependency>
			<groupId>org.springframework.security</groupId>
			<artifactId>spring-security-test</artifactId>
			<scope>test</scope>
		</dependency>

		<dependency>
			<groupId>org.springdoc</groupId>
			<artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
			<version>3.1.0</version>
			<scope>runtime</scope>
		</dependency>
</#if>

		<dependency>
			<groupId>${groupId}</groupId>
			<artifactId>${nome}-infraestrutura</artifactId>
			<version>1.0.0-SNAPSHOT</version>
			<scope>runtime</scope>
		</dependency>
	</dependencies>
</project>
