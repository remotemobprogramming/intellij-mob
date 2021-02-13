/*
 * Copyright 2020-2021 Koji Hasegawa. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

plugins {
    id("org.jetbrains.intellij") version "0.6.5"
    java
    kotlin("jvm") version "1.4.21"
    id("com.palantir.git-version") version "0.12.3"
    jacoco
    id("io.gitlab.arturbosch.detekt") version "1.11.0"
}

group = "com.nowsprinting"

val gitVersion: groovy.lang.Closure<String> by extra
val suppressPrefix = { s: Any -> (s as String).replace("^v".toRegex(), "") }
version = suppressPrefix(gitVersion())

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
    version = "2020.3"
    setPlugins("git4idea")
}
configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
detekt {
    config = files("$projectDir/config/detekt/detekt.yml")
    reports {
        html.enabled = true // observe findings in your browser with structure and code snippets
        xml.enabled = true // checkstyle like format mainly for integrations like Jenkins
        txt.enabled = false // similar to the console output, contains issue signature to manually edit baseline files
    }
}
tasks {
    // Set the compatibility versions to 1.8
    withType<JavaCompile> {
        sourceCompatibility = "1.8"
        targetCompatibility = "1.8"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    withType<io.gitlab.arturbosch.detekt.Detekt> {
        jvmTarget = "1.8"
    }

    test {
        useJUnitPlatform {
            includeEngines("junit-jupiter")
        }
        finalizedBy(jacocoTestReport)   // report is always generated after tests run
    }
    jacocoTestReport {
        reports {
            xml.isEnabled = true
            csv.isEnabled = false
            html.destination = file("${buildDir}/reports/jacocoHtml")
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
    sinceBuild("203")
    untilBuild(null)
}

/**
 * Create latest version change notes from CHANGELOG.md
 */
fun changeNotesFromChangeLog(): String {
    val builder = StringBuilder()
    builder.append("<ul>")
    builder.append(latestChangeLog())
    builder.append("</ul>")
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