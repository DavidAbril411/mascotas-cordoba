# veterinaria-rest

Sistema de Gestión Veterinaria con servicios **REST**. Emula a las veterinarias adheridas que se integran por REST: cada instancia es una organización independiente, con su propia base, su puerto y su API Key. La Municipalidad (`mascotas-rest`) consume estos servicios en `POST /veterinarias/integracion`.

| Veterinaria | Puerto | Base | API Key | En la demo |
|---|---|---|---|---|
| 1 - VETERINARIA SAN ROQUE | 8091 | `veterinaria_1` | `VET1kR7pQ2mZ9xL4sT8vN3bH6yW1cF5dG0aJ` | levantada |
| 2 - CLINICA VETERINARIA DEL PARQUE | 8092 | `veterinaria_2` | `VET2mW4nB8vC1xZ6kL9pQ3rT7yH2sD5fG0aJ` | apagada (tolerancia a fallos) |

Las veterinarias 3 y 4 son SOAP: ver `backend/veterinaria-ws`.

## Base de datos

Modelo lógico de la veterinaria (`02-Modelo-logico-veterinaria`), sin tablas nuevas. Por cada veterinaria:

1. Crear la base (`create database veterinaria_1`).
2. Ejecutar `database/script-veterinaria.sql` cambiando el `use` de la primera línea por la base que corresponde.
3. Ejecutar `database/datos-veterinaria-N.sql` (ya trae su `use`).

Datos de prueba:

- Veterinaria 1: Firulais (perro, microchip `900100000000001`) y Michi (gata), de Carolina López (CUIL `27280001112`). Firulais tiene una vacunación con dos vacunas y una desparasitación en la misma visita; Michi, una esterilización.
- Veterinaria 2: Luna, de Sofía Gómez.
- Veterinaria 3 (SOAP): Rocco, Toby (murió, `vive = 0`) y **Firulais con el mismo microchip** que en la veterinaria 1, para mostrar que la Municipalidad le da el mismo NRM.
- Veterinaria 4 (SOAP): Nala, de Sofía Gómez.

## Ejecutar

Desde IntelliJ: Run a `VeterinariaRestApplication`. Los valores por defecto de `application.properties` son los de la veterinaria 1 (puerto 8091, base `veterinaria_1`, clave `Pyxis.2026`). Para levantar la veterinaria 2 desde IntelliJ: duplicar la configuración de Run y poner las variables de entorno `PORT=8092;DB_NAME=veterinaria_2;API_KEY=VET2mW4nB8vC1xZ6kL9pQ3rT7yH2sD5fG0aJ`.

Con Docker:

```
mvn clean package
docker compose up --build veterinaria-1
```

(`docker compose up --build` levanta las dos.)

## Contrato

Lo define la Municipalidad y es el mismo para todas las veterinarias, REST o SOAP. Es **plano**: la veterinaria manda los datos como los tiene que recibir la Municipalidad, no como están en su base. Todas las operaciones van **por POST**, con el header `X-API-Key`. Sin API Key o con una incorrecta: 401.

### `POST /mascotas/pendientes`

Mascotas que todavía no tienen número de registro municipal (NRM). Body: `{}`.

```json
[
  {
    "idInterno": "1",
    "nombre": "Firulais",
    "especie": "Perro",
    "raza": "Mestizo",
    "sexo": "M",
    "fechaNacimiento": "2021-03-10",
    "pelaje": "Marrón, Corto",
    "microchip": "900100000000001",
    "vive": true,
    "cuilResponsable": "27280001112",
    "apellidoResponsable": "LOPEZ",
    "nombreResponsable": "CAROLINA",
    "correoResponsable": "carolina.lopez@mail.com",
    "telefonoResponsable": "3515550101",
    "domicilioResponsable": "AV. VELEZ SARSFIELD 1200"
  }
]
```

| Campo | Obligatorio | Descripción |
|---|---|---|
| `idInterno` | sí | Identificador de la mascota en la veterinaria |
| `nombre`, `especie`, `sexo` | sí | `sexo`: `M` macho, `H` hembra. `especie` en texto (`Perro`, `Gato`, …) |
| `raza`, `fechaNacimiento`, `pelaje`, `microchip` | no | Fecha en formato `aaaa-mm-dd` |
| `vive` | sí | `true` / `false` |
| `cuilResponsable`, `apellidoResponsable`, `nombreResponsable` | sí | Responsable principal de la mascota |
| `correoResponsable`, `telefonoResponsable`, `domicilioResponsable` | no | |

### `POST /mascotas/nro-reg-municipal`

La Municipalidad informa el NRM que le asignó a una mascota.

```json
{ "idInterno": "1", "nroRegMunicipal": 1 }
```

200 vacío · 400 si faltan datos · 404 `La mascota no existe.`

### `POST /atenciones/listado`

Atenciones de las mascotas que ya tienen NRM. Body: `{}`. Una fila por atención y tipo de atención, con todo "planchado": si en la misma visita le pusieron dos vacunas, van juntas en `detalle`.

```json
[
  {
    "nroRegMunicipal": 1,
    "vive": true,
    "fechaAtencion": "2026-10-15",
    "codTipoAtencion": "VAC",
    "detalle": "Antirrábica (lote AR-2291) · Séxtuple (lote SX-118). Control anual sin observaciones",
    "fechaProxima": "2027-10-15",
    "cuilProfesional": "27250003334",
    "apellidoProfesional": "RIOS",
    "nombreProfesional": "LAURA",
    "correoProfesional": "laura.rios@vetsanroque.com.ar",
    "matriculaProfesional": "MP-1234"
  }
]
```

| Campo | Obligatorio | Descripción |
|---|---|---|
| `nroRegMunicipal` | sí | NRM que informó la Municipalidad |
| `vive` | sí | Si la mascota vive hoy |
| `fechaAtencion`, `codTipoAtencion` | sí | Tipos del programa: `VAC` Vacunación, `DES` Desparasitación, `EST` Esterilización, `CAS` Castración. Los demás tipos se pueden mandar: la Municipalidad no los registra, pero toma el dato de si la mascota vive |
| `detalle` | no | Vacunas (con lote), intervención, indicaciones y observaciones |
| `fechaProxima` | no | Próxima vacuna (en una vacunación) o próxima atención |
| `cuilProfesional`, `apellidoProfesional`, `nombreProfesional` | sí | Quien atendió |
| `correoProfesional`, `matriculaProfesional` | no | |

La Municipalidad reconoce **la misma atención por mascota + fecha de atención + tipo**, así que la veterinaria puede mandar las mismas atenciones todas las veces: no se duplican, y si alguna cambió se actualiza.

## Probar a mano (para la demo)

Nueva atención para Firulais en la veterinaria 1 (dos vacunas en la misma visita):

```sql
use veterinaria_1
insert into dbo.atencion_sanitaria(id_mascota, nro_atencion, id_profesional, fecha_atencion, fecha_prox_atencion, observ_atencion)
values(1, 2, 2, '2026-12-02', null, null)
insert into dbo.detalle_atencion_sanitaria(id_mascota, nro_atencion, cod_tipo_atencion, nro_detalle, cod_vacuna, lote_vacuna, fecha_prox_vacunacion, indicaciones, intervencion)
values(1, 2, 'VAC', 1, 'ANT', 'AR-4001', '2027-12-02', null, null),
      (1, 2, 'VAC', 2, 'SEX', 'SX-205', '2027-12-02', null, null)
```

Después, en Postman, "Disparar integración con las veterinarias": la veterinaria 1 responde `atencionesNuevas: 1` y la atención queda en `informacion_sanitaria` de `mascotas_cba`. Si se cambia la vacuna (`update dbo.detalle_atencion_sanitaria set lote_vacuna = 'AR-4002' where id_mascota = 1 and nro_atencion = 2 and nro_detalle = 1`) y se vuelve a disparar, responde `atencionesActualizadas: 1`.
