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

	<artifactId>${projectIdentifier}-domain-${upperCamelToLowerHyphen(contextName)}-bdd</artifactId>

	<dependencyManagement>
		<dependencies>
			<dependency>
				<groupId>io.cucumber</groupId>
				<artifactId>cucumber-bom</artifactId>
				<version>7.21.0</version>
				<type>pom</type>
				<scope>import</scope>
			</dependency>
		</dependencies>
	</dependencyManagement>

	<dependencies>
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
			<groupId>${groupId}</groupId>
			<artifactId>${projectIdentifier}-domain-${upperCamelToLowerHyphen(contextName)}</artifactId>
			<version>${"$"}{project.version}</version>
			<scope>test</scope>			
		</dependency>
  </dependencies>
</project>