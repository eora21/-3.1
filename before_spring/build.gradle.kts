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
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
    implementation("mysql:mysql-connector-java:8.0.33")
    implementation("org.springframework:spring-context:6.1.1")
    implementation("org.springframework:spring-jdbc:6.1.1")
    testImplementation("org.assertj:assertj-core:3.24.2")
    testImplementation("org.springframework:spring-test:6.1.1")
    testImplementation("org.mockito:mockito-junit-jupiter:5.8.0")
    testImplementation("org.springframework.boot:spring-boot-test:3.2.0")
    implementation("org.springframework.boot:spring-boot-starter-mail:3.2.0")
    implementation("org.aspectj:aspectjweaver:1.9.20")
    implementation("jakarta.xml.bind:jakarta.xml.bind-api:4.0.1")
    implementation("org.glassfish.jaxb:jaxb-runtime:4.0.4")
    implementation("org.springframework:spring-oxm:6.1.1")
    implementation("com.h2database:h2:2.2.224")
}

tasks.test {
    useJUnitPlatform()
}