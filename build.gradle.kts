import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    application
    kotlin("jvm") version "1.9.0"
    id("com.github.johnrengelman.shadow") version "7.0.0"
}


group = "cn.xor7"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    implementation(kotlin("reflect"))
    implementation("io.javalin:javalin:5.6.2")
    implementation("com.alibaba:fastjson:2.0.39")
    implementation("cn.hutool:hutool-log:${project.property("hutool_version")}")
    implementation("cn.hutool:hutool-system:${project.property("hutool_version")}")
    implementation("com.github.oshi:oshi-core:6.4.4")
    implementation("org.reflections:reflections:0.10.2")
    implementation("ch.qos.logback:logback-classic:1.4.11")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<ShadowJar> {
    archiveFileName.set("Raystone-${project.property("raystone_version")}.jar")
    minimize()
}

kotlin {
    jvmToolchain(8)
}

application {
    mainClass.set("cn.xor7.raystone.MainKt")
}