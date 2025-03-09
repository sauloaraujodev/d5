<#ftl output_format="XML">
<?xml version="1.0" encoding="UTF-8"?>

<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
                             https://maven.apache.org/xsd/maven-4.0.0.xsd">
  	<modelVersion>4.0.0</modelVersion>
  	
  	<parent>
	    <groupId>org.springframework.boot</groupId>
	    <artifactId>spring-boot-starter-parent</artifactId>
	    <version>3.4.2</version>
	    <relativePath/>
	</parent>

  	<groupId>${groupId}</groupId>
  	<artifactId>${projectIdentifier}-parent</artifactId>
  	<version>${version}</version>
  
  	<packaging>pom</packaging>
  	
	<build>
		<pluginManagement>
			<plugins>
				<plugin>
					<groupId>com.google.cloud.tools</groupId>
					<artifactId>jib-maven-plugin</artifactId>
					<version>3.4.4</version>
				</plugin>				
			</plugins>
		</pluginManagement>
	</build>

  	<dependencyManagement>
  		<dependencies>
			<dependency>
			    <groupId>org.mapstruct</groupId>
			    <artifactId>mapstruct</artifactId>
			    <version>1.6.3</version>
			</dependency>			
  		</dependencies>
  	</dependencyManagement>
</project>