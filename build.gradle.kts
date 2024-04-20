import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    id("org.springframework.boot") version "3.2.4"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.spring") version "1.9.22"
}

group = "com.onlineServicesForEthnic"
version = "1.0.0"

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

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


subprojects {
    apply {
        plugin("org.springframework.boot")
        plugin("io.spring.dependency-management")
        plugin("org.jetbrains.kotlin.jvm")
        plugin("org.jetbrains.kotlin.plugin.spring")
    }

    dependencies {
        ext {
            set("mybatis-flex", "1.8.7")
            set("knife4j", "4.5.0")
            set("hutool", "5.8.16")
            set("guava", "27.0.1-android")
            set("jwt", "0.9.1")
            set("jaxb-api", "2.3.1")
            set("oss", "3.15.1")
            set("activation", "1.1.1")
            set("jaxb-runtime", "2.3.3")
            set("common-pool2", "2.12.0")
            set("poi","5.2.3")
        }
        //spring-boot-starter
        //redis
        implementation("org.springframework.boot:spring-boot-starter-data-redis")
        //security
        implementation("org.springframework.boot:spring-boot-starter-security")
        //web
        implementation("org.springframework.boot:spring-boot-starter-web")
        //热部署
//        runtimeOnly("org.springframework.boot:spring-boot-devtools")
        //aop
        implementation("org.springframework.boot:spring-boot-starter-aop")
        //quartz
        implementation("org.springframework.boot:spring-boot-starter-quartz")

        //swagger
        implementation("com.github.xiaoymin:knife4j-openapi3-jakarta-spring-boot-starter:${ext.get("knife4j")}")
        implementation("com.github.xiaoymin:knife4j-dependencies:${ext.get("knife4j")}")

        //支持配置属性类，yml文件中可以提示配置项
        implementation("org.springframework.boot:spring-boot-configuration-processor")

        //lombok
        compileOnly("org.projectlombok:lombok")
        annotationProcessor("org.projectlombok:lombok")

        //hutool
        implementation("cn.hutool:hutool-all:${ext.get("hutool")}")

        //BiMap的引入
        implementation("com.google.guava:guava:${ext.get("guava")}")

        //mybatis
        runtimeOnly("com.mysql:mysql-connector-j")
        implementation("com.mybatis-flex:mybatis-flex-spring-boot3-starter:${ext.get("mybatis-flex")}")
        annotationProcessor("com.mybatis-flex:mybatis-flex-processor:${ext.get("mybatis-flex")}")
        implementation("com.zaxxer:HikariCP")
        //kotlin扩展库
        implementation("com.mybatis-flex:mybatis-flex-kotlin-extensions:1.0.8")
        //代码生成
        implementation("com.mybatis-flex:mybatis-flex-codegen:1.8.2")

        // jackson
        implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")


        //test
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("org.springframework.security:spring-security-test")
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs += "-Xjsr305=strict"
            jvmTarget = "21"
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}


project("onlineServicesForEthnic-dao") {
    dependencies {
        api(project(":onlineServicesForEthnic-pojo"))
    }
}

project("onlineServicesForEthnic-common") {
    dependencies {
        //aliyun-OSS
        implementation("com.aliyun.oss:aliyun-sdk-oss:${ext.get("oss")}")
        implementation("javax.activation:activation:${ext.get("activation")}")
        implementation("javax.xml.bind:jaxb-api:${ext.get("jaxb-api")}")
        implementation("org.glassfish.jaxb:jaxb-runtime:${ext.get("jaxb-runtime")}")

        implementation("org.apache.commons:commons-pool2:${ext.get("common-pool2")}")

        //poi
        implementation("org.apache.poi:poi:${ext.get("poi")}")
        implementation("org.apache.poi:poi-ooxml:${ext.get("poi")}")

        //dao交互数据库
        api(project(":onlineServicesForEthnic-dao"))
    }
}

project("onlineServicesForEthnic-service") {
    dependencies {
        api(project(":onlineServicesForEthnic-common"))
    }
}



project("onlineServicesForEthnic-api") {
    dependencies {
        // JWT
        implementation("io.jsonwebtoken:jjwt:${ext.get("jwt")}")
        implementation(project(":onlineServicesForEthnic-service"))
    }
}


