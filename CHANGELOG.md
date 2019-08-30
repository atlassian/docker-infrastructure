# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/en/1.0.0/)
and this project adheres to [Semantic Versioning](http://semver.org/spec/v2.0.0.html).

## API
The API consists of all public Java types from `com.atlassian.performance.tools.dockerinfrastructure.api` and its subpackages:

  * [source compatibility]
  * [binary compatibility]
  * [behavioral compatibility] with behavioral contracts expressed via Javadoc

[source compatibility]: http://cr.openjdk.java.net/~darcy/OpenJdkDevGuide/OpenJdkDevelopersGuide.v0.777.html#source_compatibility
[binary compatibility]: http://cr.openjdk.java.net/~darcy/OpenJdkDevGuide/OpenJdkDevelopersGuide.v0.777.html#binary_compatibility
[behavioral compatibility]: http://cr.openjdk.java.net/~darcy/OpenJdkDevGuide/OpenJdkDevelopersGuide.v0.777.html#behavioral_compatibility

### POM
Changing the license is breaking a contract.
Adding a requirement of a major version of a dependency is breaking a contract.
Dropping a requirement of a major version of a dependency is a new contract.

## [Unreleased]
[Unreleased]: https://github.com/atlassian/docker-infrastructure/compare/release-0.1.4...master

### Added
- Diagnose WebDriver errors during Jira Core setup. Unblock [JPERF-566].

[JPERF-566]: https://ecosystem.atlassian.net/browse/JPERF-566

## [0.1.4] - 2019-08-29
[0.1.4]: https://github.com/atlassian/docker-infrastructure/compare/release-0.1.3...release-0.1.4

### Fixed
- Close popups one by one. And wait for them to slide out. Fix [JPERF-565].

[JPERF-565]: https://ecosystem.atlassian.net/browse/JPERF-565

## [0.1.3] - 2019-08-29
[0.1.3]: https://github.com/atlassian/docker-infrastructure/compare/release-0.1.2...release-0.1.3

### Fixed
- Stop putting the license into the admin user name. Fix [JPERF-561].

[JPERF-561]: https://ecosystem.atlassian.net/browse/JPERF-561

## [0.1.2] - 2019-01-15
[0.1.2]: https://github.com/atlassian/docker-infrastructure/compare/release-0.1.1...release-0.1.2

### Fixed
- Provision jira with a default dataset.

### Added
- `JiraCoreFormula` can build `Jira` that is accessible from docker or host.

### Removed
- Remove `getDockerUri` method from `Jira` interface.

## [0.1.1] - 2019-01-10
[0.1.1]: https://github.com/atlassian/docker-infrastructure/compare/release-0.1.0...release-0.1.1

## Fixed
- Stop to use deprecated API.

## [0.1.0] - 2018-12-13
[0.1.0]: https://github.com/atlassian/docker-infrastructure/compare/initial-commit...release-0.1.0

### Added
- Jira Core formula.
- Dockerised Chrome browser.
