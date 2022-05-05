plugins {
    application
    id("java-library")
    java
    jacoco
}

sourceSets {
    test {
        java.srcDir("src/test/java")
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")

    implementation("com.google.guava:guava:30.1.1-jre")

    val vertx_version = "4.2.6"
    implementation("io.vertx:vertx-core:" + vertx_version)
    implementation("io.vertx:vertx-web:" + vertx_version)
    implementation("io.vertx:vertx-web-client:" + vertx_version)

    implementation("com.github.javaparser:javaparser-symbol-solver-core:3.24.2")
}

application {
    mainClass.set("parser.AsyncParser")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}
