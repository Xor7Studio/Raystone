import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    `maven-publish`
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
    implementation("cn.zhxu:okhttps:4.0.1")
    implementation("cn.zhxu:okhttps-gson:4.0.1")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("net.peanuuutz.tomlkt:tomlkt:0.3.3")
    implementation("io.netty:netty-codec:4.1.87.Final")
}

kotlin {
    jvmToolchain(8)
}

tasks.withType<ShadowJar> {
    minimize()
    archiveFileName.set("../../../build/Raystone-API-${project.property("raystone_version")}.jar")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            groupId = group as String?
            artifactId = "raystone-api"
            version = version
        }
    }
    repositories {
        mavenLocal()
    }
}