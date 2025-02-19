<?xml version="1.0" encoding="UTF-8"?>

<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
                             https://maven.apache.org/xsd/maven-4.0.0.xsd">
  	<modelVersion>4.0.0</modelVersion>

  	<groupId>${groupId}</groupId>
  	<artifactId>${projectIdentifier}-modules</artifactId>
  	<version>${version}</version>
  
  	<packaging>pom</packaging>
  	
	<build>
		<plugins>
			<plugin>
				<groupId>com.google.cloud.tools</groupId>
				<artifactId>jib-maven-plugin</artifactId>
				<configuration>
					<to>
						<image>${"$"}{project.artifactId}:${"$"}{version}</image>
					</to>
				</configuration>
				<executions>
					<execution>
						<phase>package</phase>
						<goals>
							<goal>dockerBuild</goal>
						</goals>
					</execution>
				</executions>
			</plugin>

  			<plugin>
			    <groupId>com.github.ferstl</groupId>
			    <artifactId>depgraph-maven-plugin</artifactId>
			    <version>4.0.3</version>
			    <configuration>
			    	<graphFormat>puml</graphFormat>
			    	<includes>${"$"}{project.groupId}:*</includes>
			    </configuration>
			    <executions>
			    	<execution>
			    		<phase>package</phase>
					    <goals>
					    	<goal>aggregate</goal>
						</goals>
			    	</execution>
			    </executions>
  			</plugin>
  		</plugins>
  	</build>

	<modules>
  		<module>application</module>  	
  		<module>domain-common</module>  		
<#list model.boundedContexts as context>
  		<module>domain-${upperCamelToLowerHyphen(context.name)}</module>
  		<module>domain-${upperCamelToLowerHyphen(context.name)}-bdd</module>
</#list>
		<module>infrastructure-jpa</module>
		<module>parent</module>
  		<module>presentation-angular</module>
  		<module>presentation-backend</module>
		<module>presentation-vaadin</module>  		  		
	</modules>
</project>