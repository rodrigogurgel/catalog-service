import io.gitlab.arturbosch.detekt.Detekt

plugins {
    id("org.springframework.boot") version "3.3.1"
    id("io.spring.dependency-management") version "1.1.5"
    id("io.gitlab.arturbosch.detekt") version "1.23.6"
    id("org.jlleitschuh.gradle.ktlint") version "10.3.0"
    id("info.solidsoft.pitest") version "1.15.0"

    jacoco

    kotlin("jvm") version "1.9.24"
    kotlin("plugin.spring") version "1.9.24"
}

group = "br.com.rodrigogurgel"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

fun hasFile(filePath: String): Boolean {
    val licenseFile = File(projectDir, filePath)
    return licenseFile.exists()
}

dependencies {
    // Versions
    val detektVersion = properties["detektVersion"]
    val springMockkVersion = properties["springMockkVersion"]
    val cucumberVersion = properties["cucumberVersion"]
    val kotestVersion = properties["kotestVersion"]
    val pitestJUnit5Version = properties["pitestJUnit5Version"]
    val gradlePitestPluginVersion = properties["gradlePitestPluginVersion"]
    val arcmutatePitestKotlinPluginVersion = properties["arcmutatePitestKotlinPluginVersion"]
    val arcmutateSpringVersion = properties["arcmutateSpringVersion"]

    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // Spring
    implementation("org.springframework.boot:spring-boot-starter-web")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    // Detekt
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:$detektVersion")

    // Spring Test
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude("org.mockito", "mockito-junit-jupiter")
        exclude("org.mockito", "mockito-core")
    }
    testImplementation("com.ninja-squad:springmockk:$springMockkVersion")

    // JUnit
    testImplementation("org.junit.platform:junit-platform-suite")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // Cucumber
    testImplementation("io.cucumber:cucumber-java:$cucumberVersion")
    testImplementation("io.cucumber:cucumber-junit-platform-engine:$cucumberVersion")
    testImplementation("io.cucumber:cucumber-picocontainer:$cucumberVersion")

    // Kotest
    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-property:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")

    // Pitest
    testImplementation("org.pitest:pitest-junit5-plugin:$pitestJUnit5Version")
    pitest("info.solidsoft.gradle.pitest:gradle-pitest-plugin:$gradlePitestPluginVersion")
    if (hasFile("arcmutate-licence.txt")) {
        pitest("com.arcmutate:pitest-kotlin-plugin:$arcmutatePitestKotlinPluginVersion")
        pitest("com.arcmutate:arcmutate-spring:$arcmutateSpringVersion")
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
    systemProperty("cucumber.junit-platform.naming-strategy", "long")
}

detekt {
    autoCorrect = true
    buildUponDefaultConfig = true
    allRules = false
    config.setFrom("$projectDir/config/detekt.yaml")
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

configurations.all {
    resolutionStrategy.eachDependency {
        if (requested.group == "org.jetbrains.kotlin") {
            useVersion(io.gitlab.arturbosch.detekt.getSupportedKotlinVersion())
        }
    }
}

pitest {
    threads = 8
    avoidCallsTo = setOf("kotlin.jvm.internal")
    outputFormats = setOf("HTML")
    jvmArgs = listOf("-Xmx1024m")
    verbose = true
    pitestVersion = "1.16.1"
    junit5PluginVersion = "1.2.1"
}

jacoco {
    toolVersion = "0.8.12"
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)

    reports {
        html.required = true
        xml.required = true
        csv.required = false
    }

    classDirectories.setFrom(
        sourceSets.main.get().output.asFileTree.matching {
            exclude("br/com/rodrigogurgel/catalogservice/CatalogApplication*")
        }
    )

    finalizedBy(tasks.jacocoTestCoverageVerification)
}

tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            element = "CLASS"

            // Excluding values class because jacoco can't check
            val excludeValueClasses = arrayOf(
                "br.com.rodrigogurgel.catalogservice.domain.vo.Name*",
                "br.com.rodrigogurgel.catalogservice.domain.vo.Description*",
                "br.com.rodrigogurgel.catalogservice.domain.vo.Image*",
            )

            excludes = listOf(
                "br.com.rodrigogurgel.catalogservice.CatalogApplication*",
                *excludeValueClasses
            )

            limit {
                counter = "LINE"
                value = "COVEREDRATIO"
                minimum = "0.98".toBigDecimal()
            }

            limit {
                counter = "METHOD"
                value = "COVEREDRATIO"
                minimum = "0.98".toBigDecimal()
            }

            limit {
                counter = "BRANCH"
                value = "COVEREDRATIO"
                minimum = "0.98".toBigDecimal()
            }

            limit {
                counter = "COMPLEXITY"
                value = "COVEREDRATIO"
                minimum = "0.98".toBigDecimal()
            }
        }
    }
}
