import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("java-library")
    id("java")
    id("maven-publish")
    id("signing")
    kotlin("jvm") version "2.0.0"
}

group = "me.obsilabor"
version = "1.1.0"

repositories {
    mavenCentral()
}

signing {
    useGpgCmd()
    sign(publishing.publications)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
    }
}

val jarThing by tasks.registering(Jar::class) {
    archiveClassifier.set("jar")
}

java {
    withSourcesJar()
    withJavadocJar()
}

publishing {
    kotlin.runCatching {
        repositories {
            maven("https://maven.kitpvp.world/public-releases") {
                name = "kitpvpWorldRepository"
                credentials(PasswordCredentials::class)
            }
        }
    }.onFailure {
        println("Unable to add publishing repositories: ${it.message}")
    }

    publications {
        create<MavenPublication>(project.name) {
            from(components["java"])
            artifact(jarThing.get())

            this.groupId = project.group.toString()
            this.artifactId = project.name.lowercase()
            this.version = project.version.toString()

            pom {
                name.set(project.name)
                description.set("Alert is a really simple and blazing fast event listening utility.")

                developers {
                    developer {
                        name.set("mooziii")
                    }
                }

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://github.com/mooziii/alert/blob/main/LICENSE")
                    }
                }

                url.set("https://github.com/KitPvP-World/alert")

                scm {
                    connection.set("scm:git:git://github.com/KitPvP-World/alert.git")
                    url.set("https://github.com/KitPvP-World/alert/tree/main")
                }
            }
        }
    }
}
