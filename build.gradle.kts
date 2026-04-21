import net.kyori.indra.git.RepositoryValueSource
import org.eclipse.jgit.api.Git

plugins {
    java
    alias(libs.plugins.lombok)
    alias(libs.plugins.indra.git)
    alias(libs.plugins.blossom)
}

repositories {
    mavenCentral()
    maven("https://repo.opencollab.dev/maven-snapshots")
    maven("https://jitpack.io/")
    maven("https://repo.foxikle.dev/cytonic")
}

dependencies {
    implementation(libs.inventoryFramework.api)
    implementation(libs.inventoryFramework.platform)
    compileOnly(libs.cytosis) {
        exclude("me.devnatan")
        exclude("net.cytonic.minestomInventoryFramework")
    }
    implementation(libs.configurate.yaml)
    implementation(libs.mongodb)
    implementation(libs.lamp.common)
    implementation(libs.lamp.minestom)
    implementation(libs.commonsText)
    implementation(libs.classgraph)
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

sourceSets {
    main {
        blossom {
            javaSources {
                property("version", "${project.version} (${gitVersion.get()})")
            }
        }
    }
}
