
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import org.apache.avro.Schema.Parser
import org.apache.avro.compiler.specific.SpecificCompiler
import org.apache.avro.generic.GenericData
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val kotlinVersion = "1.9.23"
    val springBootVersion = "3.2.5"
    val springDependencyManagementVersion = "1.1.4"
    val detektVersion = "1.23.6"

    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    kotlin("plugin.jpa") version kotlinVersion

    id("org.springframework.boot") version springBootVersion
    id("io.spring.dependency-management") version springDependencyManagementVersion
    id("io.gitlab.arturbosch.detekt") version detektVersion
    id("org.jlleitschuh.gradle.ktlint") version "10.3.0"
}

group = "br.com.rodrigogurgel"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
    maven("https://packages.confluent.io/maven/")
}

detekt {
    buildUponDefaultConfig = true
    allRules = false
    config.setFrom("$projectDir/config/detekt.yml")
    baseline = file("$projectDir/config/baseline.xml")
}

tasks.withType<Detekt>().configureEach {
    reports {
        html.required.set(true)
        xml.required.set(true)
        txt.required.set(true)
        sarif.required.set(true)
        md.required.set(true)
    }
}

tasks.withType<Detekt>().configureEach {
    jvmTarget = "1.8"
}
tasks.withType<DetektCreateBaselineTask>().configureEach {
    jvmTarget = "1.8"
}

dependencies {
    val coroutinesVersion = properties["coroutinesVersion"]
    val kafkaAvroSerializerVersion = properties["kafkaAvroSerializerVersion"]
    val avroVersion = properties["avroVersion"]
    val michaelBullKotlinResultVersion = properties["michaelBullKotlinResultVersion"]
    val rxJavaVersion = properties["rxJavaVersion"]
    val awsSdkVersion = properties["awsSdkVersion"]

    // DynamoDB
    implementation(platform("software.amazon.awssdk:bom:$awsSdkVersion"))
    implementation("software.amazon.awssdk:dynamodb-enhanced")

    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-slf4j:$coroutinesVersion")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    // Spring
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    // Kafka
    implementation("org.springframework.kafka:spring-kafka")
    implementation("org.apache.avro:avro:$avroVersion")
    implementation("io.confluent:kafka-avro-serializer:$kafkaAvroSerializerVersion") {
        // FIX https://devhub.checkmarx.com/cve-details/CVE-2024-26308/
        exclude("org.apache.commons", "commons-compress")
        implementation("org.apache.commons:commons-compress:1.26.0")
    }

    // Misc
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.michael-bull.kotlin-result:kotlin-result:$michaelBullKotlinResultVersion")
    implementation("com.michael-bull.kotlin-result:kotlin-result-coroutines:$michaelBullKotlinResultVersion")
    implementation("io.reactivex.rxjava3:rxjava:$rxJavaVersion")
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.6")

    // Micrometer
    implementation("io.micrometer:micrometer-registry-prometheus")
    implementation("org.aspectj:aspectjweaver:1.9.22.1")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.kafka:spring-kafka-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.clean {
    doFirst {
        delete(paths = arrayOf("$rootDir/src/main/avro"))
    }
}

buildscript {
    val avroVersion = properties["avroVersion"]

    dependencies {
        classpath("org.apache.avro:avro-tools:$avroVersion")
    }
}

val avroGen by tasks.register("generateAvroJavaClasses") {
    val sourceAvroFiles = fileTree("src/main/resources") {
        include("**/*.avsc")
    }
    val generatedJavaDir = File("$rootDir/src/main/avro")

    doFirst {
    }

    doLast {
        sourceAvroFiles.forEach { avroFile ->
            val schema = Parser().parse(avroFile)
            val compiler = SpecificCompiler(schema)
            compiler.setFieldVisibility(SpecificCompiler.FieldVisibility.PRIVATE)
            compiler.setOutputCharacterEncoding("UTF-8")
            compiler.setStringType(GenericData.StringType.CharSequence)
            compiler.compileToDestination(avroFile, generatedJavaDir)
        }
    }
}

tasks.jar {
    enabled = false
    archiveClassifier = ""
}

tasks.withType<KotlinCompile> {
    dependsOn(avroGen)
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    dependsOn(avroGen)
    useJUnitPlatform()
}
