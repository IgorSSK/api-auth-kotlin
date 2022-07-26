dependencies {
    implementation(project(":application"))
    implementation(project(":common"))
    //AWS
    implementation("com.amazonaws:aws-lambda-java-events:3.11.0")
    implementation("aws.sdk.kotlin:cognitoidentityprovider:0.17.1-beta")
    //MICRONAUT
    implementation("io.micronaut.aws:micronaut-function-aws")
}

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