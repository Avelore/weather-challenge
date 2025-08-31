plugins {
    kotlin("jvm") version "2.0.0"
    application
}

repositories {
    mavenCentral()
}

dependencies {
    // HTTP client + API layer
    implementation("com.squareup.retrofit2:retrofit:2.11.0")            // Retrofit core (HTTP client abstraction)
    implementation("com.squareup.retrofit2:converter-moshi:2.11.0")     // JSON to Kotlin via Moshi
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")   // logs HTTP requests/responses
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set("MainKt")
}
