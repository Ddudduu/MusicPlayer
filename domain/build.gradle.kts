plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
    kotlin("kapt")
}
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("javax.inject:javax.inject:1")
    implementation("com.google.dagger:hilt-core:2.48")
    kapt("com.google.dagger:hilt-compiler:2.48")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
}
