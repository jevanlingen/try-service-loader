plugins {
    id("java")
    id("maven-publish")
}

group = "io.jacob"
version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    compileOnly("org.jspecify:jspecify:latest.release")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("-parameters")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "io.jacob"
            artifactId = "contest-api"
            version = "1.0-SNAPSHOT"

            from(components["java"])
        }
    }
}
