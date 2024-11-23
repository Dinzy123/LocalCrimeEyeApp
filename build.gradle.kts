buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.google.services)  // Google services
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.0") // Kotlin plugin for Gradle
    }
}

plugins {
    alias(libs.plugins.androidApplication) apply false
}
