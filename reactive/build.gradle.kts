plugins {
    id("application")
    id("java-library")
    id("java")
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

    implementation("io.reactivex.rxjava3:rxjava:3.1.4")
}

application {
    mainClass.set("reactive.ReactiveParser")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}
