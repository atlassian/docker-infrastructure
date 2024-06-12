val kotlinVersion = "1.3.20"
val seleniumVersion = "3.141.59"
val testContainersVersion = "1.17.5"
val log4jVersion = "2.17.2"

plugins {
    // gradle-release has to go first, see https://github.com/atlassian-labs/gradle-release/pull/5/commits/0e89b0646d3393e5cf2827240d64bc9bdc2060c1
    id("com.atlassian.performance.tools.gradle-release").version("0.9.0")
    kotlin("jvm").version("1.3.20")
    `java-library`
}

configurations.all {
    if (name.startsWith("dokka")) {
        return@all
    }
    resolutionStrategy {
        activateDependencyLocking()
        failOnVersionConflict()
        eachDependency {
            when (requested.module.toString()) {
                "commons-codec:commons-codec" -> useVersion("1.9")
                "org.jetbrains:annotations" -> useVersion("13.0")
                "org.slf4j:slf4j-api" -> useVersion("1.7.25")
            }
            when (requested.group) {
                "org.apache.logging.log4j" -> useVersion(log4jVersion)
            }
        }
    }
}

dependencies {
    api("org.seleniumhq.selenium:selenium-support:$seleniumVersion")
    implementation("com.atlassian.performance.tools:io:[1.0.0, 2.0.0)")
    implementation("org.seleniumhq.selenium:selenium-chrome-driver:$seleniumVersion")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
    implementation("org.testcontainers:testcontainers:$testContainersVersion")
    implementation("org.testcontainers:selenium:$testContainersVersion")
    listOf(
        "api",
        "core",
        "slf4j-impl"
    ).forEach { implementation("org.apache.logging.log4j:log4j-$it:$log4jVersion") }
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.apache.httpcomponents:httpclient:4.5.3")
    testImplementation("org.assertj:assertj-core:3.11.1")
}

tasks.wrapper {
    gradleVersion = "7.6.3"
    distributionType = Wrapper.DistributionType.ALL
}
