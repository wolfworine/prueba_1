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

---

## 🐳1. Levantar MySQL en Docker

Tu contenedor existe, pero falta lo importante: variables + DB + usuario.
Comando:

docker run -d \
  --name mysql-db \
  -p 3306:3306 \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=UserDB \
  -e MYSQL_USER=user \
  -e MYSQL_PASSWORD=123456 \
  mysql:lts

---

## 🧱 2. Verifica que esté corriendo
docker ps

Debe salir algo como:

mysql-db   0.0.0.0:3306->3306/tcp

## 🔍 3. Conectarte a MySQL (desde tu máquina)
docker exec -it mysql-db mysql -u user -p
Password:123456

## 🗄️ 4. Verifica la base
Create Data UserDB;
SHOW DATABASES;
USE UserDB;
