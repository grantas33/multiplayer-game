group = "MultiPlayerServer"
version = "1.0-SNAPSHOT"

plugins {
    java
}

tasks.register<JavaExec>("runServer") {
    group = "run"
    description = "run server with port 10000"
    classpath = sourceSets.main.get().runtimeClasspath
    main = "server.Main"
    args("10000")
}

dependencies {
    implementation("org.eclipse.persistence", "org.eclipse.persistence.moxy", version = "2.5.0")
}