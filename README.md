# 🏦 scotiabank-ms-user

Microservicio de gestión de usuarios desarrollado con **Spring WebFlux**, enfocado en registro, autenticación y administración de usuarios con seguridad basada en JWT.

---

## 🚀 Características

- Registro de usuarios
- Autenticación con JWT
- Validación de datos (email, password configurable)
- Persistencia reactiva con R2DBC
- Migraciones con Flyway
- Arquitectura hexagonal (Ports & Adapters)
- Seguridad con Spring Security WebFlux

---

## 🧱 Tecnologías

- Java 17+
- Spring Boot WebFlux
- Spring Security
- R2DBC (MySQL)
- Flyway
- JWT
- Lombok
- Maven

---

## 📦 Nombre del artefacto

El proyecto genera el siguiente `.jar`:
java -jar target/scotiabank-ms-user-1.0.jar

```bash
scotiabank-ms-user.jar
