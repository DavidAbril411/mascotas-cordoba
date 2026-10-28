# mascotas-rest

Backend REST de Mascotas Córdoba para el Parcial III: **RF15 – Autenticar usuarios** (perfil Refugio), registro de refugios y **RF13 – Gestionar publicaciones de adopción**.

## Base de datos

Crear la base `mascotas_cba` y ejecutar `database/script-mascotas-cba.sql`. Crea todas las tablas del modelo lógico de la municipalidad, los procedimientos almacenados, las especies (Perro y Gato en `dominio_rasgos_mascotas`) y un usuario de prueba:

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

Las de `/publicaciones` llevan además `Authorization: Bearer <token>`, con el JWT que devuelve el login (`config/JwtFilter`). El token guarda el `idRefugio` del usuario, así cada refugio sólo ve y modifica sus publicaciones. Clave y duración en `security.jwt-secret` (`JWT_SECRET`) y `security.jwt-expiration` (`JWT_EXPIRATION`, en milisegundos; por defecto 8 horas). Sin token o con un token vencido responde 401 con el mensaje en el cuerpo.

| Método | URI | Body | Respuesta |
|---|---|---|---|
| POST | `/sesiones` | `{"cuil": "20123456789", "clave": "123456"}` | 200 con el usuario, su refugio y el `token` · 401 con el mensaje si las credenciales no son válidas |
| POST | `/refugios` | ver ejemplo | 200 con el `idRefugio` · 400 con el mensaje si faltan datos o el CUIL ya está registrado |
| GET | `/publicaciones/especies` | | 200 con `[{"codigo": 1, "nombre": "Perro"}, …]` |
| GET | `/publicaciones/listado` | | 200 con las publicaciones del refugio del token |
| POST | `/publicaciones/publicacion` | ver ejemplo | 200 con el `nroPublicacion` · 400 si faltan datos |
| POST | `/publicaciones/publicacion/{nroPublicacion}/{estadoPublicacion}` | | 200 · 400 si el estado no es `A` (Activa), `P` (Pausada) o `F` (Finalizada). Una publicación finalizada no cambia más |

Ejemplo de alta de publicación (`foto` es la imagen en base64 como data URL):

```json
{
  "nombre": "Manchita",
  "codEspecie": 1,
  "sexo": "H",
  "edad": 2,
  "raza": "Mestizo",
  "caracteristicasMascota": "Es muy cariñosa y tranquila.",
  "condicionAdopcion": "Se requiere entrevista previa.",
  "foto": "data:image/jpeg;base64,/9j/4AAQ..."
}
```

Cada alta crea la mascota (`mascotas`, con el refugio y el año de nacimiento calculado a partir de la edad), su especie y raza (`caracteristicas_mascotas`) y la publicación (`publicaciones_adopcion`, estado `A`) en un solo procedimiento, `dbo.ins_publicacion`.

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
  "razonSocial": "REFUGIO HUELLITAS",
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

El login valida las credenciales con `dbo.get_usuario_refugio` y firma el JWT en `config/JwtUtil` (librería `jjwt`). El frontend Angular guarda este usuario en el `localStorage` del navegador (`SesionService`) para mantener la sesión aunque se recargue la página, y su interceptor manda el token en cada llamada.

## CORS

`config/CorsConfig.java` es el de la profe (`personas-rest`), con `X-API-Key` agregado a los headers permitidos para que el portal Angular (puertos 4200 y 4201) pueda mandar la API Key. `Authorization`, que usa el JWT, ya venía en el de la profe.
