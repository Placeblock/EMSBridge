plugins {
    id("java")
    id("xyz.jpenilla.run-paper") version "2.3.1"
}

group = "de.codelix"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven {
        name = "codemc-snapshots"
        url = uri("https://repo.codemc.io/repository/maven-snapshots/")
    }
}

dependencies {
    // Lombok
    compileOnly("org.projectlombok:lombok:1.18.36")
    annotationProcessor("org.projectlombok:lombok:1.18.36")
    testCompileOnly("org.projectlombok:lombok:1.18.36")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.36")

    // Paper
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
    compileOnly("net.kyori:adventure-text-minimessage:4.18.0")

    // EMS
    implementation("de.codelix:entity-management-system:1.0-SNAPSHOT")

    // MySQL
    implementation("com.zaxxer:HikariCP:6.2.1")

    // Commands
    implementation("de.codelix.commandapi:core:4.0.0-SNAPSHOT")
    implementation("de.codelix.commandapi:paper:4.0.0-SNAPSHOT")

    // GUI
    implementation("de.codelix:BetterInventories:2.2.1")
    implementation("net.wesjd:anvilgui:1.10.4-SNAPSHOT")
}


tasks {
    runServer {
        // Configure the Minecraft version for our task.
        // This is the only required configuration besides applying the plugin.
        // Your plugin's jar (or shadowJar if present) will be used automatically.
        version = "1.21.4"
    }

    jar {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        dependsOn(configurations.runtimeClasspath)
        from({
            configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
        })
    }
}