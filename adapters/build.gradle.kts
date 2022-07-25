plugins {
    id("org.jetbrains.kotlin.jvm") version "1.6.21"
    id("org.jetbrains.kotlin.kapt") version "1.6.21"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.6.21"
    id("io.micronaut.library") version "3.4.1"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.6.21"
}

group = "br.com.igorssk"
version = "0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":application"))
    implementation(project(":common"))
    implementation("io.micronaut:micronaut-jackson-databind")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
    implementation("jakarta.annotation:jakarta.annotation-api")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.7.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.7.0")
    runtimeOnly("ch.qos.logback:logback-classic")
    implementation("io.micronaut:micronaut-validation")

    //AWS
    implementation("com.amazonaws:aws-lambda-java-events:3.11.0")
    implementation("aws.sdk.kotlin:cognitoidentityprovider:0.17.1-beta")


    implementation("io.micronaut.aws:micronaut-function-aws")

    runtimeOnly("com.fasterxml.jackson.module:jackson-module-kotlin")

    testImplementation("io.micronaut:micronaut-http-client")
}

//java {
//    sourceCompatibility = JavaVersion.toVersion("11")
//}
//
//tasks {
//    compileKotlin {
//        kotlinOptions {
//            jvmTarget = "11"
//        }
//    }
//    compileTestKotlin {
//        kotlinOptions {
//            jvmTarget = "11"
//        }
//    }
//}
micronaut {
    runtime("lambda_java")
    testRuntime("junit5")
    processing {
        incremental(true)
        annotations("br.com.igorssk.*")
    }
}
//
//tasks.getByName<Test>("test") {
//    useJUnitPlatform()
//}