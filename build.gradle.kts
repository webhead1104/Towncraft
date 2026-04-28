import io.ebean.annotation.Platform
import net.kyori.indra.git.RepositoryValueSource
import org.eclipse.jgit.api.Git

plugins {
    java
    alias(libs.plugins.lombok)
    alias(libs.plugins.indra.git)
    alias(libs.plugins.blossom)
    id("net.cytonic.run-cytosis") version "1.0"
    alias(libs.plugins.shadow)
    alias(libs.plugins.ebean)
    id("net.cytonic.migration-generator") version "1.0-SNAPSHOT"
}

version = "0.0.1"

repositories {
    mavenCentral()
    maven("https://repo.opencollab.dev/maven-snapshots")
    maven("https://jitpack.io/")
    maven("https://repo.foxikle.dev/cytonic")
}

dependencies {
    compileOnly(libs.cytosis)
    implementation(libs.configurate.yaml)
    implementation(libs.mongodb)
    implementation(libs.lamp.common)
    implementation(libs.lamp.minestom)
    implementation(libs.commonsText)
    implementation(libs.classgraph)
}

migration {
    id = "towncraft"
    platform = Platform.POSTGRES
    entityPackages = listOf("me.webhead1104.towncraft")
}

tasks.withType<JavaCompile> {
    // Preserve parameter names in the bytecode
    options.compilerArgs.add("-parameters")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(25))
}

abstract class RepositoryUrlValueSource : RepositoryValueSource.Parameterless<String>() {
    override fun obtain(repository: Git): String? {
        return repository.repository.config.getString("remote", "origin", "url")
    }
}

val gitBranch: Provider<String> = indraGit.branchName().orElse("DEV")

val gitCommitAbbrev: Provider<String> = indraGit.commit().map { it.name?.substring(0, 7) ?: "0".repeat(7) }

val gitVersion: Provider<String> = gitBranch.zip(gitCommitAbbrev) { branch, commit ->
    "git-${branch}-${commit}"
}

tasks {
    runCytosis {
        cytosisVersion(libs.versions.cytosis.get())
        runDirectory.set(rootProject.layout.projectDirectory.dir("run/cytosis"))
    }
    shadowJar {
        archiveFileName.set("Towncraft-${project.version}.jar")
        archiveClassifier.set("")
        mergeServiceFiles()
    }
}

sourceSets {
    main {
        blossom {
            javaSources {
                property("version", "${project.version} (${gitVersion.get()})")
            }
        }
    }
}
