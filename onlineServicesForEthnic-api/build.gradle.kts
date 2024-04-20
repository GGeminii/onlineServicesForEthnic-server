plugins {
    id("java")
}

group = "com.intelliPark"
version = "1.0.0"

repositories {
    maven {
        url = uri("https://maven.aliyun.com/repository/public/")
    }
    maven {
        url = uri("https://maven.aliyun.com/repository/spring/")
    }
    mavenLocal()
    mavenCentral()
}


dependencies {
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}