# Changelog

## [1.1.0](https://github.com/safe-slope/lock-service/compare/lock-microservice-v1.0.0...lock-microservice-v1.1.0) (2026-01-18)


### Features

* Added configuration for automated releases using `release-please` ([16b0ad3](https://github.com/safe-slope/lock-service/commit/16b0ad3eae8aceda9f94d70e4de865a424f91c4f))


### Bug Fixes

* Updated JWT claim parsing to use `verifyWith` and `parseSignedClaims` for improved security ([bac9191](https://github.com/safe-slope/lock-service/commit/bac91915b7d40a8c8d4cae0538dbab8b56b97374))


### Code Refactoring

* Refactored security module to simplify configuration and improve JWT handling ([81563fe](https://github.com/safe-slope/lock-service/commit/81563fefb67975d99e4bf758c179af1fd6897878))


### Build System

* added JUnit, PostgreSQL, and Mockito dependencies in `pom.xml` ([ac15626](https://github.com/safe-slope/lock-service/commit/ac156261fc72c40069c28a7fbda1a3ca3621fe4a))
* reorganized dependencies in `pom.xml`, added `mqtt-adapter`, `PostgreSQL`, and `spring-boot-starter-test` dependencies, and updated build configuration ([c32d6ec](https://github.com/safe-slope/lock-service/commit/c32d6ec5a80a2d373a5e8f41f3450ebafb246764))
