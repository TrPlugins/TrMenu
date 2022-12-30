plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}
dependencies {
    implementation(gradleApi())
    implementation(gradleKotlinDsl())
}
