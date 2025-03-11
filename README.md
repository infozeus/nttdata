# Ejercicio Application API Rest

Esta es una aplicación de ejemplo para la gestión de usuarios. La aplicación está construida con Spring Boot y proporciona una API REST para crear y gestionar usuarios.

## Requisitos

- Java 11 o superior
- Maven 3.6.3 o superior
- Spring Boot 3
- Spring Security
- JWT (Json Web Token)
- Spring Data JPA
- H2 Database (Modo desarrollo)
-  Swagger (Springdoc OpenAPI)


## Configuración

1. Clona el repositorio:

```bash
git clone https://github.com/infozeus/smartjob.git
cd ejercicio
```

2. Configura las propiedades de la aplicación en el archivo `src/main/resources/application.properties`:

# JWT Secret
jwt.secret=your_secret_key_here

# Regex para la contraseña
password.regex=^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#\\$%\\^&\\*])(?=.{8,})
```

## Compilar y Ejecutar la Aplicación

1. Compila la aplicación usando Maven:

```bash
mvn clean install
```

2. Ejecuta la aplicación:

```bash
mvn spring-boot:run
```

La aplicación estará disponible en `http://localhost:8080`.

## Probar la Aplicación

### Crear un Usuario

Puedes usar `curl` o cualquier herramienta de cliente HTTP (como Postman) para probar la API.

```bash
curl -X POST http://localhost:8080/api/usuarios/crear \
    -H "Content-Type: application/json" \
    -d '{
          "name": "Juan Rodriguez",
          "email": "juan.rodriguez@correo.com",
          "password": "Hunter2025",
          "phones": [
            {
              "number": "1234567",
              "citycode": "1",
              "contrycode": "57"
            }
          ]
        }'
```

### Respuesta Esperada

```json
{
  "id": "UUID",
  "name": "Juan Rodriguez",
  "email": "juan.rodriguez@correo.com",
  "created": "2025-03-10T10:00:00",
  "modified": "2025-03-10T10:00:00",
  "last_login": "2025-03-10T10:00:00",
  "token": "jwt_token",
  "isactive": true,
  "phones": [
    {
      "number": "1234567",
      "citycode": "1",
      "contrycode": "57"
    }
  ]
}
```

## Ejecutar Pruebas

Para ejecutar las pruebas unitarias, usa el siguiente comando:

```bash
mvn test
```

## Documentación de la API

La documentación de la API está disponible en `http://localhost:8080/swagger-ui.html` una vez que la aplicación esté en ejecución.

