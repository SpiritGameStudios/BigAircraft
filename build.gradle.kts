plugins {
    java
    `maven-publish`
    idea

    alias(libs.plugins.moddevgradle)
}

val modId = "aerobig"

version = "1.5.0"
group = "dev.spiritstudios.aerobig"

base.archivesName = modId

java.toolchain.languageVersion = JavaLanguageVersion.of(21)

neoForge {
    version = libs.versions.neoforge.get()

    parchment {
        mappingsVersion = libs.versions.parchment.get()
        minecraftVersion = libs.versions.minecraft.get()
    }

    runs {
        register("client") {
            client()
            systemProperty("neoforge.enabledGameTestNamespaces", modId)
        }

        register("server") {
            server()
            programArgument("--nogui")
            systemProperty("neoforge.enabledGameTestNamespaces", modId)
        }

        register("data") {
            data()
            programArguments.addAll(
                "--mod",
                modId,
                "--all",
                "--output",
                file("src/generated/resources/").absolutePath,
                "--existing",
                file("src/main/resources/").absolutePath
            )
        }

        configureEach {
            systemProperty("forge.logging.markers", "REGISTRIES")
            logLevel = org.slf4j.event.Level.INFO
        }
    }

    mods {
        register(modId) {
            sourceSet(sourceSets.main.get())
        }
    }
}

sourceSets.main.get().resources { srcDir("src/generated/resources") }

repositories {
    maven("https://maven.blamejared.com") { name = "BlameJared Maven" }
    maven("https://maven.createmod.net/") { name = "createmod Maven" }
    maven("https://mvn.devos.one/snapshots") { name = "Registrate" }
    maven("https://maven.ryanhcode.dev/releases/") { name = "Ryan's Public Maven" }
    maven("https://raw.githubusercontent.com/Fuzss/modresources/main/maven/")

    maven("https://maven.theillusivec4.top/")
    maven("https://api.modrinth.com/maven") {
        name = "Modrinth"
        content { includeGroup("maven.modrinth") }
    }

    maven("https://maven.squiddev.cc") {
        name = "SquidDev"
        content { includeGroup("cc.tweaked") }
    }
}

dependencies {
    // region Required deps
    implementation(libs.simulated)
    implementation(libs.aeronautics)
    implementation(libs.offroad)

    implementation(variantOf(libs.create) { classifier("slim") }) {
        isTransitive = false
    }

    implementation(libs.sable)
    implementation(libs.sable.companion)

    implementation(libs.registrate)

    compileOnly(libs.flywheel.api)
    implementation(libs.flywheel)
    // endregion

    // region Optional deps
    compileOnly(libs.computercraft.core.api)
    compileOnly(libs.computercraft.forge.api)
    runtimeOnly(libs.computercraft.forge)
    // endregion
}

var generateModMetadata = tasks.register<ProcessResources>("generateModMetadata") {
    val replaceProperties = mapOf(
        "mod_id" to modId,
        "mod_version" to version,
        "minecraft_version" to libs.versions.minecraft.get(),
        "neoforge_version" to libs.versions.neoforge.get()
    )

    inputs.properties(replaceProperties)
    expand(replaceProperties)
    from("src/main/templates")
    into("build/generated/sources/modMetadata")
}

sourceSets.main.get().resources.srcDir(generateModMetadata)
neoForge.ideSyncTask(generateModMetadata)

idea {
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}