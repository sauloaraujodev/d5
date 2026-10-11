<project xmlns="http://maven.apache.org/POM/4.0.0"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
	<modelVersion>4.0.0</modelVersion>

	<parent>
		<groupId>${groupId}</groupId>
		<artifactId>${nome}-pai</artifactId>
		<version>1.0.0-SNAPSHOT</version>
	</parent>

	<artifactId>${nome}-infraestrutura</artifactId>

	<dependencies>
<#if framework>
		<dependency>
			<groupId>dev.sauloaraujo</groupId>
			<artifactId>d5-framework-infraestrutura</artifactId>
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
			<groupId>com.h2database</groupId>
			<artifactId>h2</artifactId>
			<scope>runtime</scope>
		</dependency>

</#if>
		<dependency>
			<groupId>${groupId}</groupId>
			<artifactId>${nome}-aplicacao</artifactId>
			<version>1.0.0-SNAPSHOT</version>
		</dependency>

<#if framework>
		<dependency>
			<groupId>dev.sauloaraujo</groupId>
			<artifactId>d5-framework-infraestrutura-teste</artifactId>
			<scope>test</scope>
		</dependency>
<#else>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>

		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa-test</artifactId>
			<scope>test</scope>
		</dependency>
</#if>
	</dependencies>
</project>
