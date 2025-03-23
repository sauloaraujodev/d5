<#ftl output_format="XML">
<?xml version="1.0" encoding="UTF-8"?>

<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
                             https://maven.apache.org/xsd/maven-4.0.0.xsd">
  	<modelVersion>4.0.0</modelVersion>

	<parent>
    	<groupId>${groupId}</groupId>
		<artifactId>${projectIdentifier}-parent</artifactId>
		<version>${version}</version>
		<relativePath>../parent</relativePath>
	</parent>

	<artifactId>${projectIdentifier}-infrastructure</artifactId>
	
	<dependencies>
		<!-- compile -->
		<dependency>
		    <groupId>org.flywaydb</groupId>
		    <artifactId>flyway-core</artifactId>
		    <scope>compile</scope>
		</dependency>

		<dependency>
		    <groupId>org.mapstruct</groupId>
		    <artifactId>mapstruct</artifactId>
		    <scope>compile</scope>
		</dependency>

		<dependency>
		    <groupId>org.springframework.boot</groupId>
		    <artifactId>spring-boot-starter-data-jpa</artifactId>
		    <scope>compile</scope>
		</dependency>
<#list model.boundedContexts as context>

		<dependency>
			<groupId>${"$"}{project.groupId}</groupId>
			<artifactId>${projectIdentifier}-domain-${upperCamelToLowerHyphen(context.name)}</artifactId>
			<version>${"$"}{project.version}</version>
			<scope>compile</scope>
		</dependency>
</#list>

		<dependency>
			<groupId>${"$"}{project.groupId}</groupId>
			<artifactId>${projectIdentifier}-application</artifactId>
			<version>${"$"}{project.version}</version>
			<scope>compile</scope>
		</dependency>

		<!-- test -->
		<dependency>
		    <groupId>org.springframework.boot</groupId>
		    <artifactId>spring-boot-starter-test</artifactId>
		    <scope>test</scope>
		</dependency>
	</dependencies>		
</project>