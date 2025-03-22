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

	<artifactId>${projectIdentifier}-presentation-vaadin</artifactId>
	
	<build>
		<plugins>
			<plugin>
				<groupId>com.google.cloud.tools</groupId>
				<artifactId>jib-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>	

	<dependencies>
		<!-- compile -->
		<dependency>
			<groupId>com.vaadin</groupId>
			<artifactId>vaadin-spring-boot-starter</artifactId>
			<version>24.7.0</version>
			<scope>compile</scope>
		</dependency>
	
		<dependency>
			<groupId>${"$"}{project.groupId}</groupId>
			<artifactId>${projectIdentifier}-application</artifactId>
			<version>${"$"}{project.version}</version>
			<scope>compile</scope>
		</dependency>
		
		<!-- runtime -->
		<dependency>
			<groupId>${"$"}{project.groupId}</groupId>
			<artifactId>${projectIdentifier}-infrastructure</artifactId>
			<version>${"$"}{project.version}</version>
			<scope>runtime</scope>
		</dependency>		
	</dependencies>
</project>