plugins {
    kotlin("jvm") version "1.5.30"
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "io.github.blugon0921"
version = "1.0"



repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://oss.sonatype.org/content/groups/public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.17.1-R0.1-SNAPSHOT")

    implementation("net.kyori:adventure-api:4.7.0")
}

tasks {
    processResources {
        filesMatching("*.yml") {
            expand(project.properties)
        }
    }

    shadowJar {
        from(sourceSets["main"].output)
        archiveBaseName.set(project.name)
        archiveVersion.set("")
        archiveFileName.set("${project.name}.jar")

        doLast {
            copy {
                from(archiveFile)

                //Save Location
                val plugins = File("C:/Users/blugo/바탕화면/Files/Minecraft/Servers/StoneBattle/plugins")
                into(plugins)
            }
        }
    }
}