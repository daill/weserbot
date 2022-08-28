/*
 * Copyright 2021 Christian Kramer
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files
 *  (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge,
 *  publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do
 *  so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
 * LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO
 * EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

plugins {
    val springBootVersion = "2.5.0"
    val kotlinVersion = "1.7.0"
    val dependencyManagement = "1.0.11.RELEASE"


    kotlin("jvm") version kotlinVersion
    application
    idea
    java
    id("io.spring.dependency-management") version dependencyManagement
    id("org.springframework.boot") version springBootVersion
    id("org.jetbrains.kotlin.plugin.spring") version kotlinVersion
    id("org.jetbrains.kotlin.plugin.allopen") version kotlinVersion
    id("org.jetbrains.kotlin.plugin.noarg") version kotlinVersion
    id("org.jetbrains.kotlin.plugin.jpa") version kotlinVersion
    id("org.jetbrains.kotlin.kapt") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion
}

group = "de.daill"
version = "0.2.4"

repositories {
    mavenCentral()
    maven { setUrl("https://jitpack.io") }
}

sourceSets {
    map { it.java.srcDir("src/${it.name}/kotlin") }
}

allOpen {
    annotation("de.daill")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.springframework.boot:spring-boot-starter:2.5.0")
    implementation("org.springframework.boot:spring-boot-starter-websocket:2.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.8.+")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    testImplementation(platform("org.junit:junit-bom:5.7.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}
