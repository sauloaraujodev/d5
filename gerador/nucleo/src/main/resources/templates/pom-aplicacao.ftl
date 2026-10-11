<project xmlns="http://maven.apache.org/POM/4.0.0"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>

	<parent>
		<groupId>${groupId}</groupId>
		<artifactId>${nome}-pai</artifactId>
		<version>1.0.0-SNAPSHOT</version>
	</parent>

	<artifactId>${nome}-aplicacao</artifactId>

	<dependencies>
<#if framework>
		<dependency>
			<groupId>dev.sauloaraujo</groupId>
			<artifactId>d5-framework-aplicacao</artifactId>
		</dependency>

<#else>
		<dependency>
			<groupId>org.apache.commons</groupId>
			<artifactId>commons-lang3</artifactId>
		</dependency>

</#if>
<#list subdominios as subdominio>
		<dependency>
			<groupId>${groupId}</groupId>
			<artifactId>${nome}-dominio-${subdominio}</artifactId>
			<version>1.0.0-SNAPSHOT</version>
		</dependency>

</#list>
<#if framework>
		<dependency>
			<groupId>dev.sauloaraujo</groupId>
			<artifactId>d5-framework-aplicacao-teste</artifactId>
			<type>pom</type>
			<scope>test</scope>
		</dependency>
<#else>
		<dependency>
			<groupId>io.cucumber</groupId>
			<artifactId>cucumber-java</artifactId>
			<scope>test</scope>
		</dependency>

		<dependency>
			<groupId>io.cucumber</groupId>
			<artifactId>cucumber-junit-platform-engine</artifactId>
			<scope>test</scope>
		</dependency>

		<dependency>
			<groupId>org.junit.jupiter</groupId>
			<artifactId>junit-jupiter</artifactId>
			<scope>test</scope>
		</dependency>

		<dependency>
			<groupId>org.junit.platform</groupId>
			<artifactId>junit-platform-suite</artifactId>
			<scope>test</scope>
		</dependency>

		<dependency>
			<groupId>org.mockito</groupId>
			<artifactId>mockito-core</artifactId>
			<scope>test</scope>
		</dependency>
</#if>
	</dependencies>
</project>
