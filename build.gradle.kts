plugins {
    id("org.springframework.boot") version "2.6.3"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.spring") version "1.6.10"
    application
}

val BUILD_ID: String by project

group = "org.hamster"
version = "1.0.0.$BUILD_ID"
application {
    mainClass.set("org.hamster.Application")
}
java.sourceCompatibility = JavaVersion.VERSION_11
java.targetCompatibility = JavaVersion.VERSION_11

val jaxb by configurations.creating

repositories {
    mavenCentral()
}

tasks.register("genJaxb") {
    ext["sourcesDir"] = "${buildDir}/generated-sources/jaxb"
    ext["classesDir"] = "${buildDir}/classes/jaxb"
    ext["schema"] = "src/main/resources/xsd/maven-metadata.xsd"

    ext["classesDir"]?.let { outputs.dir(it) }

    doLast {
        ant.withGroovyBuilder {
            "taskdef"(
                "name" to "xjc", "classname" to "com.sun.tools.xjc.XJCTask",
                "classpath" to jaxb.asPath
            )
            ext["sourcesDir"]?.let { mkdir(it) }
            ext["classesDir"]?.let { mkdir(it) }

            "xjc"("destdir" to ext["sourcesDir"], "schema" to ext["schema"]) {
                "arg"("value" to "-wsdl")
                "produces"("dir" to ext["sourcesDir"], "includes" to "**/*.java")
            }

            "javac"(
                "destdir" to ext["classesDir"], "source" to 11, "target" to 11, "debug" to true,
                "debugLevel" to "lines,vars,source", "classpath" to jaxb.asPath
            ) {
                "src"("path" to ext["sourcesDir"])
                "include"("name" to "**/*.java")
                "include"("name" to "*.java")
            }

            "copy"("todir" to ext["classesDir"]) {
                "fileset"("dir" to ext["sourcesDir"], "erroronmissingdir" to false) {
                    "exclude"("name" to "**/*.java")
                }
            }
        }
    }
}
tasks.build {
    dependsOn("genJaxb")
}

dependencies {
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign:3.1.1")

    //spring boot deps
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    //logger
    implementation("ch.qos.logback:logback-classic:1.2.11")
    implementation("ch.qos.logback:logback-core:1.2.11")
    implementation("ch.qos.logback:logback-access:1.2.11")
    implementation("net.logstash.logback:logstash-logback-encoder:7.0.1")
    implementation("org.slf4j:slf4j-api")
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("com.fasterxml.jackson.core:jackson-core")
    implementation("com.fasterxml.jackson.core:jackson-annotations")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    implementation("io.github.openfeign:feign-jaxb:11.8")
    implementation("org.liquibase:liquibase-core")
    implementation("org.liquibase:liquibase-core")
    implementation("org.apache.commons:commons-dbcp2")
    implementation("org.postgresql:postgresql")
    implementation("javax.validation:validation-api")

    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testImplementation("org.junit.platform:junit-platform-launcher")
    testImplementation("org.junit.platform:junit-platform-commons")
    testImplementation("org.assertj:assertj-core")

    implementation("wsdl4j:wsdl4j:1.6.3")
    jaxb("org.glassfish.jaxb:jaxb-xjc:3.0.2")
    jaxb("org.glassfish.jaxb:jaxb-runtime:3.0.2")
    jaxb("jakarta.xml.bind:jakarta.xml.bind-api:3.0.1")
    implementation("jakarta.xml.bind:jakarta.xml.bind-api:3.0.1")
    jaxb("jakarta.activation:jakarta.activation-api:2.1.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
}

tasks.compileKotlin {
    dependsOn("genJaxb")
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

sourceSets.main {
    java.srcDirs("${buildDir}/generated-sources/jaxb")
}