# mascotas-rest

Backend REST de Mascotas Córdoba: **RF15 – Autenticar usuarios** (perfil Refugio), registro de refugios, **RF13 – Gestionar publicaciones de adopción**, las consultas públicas del portal, **RF17 – Consultar veterinarias adheridas** y **RF18 – Consultar refugios adheridos**, y la **integración con las veterinarias** (**RF06 – Registrar mascotas** y **RF09 – Registrar información sanitaria**).

## Base de datos

Crear la base `mascotas_cba` y ejecutar `database/script-mascotas-cba.sql`. Crea todas las tablas del modelo lógico de la municipalidad, los procedimientos almacenados, las especies (Perro y Gato en `dominio_rasgos_mascotas`) y un usuario de prueba:

- CUIL `20123456789`, clave `123456` (refugio *REFUGIO HUELLITAS*).
- CUIL `27301234567`, clave `123456` (refugio *REFUGIO COLITAS FELICES*).

También carga los horarios de atención de los dos refugios y cinco veterinarias de prueba (cuatro activas y una inactiva, que no aparece en el portal). Las cuatro activas traen la configuración de la integración: la 1 y la 2 por REST, la 3 y la 4 por SOAP.

El script borra y vuelve a crear todas las tablas: cada vez que se ejecuta se pierden los datos cargados (refugios registrados, publicaciones).

### Lo que se agrega al modelo lógico

- Tabla `horarios_refugios` (`id_refugio`, `nro_horario`, `dias`, `hora_desde`, `hora_hasta`): RF18 pide los horarios de atención de cada refugio y `refugios` no tiene dónde guardarlos.
- Tabla `logs_integraciones` (`nro_log`, `fecha`, `proceso`, `organizacion`, `referencia`, `detalle`, `error`): el "guardar log" del diagrama de la integración (RNF06 y RNF08). Las tablas del modelo no se modifican.
- Tres atributos en `atributos_sistema`, que se cargan por veterinaria en `configuracion_veterinatarias`: `ESTADO` (`A` activa, `I` inactiva), `HORARIOS` (texto) y `SERVICIOS` (códigos de `tipos_atencion_sanitaria` separados por coma, por ejemplo `VAC,DES,CAS`).
- Un refugio está habilitado si su responsable tiene `ciudadanos.habilitado = 1`, que es lo mismo que mira el login.

Para la instancia de test de Docker, crear también `mascotas_cba_test` (cambiar el `use` de la primera línea del script).

## Ejecutar desde IntelliJ

Todas las variables de `application.properties` tienen un valor por defecto (`localhost:1433`, base `mascotas_cba`, usuario `sa`, clave `Pyxis.2026`), así que alcanza con darle Run a `MascotasRestApplication`: no hace falta configurar variables de entorno.

## Ejecutar con Docker

```
mvn clean package
docker compose up --build
```

- `app-dev`: http://localhost:8085/api/v1 (base `mascotas_cba`)
- `app-test`: http://localhost:8086/api/v1 (base `mascotas_cba_test`)

Los contenedores se conectan al SQL Server de la PC mediante `host.docker.internal`.

Ojo con la integración: las URL de las veterinarias están en la base (`configuracion_veterinatarias`) con `localhost`, que es lo que sirve desde IntelliJ. Si `mascotas-rest` corre en Docker, `localhost` es el propio contenedor: hay que cambiar esas URL a `host.docker.internal` (es un `update` en la base, no se toca el código).

## Endpoints

Todas las llamadas llevan el header `X-API-Key` (valor en `security.api-key` de `application.properties`, variable `API_KEY`).

Las de `/veterinarias` (incluida la integración) y las consultas de `/refugios` son públicas: sólo piden la API Key (`JwtFilter.shouldNotFilter` las deja pasar sin token).

Las de `/publicaciones` llevan además `Authorization: Bearer <token>`, con el JWT que devuelve el login (`config/JwtFilter`). El token guarda el CUIL y el `idRefugio` del usuario; el filtro los deja en el request dentro de un `UsuarioBE` (atributo `usuario`) y cada método lo recibe con `@RequestAttribute UsuarioBE usuario`, así cada refugio sólo ve y modifica sus publicaciones. Clave y duración en `security.jwt-secret` (`JWT_SECRET`) y `security.jwt-expiration` (`JWT_EXPIRATION`, en milisegundos; por defecto 8 horas). Sin token o con un token vencido responde 401 con el mensaje en el cuerpo.

| Método | URI | Body | Respuesta |
|---|---|---|---|
| POST | `/sesiones` | `{"cuil": "20123456789", "clave": "123456"}` | 200 con el usuario, su refugio y el `token` · 401 con el mensaje si las credenciales no son válidas |
| POST | `/refugios` | ver ejemplo | 200 con el `idRefugio` · 400 con el mensaje si faltan datos o el CUIL ya está registrado |
| GET | `/veterinarias/listado` | | 200 con las veterinarias activas: `idVeterinaria`, `razonSocial`, `domicilio`, `telefono`, `correo`, `horarios` y `servicios` (nombres separados por coma) |
| GET | `/veterinarias/servicios` | | 200 con `[{"codigo": "VAC", "nombre": "Vacunación"}, …]` (combo del filtro) |
| POST | `/veterinarias/integracion` | | 200 con el resultado de cada veterinaria activa (ver [Integración con las veterinarias](#integración-con-las-veterinarias-rf06-y-rf09)) |
| GET | `/refugios/listado` | | 200 con los refugios habilitados: `idRefugio`, `razonSocial`, `domicilio`, `telefono`, `correo`, `horarios` y `cantPublicaciones` (publicaciones activas) |
| GET | `/refugios/{idRefugio}` | | 200 con los datos del refugio · 404 con el mensaje si no existe o no está habilitado |
| GET | `/refugios/{idRefugio}/publicaciones` | | 200 con las publicaciones **activas** del refugio (las pausadas y finalizadas no se ven en el portal) |
| GET | `/publicaciones/especies` | | 200 con `[{"codigo": 1, "nombre": "Perro"}, …]` |
| GET | `/publicaciones/listado` | | 200 con las publicaciones del refugio del token |
| POST | `/publicaciones/publicacion` | ver ejemplo | 200 con el `nroPublicacion` · 400 si faltan datos |
| POST | `/publicaciones/publicacion/estado` | `{"nroPublicacion": 1, "estadoPublicacion": "P"}` | 200 · 400 si el estado no es `A` (Activa), `P` (Pausada) o `F` (Finalizada). Una publicación finalizada no cambia más |

Ejemplo de alta de publicación (`foto` es la imagen en base64 como data URL):

```json
{
  "nombre": "Manchita",
  "codEspecie": 1,
  "sexo": "H",
  "anioNacimiento": 2024,
  "raza": "Mestizo",
  "caracteristicasMascota": "Es muy cariñosa y tranquila.",
  "condicionAdopcion": "Se requiere entrevista previa.",
  "foto": "data:image/jpeg;base64,/9j/4AAQ..."
}
```

Cada alta crea la mascota (`mascotas`, con el refugio y el año de nacimiento; el listado devuelve la edad calculada), su especie y raza (`caracteristicas_mascotas`) y la publicación (`publicaciones_adopcion`, estado `A`) en un solo procedimiento, `dbo.ins_publicacion`.

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

El login valida las credenciales con `dbo.get_usuario_refugio` y firma el JWT en `config/JwtUtil` (librería `jjwt`). El frontend Angular guarda este usuario en el `sessionStorage` del navegador (`SesionService`): la sesión se mantiene aunque se recargue la página y se pierde al cerrar la pestaña, y su interceptor manda el token en cada llamada.

### Consultas públicas del portal (RF17 y RF18)

| Clase | Método | Procedimiento |
|---|---|---|
| `VeterinariasResource` → `VeterinariasRepository` | `getVeterinarias()` | `dbo.get_veterinarias`: veterinarias con `ESTADO = 'A'`; arma `servicios` con los nombres de `tipos_atencion_sanitaria` |
| | `getServicios()` | `dbo.get_tipos_atencion` |
| `RefugiosResource` → `RefugiosRepository` | `getRefugios()` | `dbo.get_refugios` |
| | `getRefugio(RefugioBE refugio)` | `dbo.get_refugios @id_refugio` (el mismo procedimiento, con el filtro opcional) |
| | `getPublicacionesActivas(RefugioBE refugio)` | `dbo.get_publicaciones_activas @id_refugio` |

BE nuevos: `VeterinariaBE`, `TipoAtencionBE` y `RefugioBE`. Las publicaciones del refugio usan el `PublicacionBE` que ya existía. Son sólo lecturas, así que no llevan `@Transactional`.

Ejemplo de `GET /refugios/1`:

```json
{
  "idRefugio": 1,
  "razonSocial": "REFUGIO HUELLITAS",
  "domicilio": "CAMINO A SAN CARLOS KM 5",
  "telefono": "3515554321",
  "correo": "huellitas@refugio.org",
  "horarios": "Lunes a viernes de 09:00 a 17:00 · Sábados de 10:00 a 13:00",
  "cantPublicaciones": 2
}
```

## CORS

`config/CorsConfig.java` es el de la profe (`personas-rest`), con `X-API-Key` agregado a los headers permitidos para que el portal Angular (puertos 4200 y 4201) pueda mandar la API Key. `Authorization`, que usa el JWT, ya venía en el de la profe.

## Integración con las veterinarias (RF06 y RF09)

`POST /veterinarias/integracion` (sólo API Key; se dispara desde Postman) recorre las veterinarias activas y, con cada una:

1. Le pide las **mascotas pendientes** (las que todavía no tienen NRM).
2. Registra cada mascota en el Registro Único con `dbo.ins_mascota_veterinaria`: si ya existe (mismo microchip, o mismo nombre + año de nacimiento + responsable) recupera su NRM; si no, la da de alta con su responsable (si no era ciudadano), especie, raza y pelaje.
3. Le **informa el NRM** a la veterinaria.
4. Le pide las **atenciones** (información sanitaria) de sus mascotas con NRM y registra cada una con `dbo.ins_atencion_veterinaria`.

### Nada fijo en el código

Todo lo que hace falta para hablar con una veterinaria está en `configuracion_veterinatarias` (atributos del modelo lógico):

| Atributo | Ejemplo REST (veterinaria 1) | Ejemplo SOAP (veterinaria 3) |
|---|---|---|
| `TECNOLOGIA` | `REST` | `SOAP` |
| `URL` | `http://localhost:8091/api/v1` | `http://localhost:8095/services` |
| `PARAMETROS` | `{"mascotasPendientes": "/mascotas/pendientes", "nroRegMunicipal": "/mascotas/nro-reg-municipal", "atenciones": "/atenciones/listado"}` | por cada operación: `servicio`, `namespace`, `serviceName`, `portName` y `operacion` (el JSON que usa `SOAPClient.fromConfig`) |
| `API_KEY` | credencial que la veterinaria le dio a la Municipalidad | ídem |

`dbo.get_veterinarias_integracion` devuelve esa configuración en un `ConfiguracionVeterinariaBE`. El código sólo conoce las tres operaciones del contrato (`ClienteVeterinaria`, clase abstracta) y hay una implementación por tecnología:

- `ClienteRest` (`@Component("REST")`): usa `Httpful` y llama **siempre por POST** a `URL + ruta`, con el header `X-API-Key`.
- `ClienteSoap` (`@Component("SOAP")`): usa `SOAPClient.fromConfig(...)` con el JSON de la operación; la API Key viaja en el header SOAP `apiKey`.

Spring junta los dos en un `Map<String, ClienteVeterinaria>` cuyas claves son los nombres de los `@Component`, y `VeterinariasResource` elige el cliente con el valor de `TECNOLOGIA`. Para sumar una veterinaria alcanza con `insert` en `veterinarias` y `configuracion_veterinatarias`; para sumar otra tecnología, una clase nueva que extienda `ClienteVeterinaria`.

`Httpful` y `SOAPClient` (paquete `utils`) son los de la profe (`05-consumo-de-servicios`) sin cambios. Las versiones de Jersey y JAXB las maneja Spring Boot 4; `jaxws-rt` va en 4.0.3 porque la 3.0.0 del ejemplo es de Jakarta EE 9.

### Tolerancia a fallos

- Si una veterinaria no responde (apagada, URL o API Key incorrecta), se guarda el error en `logs_integraciones` y se sigue con la próxima.
- Si falla una mascota o una atención, se guarda el error con la referencia y se sigue con la siguiente.
- Cada mascota y cada atención se registra en su propia transacción (`@Transactional` en `insertarMascota` e `insertarAtencion` del Repository): un error en la mascota 30 no deshace las 29 anteriores.
- Se puede disparar las veces que haga falta. Una mascota que ya está no se duplica (y se le vuelve a informar el NRM si la veterinaria no lo recibió). **La misma atención es la misma mascota + fecha de atención + tipo**: si llega de nuevo y cambió algo (por ejemplo la veterinaria corrigió la vacuna) se actualiza; si no cambió, no se toca.
- Cada atención trae si la mascota **vive**, y se actualiza en `mascotas.vive`.

Respuesta (dos veterinarias apagadas):

```json
[
  {
    "idVeterinaria": 1,
    "razonSocial": "VETERINARIA SAN ROQUE",
    "tecnologia": "REST",
    "estado": "OK",
    "mascotasInformadas": 2,
    "atencionesNuevas": 3,
    "atencionesActualizadas": 0,
    "errores": []
  },
  {
    "idVeterinaria": 2,
    "razonSocial": "CLINICA VETERINARIA DEL PARQUE",
    "tecnologia": "REST",
    "estado": "NO DISPONIBLE",
    "mascotasInformadas": 0,
    "atencionesNuevas": 0,
    "atencionesActualizadas": 0,
    "errores": ["No se pudo comunicar con la veterinaria: Connection refused"]
  }
]
```

`estado`: `OK`, `CON ERRORES` (respondió pero falló alguna mascota o atención) o `NO DISPONIBLE`.

### Lo que queda en la base

| Dato que manda la veterinaria | Dónde queda |
|---|---|
| Mascota | `mascotas` (`año_nacimiento` sale de la fecha de nacimiento, `vive`, `microchip`, `id_responsable`) + `caracteristicas_mascotas` (`ESP`, `RAZ`, `PEL`). Una especie que la Municipalidad no tenía se agrega a `dominio_rasgos_mascotas` |
| Responsable y profesional | `ciudadanos` (si no existían, con clave inicial = su CUIL); el profesional también en `profesionales_veterinarias` |
| Atención | `informacion_sanitaria`: `detalle_atencion` con las vacunas "planchadas" (por ejemplo `Antirrábica (lote AR-2291) · Séxtuple (lote SX-118)`) y `fecha_vencimiento` = próxima vacuna o próxima atención. Los tipos que no son del programa (consulta clínica, cirugía) no se registran, pero sí actualizan si la mascota vive |
| Errores y resumen de cada corrida | `logs_integraciones` |

El contrato de las veterinarias está en `backend/veterinaria-rest/README.md`.
