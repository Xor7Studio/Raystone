plugins {
    application
    kotlin("jvm") version "1.9.0"

}


group = "cn.xor7.code.raystone"
version = project.property("raystone_version")!!

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
}

kotlin {
    jvmToolchain(8)
}
