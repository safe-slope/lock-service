# Changelog

## [1.3.3](https://github.com/safe-slope/lock-service/compare/lock-microservice-v1.3.2...lock-microservice-v1.3.3) (2026-01-30)


### Bug Fixes

* added parameters plugin to parent pom ([bc8af76](https://github.com/safe-slope/lock-service/commit/bc8af768726057bb891ebdc7d8a01e4811da634f))
* added parameters plugin to parent pom ([d418049](https://github.com/safe-slope/lock-service/commit/d41804973843e9d95a34aefb19dda4f1b5423dd8))
* remove wrong Maven compiler properties from security module pom ([045448b](https://github.com/safe-slope/lock-service/commit/045448b1eaedcbdb7c0f2fa865e362503ec6149d))

## [1.3.2](https://github.com/safe-slope/lock-service/compare/lock-microservice-v1.3.1...lock-microservice-v1.3.2) (2026-01-29)


### Bug Fixes

* change JPA ddl-auto config in staging to `update` for schema management ([f178732](https://github.com/safe-slope/lock-service/commit/f178732f40f2b181e16bf4db2b5f63e12fc0f3a8))

## [1.3.1](https://github.com/safe-slope/lock-service/compare/lock-microservice-v1.3.0...lock-microservice-v1.3.1) (2026-01-29)


### Bug Fixes

* extend kubectl rollout timeout in test-and-publish workflow ([953b037](https://github.com/safe-slope/lock-service/commit/953b0372eff37d24d755cfbe0babaa271cabcce9))

## [1.3.0](https://github.com/safe-slope/lock-service/compare/lock-microservice-v1.2.2...lock-microservice-v1.3.0) (2026-01-29)


### Features

* enhance CI-CD pipeline with release-based conditional steps and staging deployment ([59e5a08](https://github.com/safe-slope/lock-service/commit/59e5a08b702e05f9f6b4b714d3ffff1effa76f53))

## [1.2.2](https://github.com/safe-slope/lock-service/compare/lock-microservice-v1.2.1...lock-microservice-v1.2.2) (2026-01-27)


### Bug Fixes

* update GitHub Actions to conditionally run Qodana and remove redundant 'needs' dependency ([fdc1e3d](https://github.com/safe-slope/lock-service/commit/fdc1e3d7e8227a5475aa49142caa27e768df0127))

## [1.2.1](https://github.com/safe-slope/lock-service/compare/lock-microservice-v1.2.0...lock-microservice-v1.2.1) (2026-01-27)


### Bug Fixes

* update .dockerignore to exclude `.vscode` folder ([425039c](https://github.com/safe-slope/lock-service/commit/425039c1908babf65e57b7ee339da69ccef4d871))

## [1.2.0](https://github.com/safe-slope/lock-service/compare/lock-microservice-v1.1.0...lock-microservice-v1.2.0) (2026-01-27)


### Features

* configure Spring Boot Actuator for health checks and update security rules ([2e57967](https://github.com/safe-slope/lock-service/commit/2e57967e20aa5cbcd4221974549a869b6b5c98b6))
* configure Spring Boot Actuator for health checks and update security rules ([7e12f21](https://github.com/safe-slope/lock-service/commit/7e12f215d2129372e8aac2e3171de1f9772accf1))

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
