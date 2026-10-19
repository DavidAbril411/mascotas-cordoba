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
| POST | `/sesiones` | `{"cuil": "20123456789", "clave": "123456"}` | 200 con el usuario, su refugio y el `idSesion` · 401 con el mensaje si las credenciales no son válidas |
| POST | `/refugios` | ver ejemplo | 200 con el `idRefugio` · 400 con el mensaje si faltan datos o el CUIL ya está registrado |

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
  "idSesion": "5ACDE4CE-BE93-41D3-838B-344868473A51",
  "idCiudadano": 1,
  "apellido": "PEREZ",
  "nombre": "JUAN",
  "cuil": "20123456789",
  "correo": "juan.perez@ubp.edu.ar",
  "idRefugio": 1,
  "razonSocial": "REFUGIO HUELLITAS"
}
```

Cada login exitoso graba una fila en la tabla `sesiones` (agregada al modelo lógico, no modifica las tablas de la profe) mediante `dbo.ins_sesion`. El frontend Angular guarda este usuario en `SesionService` para mantener el contexto de la sesión.

## CORS

`config/CorsConfig.java` es el de la profe (`personas-rest`), con `X-API-Key` agregado a los headers permitidos para que el portal Angular (puertos 4200 y 4201) pueda mandar la API Key.
