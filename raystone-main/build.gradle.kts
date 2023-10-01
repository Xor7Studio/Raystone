import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    application
    id("java")
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.9.0"
    id("com.github.johnrengelman.shadow") version "8.0.0"
}

group = "cn.xor7.code.raystone"
version = project.property("raystone_version")!!

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib-jdk8"))
    implementation(project(":raystone-api"))
    implementation("io.javalin:javalin:5.6.2")
    implementation("com.alibaba:fastjson:2.0.39")
    implementation("com.github.oshi:oshi-core:6.4.4")
    implementation("net.peanuuutz.tomlkt:tomlkt:0.3.3")
    implementation("io.netty:netty-codec:4.1.87.Final")
    implementation("org.reflections:reflections:0.10.2")
    implementation("ch.qos.logback:logback-classic:1.4.11")
    implementation("cn.hutool:hutool-log:${project.property("hutool_version")}")
    implementation("cn.hutool:hutool-system:${project.property("hutool_version")}")
}

kotlin {
    jvmToolchain(8)
}

tasks.withType<ShadowJar> {
    archiveFileName.set("../../../build/Raystone-${project.property("raystone_version")}.jar")
}

tasks.shadowJar.get().doLast {
    File("raystone-main/build").deleteRecursively()
    File("raystone-api/build").deleteRecursively()
}

application {
    mainClass.set("cn.xor7.raystone.MainKt")
}