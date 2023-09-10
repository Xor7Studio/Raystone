import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java")
    kotlin("jvm")
    id("com.github.johnrengelman.shadow") version "8.0.0"
}

group = "cn.xor7.raystone"
version = project.property("raystone_version")!!

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.KenRouKoro:ctoml4j:1.0.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("cn.zhxu:okhttps:4.0.1")
    implementation("cn.zhxu:okhttps-gson:4.0.1")
    implementation("io.netty:netty-codec:4.1.97.Final")
    implementation(kotlin("stdlib-jdk8"))
}

kotlin {
    jvmToolchain(8)
}

tasks.withType<ShadowJar> {
    minimize()
    archiveFileName.set("../../../build/Raystone-API-${project.property("raystone_version")}.jar")
}