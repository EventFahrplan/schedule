plugins {
    id("buildsrc.convention.kotlin-jvm")
    alias(libs.plugins.kotlinPluginSerialization)
    alias(libs.plugins.mavenPublish)
}

dependencies {
    api(libs.threetenbp)
    implementation(libs.bundles.kotlinxEcosystem)
    implementation(libs.bundles.okttpEcosystem)
    implementation(libs.bundles.retrofitEcosystem)

    testImplementation(kotlin("test"))
    testImplementation(libs.bundles.testEcosystem)
    testImplementation(libs.jsonSchemaValidator)
}
