plugins {
    application
    kotlin("jvm") version "1.9.0"

}


group = "cn.xor7"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
}

kotlin {
    jvmToolchain(8)
}
