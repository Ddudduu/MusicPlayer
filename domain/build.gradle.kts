plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
    kotlin("kapt")
}
java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("javax.inject:javax.inject:1")
    implementation("com.google.dagger:hilt-core:2.56.1")
    kapt("com.google.dagger:hilt-compiler:2.56.1")
}
