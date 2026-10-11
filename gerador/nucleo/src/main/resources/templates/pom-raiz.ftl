<project xmlns="http://maven.apache.org/POM/4.0.0"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>

	<parent>
<#if framework>
		<groupId>dev.sauloaraujo</groupId>
		<artifactId>d5-pai</artifactId>
		<version>1.0.0</version>
<#else>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-parent</artifactId>
		<version>4.1.1</version>
		<relativePath />
</#if>
	</parent>

	<groupId>${groupId}</groupId>
	<artifactId>${nome}-pai</artifactId>
	<version>1.0.0-SNAPSHOT</version>

	<packaging>pom</packaging>

<#if !framework>
	<dependencyManagement>
		<dependencies>
			<dependency>
				<groupId>io.cucumber</groupId>
				<artifactId>cucumber-bom</artifactId>
				<version>7.34.8</version>
				<scope>import</scope>
				<type>pom</type>
			</dependency>
		</dependencies>
	</dependencyManagement>

	<dependencies>
		<dependency>
			<groupId>org.jspecify</groupId>
			<artifactId>jspecify</artifactId>
		</dependency>
	</dependencies>

</#if>
	<modules>
		<module>dominio-compartilhado</module>
<#list subdominios as subdominio>
		<module>dominio-${subdominio}</module>
</#list>
		<module>aplicacao</module>
		<module>infraestrutura</module>
		<module>ui-backend</module>
	</modules>
</project>
