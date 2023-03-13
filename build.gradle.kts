import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-library`
    kotlin("jvm") version "1.7.21"
}

group = "org.example"
version = "0.1.2"

repositories {
    mavenCentral()
}

dependencies {
    api("io.github.rossetti:KSLCore:R1.0.1")
    implementation("org.apache.commons:commons-math3:3.6.1")
    implementation("org.apache.commons:commons-numbers-gamma:1.1")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

kotlin {
    explicitApi()
}