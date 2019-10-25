group = "MultiPlayerServer"
version = "1.0-SNAPSHOT"

plugins {
    java
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}


dependencies {
    implementation("org.eclipse.persistence", "moxy:2.5.0")
}