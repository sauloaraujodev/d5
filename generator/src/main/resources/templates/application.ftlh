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

	<artifactId>${projectIdentifier}-application</artifactId>

	<dependencies>
<#list model.boundedContexts as context>
<#if !context?is_first>

</#if>
		<dependency>
			<groupId>${"$"}{project.groupId}</groupId>
			<artifactId>${projectIdentifier}-domain-${upperCamelToLowerHyphen(context.name)}</artifactId>
			<version>${"$"}{project.version}</version>
		</dependency>
</#list>
	</dependencies>		
</project>