plugins {
	java
	id("org.springframework.boot") version "3.0.6"
	id("io.spring.dependency-management") version "1.1.0"
	id("org.openapi.generator") version "6.6.0"
}

group = "org.k9m.assignments"
version = "0.0.1-SNAPSHOT"

val cucumberVersion = "7.12.0"


java.sourceCompatibility = JavaVersion.VERSION_17
java.sourceSets["main"].java.srcDir("$buildDir/generated/src/main/java")

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-actuator")

	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0")

	compileOnly("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("com.h2database:h2")
	annotationProcessor("org.projectlombok:lombok")


	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.cucumber:cucumber-java:${cucumberVersion}")
	testImplementation("io.cucumber:cucumber-junit-platform-engine:${cucumberVersion}")
	testImplementation("io.cucumber:cucumber-spring:${cucumberVersion}")
	testImplementation("org.assertj:assertj-core:3.24.2")
	testImplementation("org.junit.platform:junit-platform-suite")


	testCompileOnly("org.projectlombok:lombok")
	testAnnotationProcessor("org.projectlombok:lombok")
}

openApiGenerate {
	generatorName.set("spring")
	library.set("spring-boot")
	inputSpec.set("src/main/resources/api/contract.yml")
	outputDir.set("$buildDir/generated")
	apiPackage.set("org.k9m.assignments.bankaccountapi.api")
	modelPackage.set("org.k9m.assignments.bankaccountapi.api.model")
	modelNameSuffix.set("DTO")
	globalProperties.set(mapOf(
			"generateSupportingFiles" to "false",
			"modelDocs" to "false"
	))
	configOptions.set(mapOf(
			"useSpringBoot3" to "true",
			"useResponseEntity" to "true",
			"dateLibrary" to "java8",
			"interfaceOnly" to "true",
			"exceptionHandler" to "false",
			"swaggerDocketConfig" to "false",
			"openApiNullable" to "false",
			"hideGenerationTimestamp" to "true",
			"skipDefaultInterface" to "true"
	))
	generateModelDocumentation.set(false)
	logToStderr.set(true)

}

tasks.withType<Test> {
	useJUnitPlatform()
}
tasks.named("compileJava") {
	dependsOn(":openApiGenerate")
}
