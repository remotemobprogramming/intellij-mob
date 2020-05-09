/*
 * Copyright 2020 Koji Hasegawa. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

plugins {
    id("org.jetbrains.intellij") version "0.4.18"
    java
    kotlin("jvm") version "1.3.71"
    id("com.palantir.git-version") version "0.12.3"
}

group = "com.nowsprinting"

val makeVersion = {
    val githubRef = System.getenv("GITHUB_REF") ?: ""
    val match = Regex("""^refs/tags/v(.+)$""").matchEntire(githubRef)
    if (match != null) {
        match.groupValues[1]
    } else {
        val gitVersion: groovy.lang.Closure<String> by extra
        gitVersion().replace("^v".toRegex(), "")
    }
}
version = makeVersion()

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.0")
    testImplementation("io.mockk:mockk:1.9")
    testCompile("org.junit.jupiter", "junit-jupiter-api", "5.6.2")
    testRuntime("org.junit.jupiter", "junit-jupiter-engine", "5.6.2")
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    version = "2020.1"
    setPlugins("git4idea")
}
configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    test {
        useJUnitPlatform {
            includeEngines("junit-jupiter")
        }
    }
    publishPlugin {
        token(System.getenv("INTELLIJ_PUBLISH_TOKEN"))
        channels(arrayOf(System.getenv("INTELLIJ_PUBLISH_CHANNEL")))
    }
}
tasks.getByName<org.jetbrains.intellij.tasks.PatchPluginXmlTask>("patchPluginXml") {
    changeNotes(
        changeNotesFromChangeLog()
    )
}

/**
 * Create latest version change notes from CHANGELOG.md
 */
fun changeNotesFromChangeLog(): String {
    val builder = StringBuilder()
    builder.append("<ul>")
    builder.append(latestChangeLog())
    builder.append("</ul>")
    builder.append("<p>Older version changes are listed on <a href=\"https://github.com/remotemobprogramming/intellij-mob/blob/master/CHANGELOG.md\">CHANGELOG.md</a></p>")
    return builder.toString()
}

fun latestChangeLog(): String {
    val builder = StringBuilder()
    val changelog = File("CHANGELOG.md")
    var inLatestVersion = false
    changelog.bufferedReader().use() {
        it.lineSequence()
            .filter(String::isNotBlank)
            .forEach {
                if (it.startsWith("#")) {
                    if (inLatestVersion && builder.isNotEmpty()) {
                        return@use  // break, found older version
                    } else {
                        inLatestVersion = true
                        return@forEach  // continue next line
                    }
                }
                builder.append("<li>${it.substring(2)}</li>")
            }
    }
    return builder.toString()
}