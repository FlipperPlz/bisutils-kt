plugins {
  id("org.jetbrains.kotlin.jvm") version "1.8.10"
  id("org.jetbrains.dokka") version "1.8.10"
}

group = "com.flipperplz"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("com.google.guava:guava:30.0-jre")
    implementation(kotlin("reflect"))
}

tasks {
    jar {

    }

    test {
        useJUnitPlatform()
    }
}

kotlin {
    jvmToolchain(11)
}
