[![GitHub Workflow Status (with event)](https://img.shields.io/github/actions/workflow/status/starichkov/micronaut-micro-service/maven.yml?style=for-the-badge)](https://github.com/starichkov/micronaut-micro-service/actions/workflows/maven.yml)
[![codecov](https://img.shields.io/codecov/c/github/starichkov/micronaut-micro-service?style=for-the-badge)](https://app.codecov.io/github/starichkov/micronaut-micro-service)
[![GitHub license](https://img.shields.io/github/license/starichkov/micronaut-micro-service?style=for-the-badge)](https://github.com/starichkov/micronaut-micro-service/blob/main/LICENSE.md)

Micronaut microservice
=
This project is a Micronaut framework based, 'ready-to-play' micro-service.

## Technical information

| Name      | Version |
|-----------|---------|
| Java      | 21      |
| Maven     | 3.8.1+  |
| Micronaut | 4.8.3   |

## GraalVM build

```
mvn clean package -Pgraalvm -Dpackaging=native-image -e -X
```

---

## Micronaut

- [Releases](https://github.com/micronaut-projects/micronaut-core/releases)
- [User Guide](https://docs.micronaut.io/latest/guide/index.html)
- [API Reference](https://docs.micronaut.io/latest/api/index.html)
- [Configuration Reference](https://docs.micronaut.io/latest/guide/configurationreference.html)
- [Micronaut Guides](https://guides.micronaut.io/index.html)

---

## Feature http-client documentation

- [Micronaut HTTP Client documentation](https://docs.micronaut.io/latest/guide/index.html#httpClient)

## License

See the [LICENSE](LICENSE.md) file for license rights and limitations (MIT).
