plugins {
    id("fabric-loom") version "1.0-SNAPSHOT"
    id("org.quiltmc.quilt-mappings-on-loom") version "4.2.0"
    id("io.github.juuxel.loom-quiltflower") version "1.7.+"
    id("io.github.p03w.machete") version "1.+"
    id("org.cadixdev.licenser") version "0.6.1"
}

apply(from = "https://raw.githubusercontent.com/JamCoreModding/Gronk/main/publishing.gradle.kts")
apply(from = "https://raw.githubusercontent.com/JamCoreModding/Gronk/main/misc.gradle.kts")

val mod_version: String by project

group = "io.github.jamalam360"
version = mod_version

sourceSets {
    val main = this.getByName("main")

    create("testmod") {
        this.compileClasspath += main.compileClasspath
        this.compileClasspath += main.output
        this.runtimeClasspath += main.runtimeClasspath
        this.runtimeClasspath += main.output
    }
}

loom {
    runs {
        create("testClient") {
            client()
            name("Testmod Client")
            source(sourceSets.getByName("testmod"))
            runDir("run/test/client")
        }

        create("testServer") {
            server()
            name("Testmod Server")
            source(sourceSets.getByName("testmod"))
            runDir("run/test/server")
        }

        getByName("client") {
            runDir("run/client")
        }

        getByName("server") {
            runDir("run/server")
        }
    }
}

repositories {
    val mavenUrls = mapOf(
        Pair("https://maven.terraformersmc.com/releases", listOf("com.terraformersmc"))
    )

    for (mavenPair in mavenUrls) {
        maven {
            url = uri(mavenPair.key)
            content {
                for (group in mavenPair.value) {
                    includeGroup(group)
                }
            }
        }
    }
}

dependencies {
    minecraft(libs.minecraft)
    mappings(loom.layered {
        addLayer(quiltMappings.mappings("org.quiltmc:quilt-mappings:${libs.versions.minecraft.get()}+build.${libs.versions.mappings.build.get()}:v2"))
    })

    modImplementation(libs.fabric.loader)
    modImplementation(libs.fabric.api)

    modApi(libs.optional.mod.menu)
}

