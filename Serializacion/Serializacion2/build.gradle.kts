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
    // https://mvnrepository.com/artifact/jakarta.xml.bind/jakarta.xml.bind-api
    implementation("jakarta.xml.bind:jakarta.xml.bind-api:4.0.2")
    // https://mvnrepository.com/artifact/org.glassfish.jaxb/jaxb-runtime
    implementation("org.glassfish.jaxb:jaxb-runtime:4.0.5")


}

tasks.test {
    useJUnitPlatform()
}