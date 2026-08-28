plugins {
    id("java-library")
    kotlin("jvm")
    kotlin("plugin.serialization")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation("javax.inject:javax.inject:1")
    implementation("com.squareup.okhttp3:mockwebserver:5.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.11.0")
    implementation("com.dampcake:bencode:1.4.2")
}
