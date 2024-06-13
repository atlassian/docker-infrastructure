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
[Unreleased]: https://github.com/atlassian/docker-infrastructure/compare/release-0.3.10...master

## [0.3.10] - 2024-06-13
[0.3.10]: https://github.com/atlassian/docker-infrastructure/compare/release-0.3.8...release-0.3.10

### Fixed
- Fix race condition between screen recording and stopping container.
- Wait for sample project key to be filled in before clicking submit.

## [0.3.8] - 2024-01-12
[0.3.8]: https://github.com/atlassian/docker-infrastructure/compare/release-0.3.7...release-0.3.8

### Fixed
- Fix java installation on MacOS. Aid [JPERF-1454].

[JPERF-1454]: https://ecosystem.atlassian.net/browse/JPERF-1454
 
## [0.3.7] - 2023-09-12
[0.3.7]: https://github.com/atlassian/docker-infrastructure/compare/release-0.3.6...release-0.3.7

### Fixed
- Use MP4 instead of FLV for video recording for video seek ability [JPERF-836]
 

## [0.3.6] - 2022-10-13
[0.3.6]: https://github.com/atlassian/docker-infrastructure/compare/release-0.3.5...release-0.3.6

### Fixed
- Update Jira license to proper one [JPERF-836]


[JPERF-836]: https://ecosystem.atlassian.net/browse/JPERF-836

## [0.3.5] - 2022-10-13
[0.3.5]: https://github.com/atlassian/docker-infrastructure/compare/release-0.3.4...release-0.3.5

### Changed
- Update Jira license [JPERF-836]
- Update testcontainers to 1.17.5 as old ryuk image wasn't available in dockerhub anymore


[JPERF-836]: https://ecosystem.atlassian.net/browse/JPERF-836

## [0.3.4] - 2022-04-10
[0.3.4]: https://github.com/atlassian/docker-infrastructure/compare/release-0.3.3...release-0.3.4

### Fixed
- Bump log4j dependency to 2.17.2. Fix [JPERF-773]

[JPERF-773]: https://ecosystem.atlassian.net/browse/JPERF-773

## [0.3.3] - 2020-02-20
[0.3.3]: https://github.com/atlassian/docker-infrastructure/compare/release-0.3.2...release-0.3.3

### Fixed
- Updated Ubuntu to the 2020 LTS version

## [0.3.2] - 2020-02-06
[0.3.2]: https://github.com/atlassian/docker-infrastructure/compare/release-0.3.1...release-0.3.2

### Fixed
- Added support for Jira Software
- updated Testing Containers

## [0.3.1] - 2020-02-03
[0.3.1]: https://github.com/atlassian/docker-infrastructure/compare/release-0.3.0...release-0.3.1

### Fixed
- Upgraded Selenium to 3.141.59
- A lot of stability fixes

## [0.3.0] - 2019-09-05
[0.3.0]: https://github.com/atlassian/docker-infrastructure/compare/release-0.2.0...release-0.3.0

### Added
- Record VNC video to diagnose WebDriver errors. Unblock [JPERF-566].

## [0.2.0] - 2019-08-30
[0.2.0]: https://github.com/atlassian/docker-infrastructure/compare/release-0.1.4...release-0.2.0

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
