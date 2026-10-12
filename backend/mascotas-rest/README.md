# mascotas-rest

Backend REST de Mascotas Córdoba para el Parcial III: **RF15 – Autenticar usuarios** (perfil Refugio) y registro de refugios.

## Base de datos

Crear la base `mascotas_cba` y ejecutar `database/script-mascotas-cba.sql`. Crea todas las tablas del modelo lógico de la municipalidad, los procedimientos almacenados y un usuario de prueba:

- CUIL `20123456789`, clave `123456` (refugio *REFUGIO HUELLITAS*).

Para la instancia de test de Docker, crear también `mascotas_cba_test` (cambiar el `use` de la primera línea del script).

## Ejecutar con Docker

```
mvn clean package
docker compose up --build
```

- `app-dev`: http://localhost:8085/api/v1 (base `mascotas_cba`)
- `app-test`: http://localhost:8086/api/v1 (base `mascotas_cba_test`)

Los contenedores se conectan al SQL Server de la PC mediante `host.docker.internal`.

## Endpoints

Todas las llamadas llevan el header `X-API-Key` (valor en `security.api-key` de `application.properties`, variable `API_KEY`).

| Método | URI | Body | Respuesta |
|---|---|---|---|
| POST | `/sesiones` | `{"cuil": "20123456789", "clave": "123456"}` | 200 con el usuario y su refugio · 401 si las credenciales no son válidas |
| POST | `/refugios` | ver ejemplo | 200 con el `idRefugio` · 400 si faltan datos o el CUIL ya está registrado |

Ejemplo de registro:

```json
{
  "razonSocial": "Refugio Patitas",
  "correo": "patitas@refugio.org",
  "telefono": "3515550000",
  "domicilio": "Calle 1 123",
  "habilitacionMunicipal": "HM-0002",
  "responsable": {
    "apellido": "Gomez",
    "nombre": "Ana",
    "cuil": "27234567894",
    "clave": "clave123",
    "correo": "ana@mail.com",
    "telefono": "3515551111",
    "domicilio": "Calle 2 456"
  }
}
```

Respuesta del login:

```json
{
  "idCiudadano": 1,
  "apellido": "PEREZ",
  "nombre": "JUAN",
  "cuil": "20123456789",
  "correo": "juan.perez@ubp.edu.ar",
  "idRefugio": 1,
  "razonSocial": "REFUGIO HUELLITAS"
}
```

El frontend Angular guarda este usuario para mantener el contexto de la sesión.
