plugins {
    id("application")
    id("java-library")
}

sourceSets {
    test {
        java.srcDir("src/test/java")
    }
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")

    implementation("com.google.guava:guava:30.1.1-jre")
}

application {
    mainClass.set("nbodies.NBodies")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}
