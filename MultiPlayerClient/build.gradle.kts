import org.gradle.internal.deployment.RunApplication
import org.gradle.internal.os.OperatingSystem

group = "MultiPlayerClient"
version = "1.0-SNAPSHOT"

plugins {
    java
}

tasks.register<JavaExec>("runClient1") {
    group = "run"
    description = "run client in localhost with a random port"
    classpath = sourceSets.main.get().runtimeClasspath
    main = "Main"
    args("localhost", "10000", "-1")
}

val lwjglVersion = "3.2.3"

val lwjglNatives = when (OperatingSystem.current()) {
    OperatingSystem.LINUX   -> System.getProperty("os.arch").let {
        if (it.startsWith("arm") || it.startsWith("aarch64"))
            "natives-linux-${if (it.contains("64") || it.startsWith("armv8")) "arm64" else "arm32"}"
        else
            "natives-linux"
    }
    OperatingSystem.MAC_OS  -> "natives-macos"
    OperatingSystem.WINDOWS -> if (System.getProperty("os.arch").contains("64")) "natives-windows" else "natives-windows-x86"
    else -> throw Error("Unrecognized or unsupported Operating system. Please set \"lwjglNatives\" manually")
}

repositories {
    maven {
        url = uri("https://repo.opennms.org/maven2/")
    }
    maven {
        url = uri("https://www.beatunes.com/repo/maven2/")
    }
    maven {
        url = uri("https://clojars.org/repo/")
    }
}

dependencies {
    implementation(platform("org.lwjgl:lwjgl-bom:$lwjglVersion"))

    implementation("javax.jnlp", "jnlp", version = "1.5.0")
    implementation("com.jcraft", "jogg", version = "0.0.7")
    implementation("org.jcraft", "jorbis", version = "0.0.17")
    implementation("org.clojars.aseipp", "ibxm", version = "0.0.1")
    implementation("net.java.jinput", "jinput", version = "2.0.9")
    implementation("net.java.jinput", "jinput-platform", version = "2.0.7")


    implementation("org.lwjgl", "lwjgl")
    implementation("org.lwjgl", "lwjgl-assimp")
    implementation("org.lwjgl", "lwjgl-bgfx")
    implementation("org.lwjgl", "lwjgl-glfw")
    implementation("org.lwjgl", "lwjgl-nanovg")
    implementation("org.lwjgl", "lwjgl-nuklear")
    implementation("org.lwjgl", "lwjgl-openal")
    implementation("org.lwjgl", "lwjgl-opengl")
    implementation("org.lwjgl", "lwjgl-par")
    implementation("org.lwjgl", "lwjgl-stb")
    implementation("org.lwjgl", "lwjgl-vulkan")
    runtimeOnly("org.lwjgl", "lwjgl", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-assimp", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-bgfx", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-glfw", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-nanovg", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-nuklear", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-openal", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-opengl", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-par", classifier = lwjglNatives)
    runtimeOnly("org.lwjgl", "lwjgl-stb", classifier = lwjglNatives)
    if (lwjglNatives == "natives-macos") runtimeOnly("org.lwjgl", "lwjgl-vulkan", classifier = lwjglNatives)
}