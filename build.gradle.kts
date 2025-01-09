plugins {
	id("fabric-loom") version "1.9-SNAPSHOT"
	id("maven-publish")
}

fun getGitCommit(): String {
	val exec = providers.exec {
		commandLine("git", "rev-parse", "--short", "HEAD")
	}
	return exec.standardOutput.asText.get().trim()
}

val archivesBaseName = "clearskies"
version = "1.5.2+fabric.${getGitCommit()}"
group = "me.grondag"

dependencies {
	minecraft("com.mojang:minecraft:1.21.4")
	mappings(loom.officialMojangMappings())

	modImplementation("net.fabricmc:fabric-loader:0.16.10")
}

java {
	withSourcesJar()
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
		vendor = JvmVendorSpec.ADOPTIUM
	}
}

publishing {
	publications.create<MavenPublication>("maven") {
		artifactId = project.name.lowercase()
		from(components["java"])
	}
}

tasks {
	processResources {
		inputs.property("version", project.version)
		filesMatching("fabric.mod.json") {
			expand("version" to project.version)
		}
	}

	withType<JavaCompile>() {
		options.encoding = Charsets.UTF_8.name()
		options.release = 21
	}

	jar {
		from("LICENSE") {
			rename { return@rename "${it}_clearskies" }
		}
	}
}
