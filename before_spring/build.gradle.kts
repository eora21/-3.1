plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("mysql:mysql-connector-java:8.0.33")
    implementation("org.springframework:spring-context:6.1.1")
    implementation("org.springframework:spring-jdbc:6.1.1")
    implementation("org.assertj:assertj-core:3.24.2")
    implementation("org.springframework:spring-test:6.1.1")
}

tasks.test {
    useJUnitPlatform()
}