val kotlinVersion = "1.2.70"

plugins {
    kotlin("jvm").version("1.2.70")
    `java-library`
    id("com.atlassian.performance.tools.gradle-release").version("0.4.3")
}

configurations.all {
    resolutionStrategy {
        activateDependencyLocking()
        failOnVersionConflict()
        eachDependency {
            when (requested.module.toString()) {
                "org.slf4j:slf4j-api" -> useVersion("1.8.0-alpha2")
                "org.jetbrains:annotations" -> useVersion("13.0")
                "commons-codec:commons-codec" -> useVersion("1.10")
                "com.google.code.findbugs:jsr305" -> useVersion("1.3.9")
                "org.apache.httpcomponents:httpclient" -> useVersion("4.5.5")
                "org.apache.httpcomponents:httpcore" -> useVersion("4.4.9")
            }
        }
    }
}

dependencies {
    api("org.seleniumhq.selenium:selenium-support:3.11.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
    implementation("org.testcontainers:testcontainers:1.9.1")
    implementation("org.testcontainers:selenium:1.9.1")
    log4j(
        "api",
        "core",
        "slf4j-impl"
    ).forEach { implementation(it) }
    testCompile("junit:junit:4.12")
    testCompile("org.apache.httpcomponents:httpclient:4.5.5")
    testCompile("org.assertj:assertj-core:3.11.0")
}

fun log4j(
    vararg modules: String
): List<String> = modules.map { module ->
    "org.apache.logging.log4j:log4j-$module:2.10.0"
}

tasks.getByName("wrapper", Wrapper::class).apply {
    gradleVersion = "5.0"
    distributionType = Wrapper.DistributionType.ALL
}