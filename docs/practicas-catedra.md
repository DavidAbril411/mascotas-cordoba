# Prácticas de la cátedra (DAS – Lic. Mariela Pastarini) para el backend Spring Boot

Documento de referencia para desarrollar el backend de **Mascotas Córdoba** con *exactamente* las mismas prácticas que el material de clase.
Fuentes analizadas (todas en `/mnt/project-files/`): clases 02, 06–12 (PDF), apuntes M2-1, M2-3, guías M2-2-1 a M2-2-5 (docx), ejemplos `factorial-ws`, `factorial-rest`, `03-docker`, `04-seguridad`, `05-consumo-de-servicios`, `06-poa` (evolución acumulativa de `localidades-ws` y `localidades-rest`), scripts `02-bases-datos/script-localidades.sql` y `Ejercicio-1/script-personas.sql`, especificación de requerimientos, modelos lógicos, `MascotasServicios.pdf` (diagrama de flujo) y `Plantilla SOA - Registro Unico de Mascotas.docx`.

> **Ejemplo canónico:** `06-poa/localidades-rest` y `06-poa/localidades-ws`. Es la versión final que incluye todo lo visto (BD + Docker + API Key + aspectos). Ante la duda, copiar su estructura y estilo.

---

## 1. Resumen rápido (lo que hay que respetar sí o sí)

| Tema | Lo que usa la profesora | Lo que evita |
|---|---|---|
| Acceso a datos | **JDBC + `JdbcTemplate` + procedimientos almacenados** (`exec dbo.xxx ?, ?`), `BeanPropertyRowMapper`, `SimpleJdbcCall` para parámetros `OUTPUT` | ORM / JPA / Hibernate / entidades `@Entity` / Spring Data repositories con métodos derivados / SQL armado en Java |
| Modelo de datos Java | Clases **`XxxBE`** (Business Entities) en paquete **`BE`**, POJOs con getters/setters explícitos | Lombok, `record`, sufijos DTO/Entity/Model, asumir que un BE = una tabla |
| SOAP | **JAX-WS + Apache CXF**, *code-first* (interfaz `@WebService` + impl `@Service`), endpoints publicados en una clase `@Configuration` | Escribir el WSDL a mano, contract-first, Spring-WS |
| REST | `@RestController` + `@RequestMapping` en clases **`XxxResource`** del paquete **`resources`**, `ResponseEntity.ok(...)`, `@PathVariable`, `@RequestBody` | Verbos en URIs (`/obtenerUsuario`), query strings, lógica en el punto de entrada |
| Seguridad | **API Key** propia: `OncePerRequestFilter` (REST, header `X-API-Key`) / `AbstractSoapInterceptor` (SOAP, header SOAP `apiKey`) | Spring Security, JWT, OAuth2, WS-Security (los explica pero los descarta por complejidad) |
| Transversales | **AOP con AspectJ**: `AuditAspect` (log antes/después) y `ExceptionHandlingAspect` (log + relanzar) | Logs y try/catch dispersos en cada método; `@ControllerAdvice` no aparece |
| Configuración | `application.properties` con **`${VARIABLE:valorPorDefecto}`** | Credenciales/URLs hardcodeadas en el código, `application.yml`, perfiles |
| Empaquetado | **WAR** + `ServletInitializer`, Docker con `tomcat:10.1-jdk17-temurin` | JAR ejecutable con Tomcat embebido en producción |
| Consumo de servicios | Clases utilitarias **`Httpful`** (JAX-RS/Jersey + Gson) y **`SOAPClient`** (Dispatch + JAXB) | Clases generadas desde WSDL (wsimport), RestTemplate/WebClient/Feign |
| Base de datos | **SQL Server**, SPs `get_` / `ins_` / `act_` / `del_`, columnas devueltas con alias **camelCase** | Lógica de acceso a datos en Java en lugar de SP |

---

## 2. Proyecto y dependencias

Parámetros con los que la guía crea **todos** los proyectos (Spring Initializr):

- Language: **Java** · Type: **Maven** · Group: **`ar.edu.ubp.das`** · Packaging: **War**
- Package: `ar.edu.ubp.das.<nombreproyectosinguiones>` (ej. `localidades-rest` → `ar.edu.ubp.das.localidadesrest`)
- Nombres de proyecto en kebab-case con sufijo de tecnología: `xxx-rest`, `xxx-ws`.
- Dependencias: **Spring Boot DevTools**, **Spring Web**, **Spring Data JDBC**, **MS SQL Server Driver**; para SOAP además **`cxf-spring-boot-starter-jaxws`** con propiedad `<cxf.version>4.2.3</cxf.version>`; para AOP **`spring-boot-starter-aspectj`**.
- Versiones del ejemplo final: `spring-boot-starter-parent` **4.1.1**, `<java.version>17</java.version>`, `spring-boot-starter-tomcat` con `scope provided`.
- pom del ejemplo REST: `spring-boot-starter-web`; pom del ejemplo WS: `spring-boot-starter-webmvc` (+ `-webmvc-test`). Tests: `spring-boot-starter-data-jdbc-test`.

Clases que siempre existen (sin modificar lo que genera Initializr):

```java
@SpringBootApplication
public class LocalidadesRestApplication {
    public static void main(String[] args) {
        SpringApplication.run(LocalidadesRestApplication.class, args);
    }
}

public class ServletInitializer extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(LocalidadesRestApplication.class);
    }
}
```

Test: solo el `contextLoads()` generado. No hay tests unitarios propios en ningún ejemplo.

---

## 3. Estructura de paquetes (exacta)

**REST** (`localidades-rest`):
```
ar.edu.ubp.das.localidadesrest
├── BE/               PaisBE, ProvinciaBE, LocalidadBE, LocalidadDataBE, LocalidadesPorProvinciaBE
├── repositories/     PaisesRepository, ProvinciasRepository, LocalidadesRepository
├── resources/        PaisesResource, ProvinciasResource, LocalidadesResource
├── config/           ApiKeyFilter
├── aspects/          AuditAspect, ExceptionHandlingAspect
├── LocalidadesRestApplication
└── ServletInitializer
```

**SOAP** (`localidades-ws`):
```
ar.edu.ubp.das.localidadesws
├── BE/
├── repositories/
├── services/         IPaisesService (interfaz @WebService) + PaisesService (@Service), ...
├── config/           ServicesConfig (publica endpoints), ApiKeyInterceptor
├── aspects/          AuditAspect, ExceptionHandlingAspect
├── LocalidadesWsApplication
└── ServletInitializer
```

Convenciones de nombres:
- Paquete **`BE` en mayúsculas**; clases `<Entidad>BE` en singular (`PaisBE`), `<Entidad>DataBE` para datos de entrada de un alta (`LocalidadDataBE`), `<Agrupado>Por<Criterio>BE` para respuestas armadas (`LocalidadesPorProvinciaBE`).
- Repositorios, recursos y servicios en **plural**: `PaisesRepository`, `PaisesResource`, `IPaisesService` / `PaisesService`.
- Interfaces SOAP con prefijo **`I`**. En el ejemplo de la guía factorial la impl se llama `FactorialServiceImpl`; en el ejemplo final (localidades) la impl es `PaisesService` (sin `Impl`). **Usar el estilo final: `IXxxService` + `XxxService`.**
- Métodos en español con prefijo de acción: `getPaises`, `getLocalidades`, `getLocalidadesPorProvincias`, `insertarLocalidad`.
- Variables y propiedades en español, abreviadas como en la BD: `codPais`, `nomPais`, `nroLocalidad`.

---

## 4. Capa de datos: Repository + JdbcTemplate + Stored Procedures

Postura explícita de la clase 08: *"Existen ORM… Nosotros nos concentraremos en esta segunda estrategia"* (gestionar explícitamente las operaciones con consultas y procedimientos almacenados). El ORM *"puede ocultar detalles de las operaciones realizadas sobre la base"*.

```java
@Repository
public class LocalidadesRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<LocalidadBE> getLocalidades(String codPais, String codProvincia) {
        return jdbcTemplate.query(
            "exec dbo.get_localidades_provincias ?, ?",
            new BeanPropertyRowMapper<>(LocalidadBE.class),
            codPais,
            codProvincia
        );
    }

    public int insertarLocalidad(LocalidadDataBE localidad) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
            .withProcedureName("ins_localidad")
            .declareParameters(
                new SqlParameter("nom_localidad", Types.VARCHAR),
                new SqlParameter("cod_pais", Types.VARCHAR),
                new SqlParameter("cod_provincia", Types.VARCHAR),
                new SqlOutParameter("nro_localidad", Types.INTEGER)
            );

        Map<String, Object> params = new HashMap<>();

        params.put("nom_localidad", localidad.getNomLocalidad());
        params.put("cod_pais", localidad.getCodPais());
        params.put("cod_provincia", localidad.getCodProvincia());

        Map<String, Object> result = jdbcCall.execute(params);

        return (int) result.get("nro_localidad");
    }
}
```

Reglas:
- **Consultas**: `jdbcTemplate.query("exec dbo.<sp> ?, ?", new BeanPropertyRowMapper<>(XxxBE.class), args...)`. Siempre con `dbo.` y placeholders `?`.
- **SP con parámetro OUTPUT**: `SimpleJdbcCall` + `declareParameters` (`SqlParameter` / `SqlOutParameter` con `java.sql.Types`) + `HashMap` con los nombres de parámetro del SP **en snake_case sin `@`**.
- `BeanPropertyRowMapper` *"evitando implementar manualmente un RowMapper"*. Para que funcione, **el SP devuelve las columnas con alias camelCase** igual a las propiedades del BE.
- *"No es necesario crear manualmente un JdbcTemplate"*: lo inyecta Spring a partir del DataSource de `application.properties`.
- El Repository **solo** accede a datos (sin lógica de negocio, sin `@Transactional`).
- Inyección por campo con `@Autowired private JdbcTemplate jdbcTemplate;`.

---

## 5. Business Entities (BE)

```java
public class PaisBE {

    private String codPais;
    private String nomPais;

    public String getCodPais() {
        return codPais;
    }

    public void setCodPais(String codPais) {
        this.codPais = codPais;
    }
    ...
}
```

- POJO con atributos `private`, getters y setters escritos a mano. Sin constructores, sin `toString` en el servidor, sin anotaciones de validación.
- *"No debemos asumir que un BE representa una tabla. Un BE puede representar el resultado de una consulta, los datos necesarios para una operación o una estructura preparada específicamente para una respuesta."*
- Orden de propiedades en respuestas agrupadas:
  - REST (Jackson): `@JsonPropertyOrder({"codProvincia","nomProvincia","localidades"})`
  - SOAP (JAXB): `@XmlType(propOrder = {"codProvincia","nomProvincia","localidades"})`
- Los BE del proyecto REST y WS son copias (la guía dice *"copiar los paquetes be y repositories… y revisar los nombres de los paquetes"*).

---

## 6. Servicios SOAP (JAX-WS + CXF, code-first)

Interfaz = contrato:

```java
@WebService
public interface ILocalidadesService {

    @WebMethod
    @WebResult(name = "localidad")
    List<LocalidadBE> getLocalidades(
        @WebParam(name = "codPais") String codPais,
        @WebParam(name = "codProvincia") String codProvincia
    );

    @WebMethod
    @WebResult(name = "nroLocalidad")
    int insertarLocalidad(
        @WebParam(name = "localidad") LocalidadDataBE localidad
    );
}
```

Implementación = lógica de negocio, delega al repositorio:

```java
@Service
public class LocalidadesService implements ILocalidadesService {

    @Autowired
    private LocalidadesRepository repository;

    @Override
    @Transactional
    public int insertarLocalidad(LocalidadDataBE localidad) {
        return repository.insertarLocalidad(localidad);
    }
}
```

Publicación de endpoints (con seguridad incluida):

```java
@Configuration
public class ServicesConfig {

    @Value("${security.api-key}")
    private String apiKey;

    @Bean
    public EndpointImpl localidadesEndpoint(Bus bus, ILocalidadesService service) {
        return buildEndpoint(bus, service, "/localidades");
    }

    private EndpointImpl buildEndpoint(Bus bus, Object service, String path) {
        EndpointImpl endpoint =
                new EndpointImpl(bus, service);
        endpoint.getInInterceptors()
                .add(new ApiKeyInterceptor(apiKey));
        endpoint.publish(path);
        return endpoint;
    }
}
```

Reglas:
- **Un servicio por entidad/área** (`/paises`, `/provincias`, `/localidades`), publicados bajo `/services/...` (ruta por defecto de CXF): `http://localhost:8080/services/localidades?wsdl`.
- Siempre nombrar parámetros y resultados con `@WebParam(name=...)` y `@WebResult(name=...)` (el resultado va en singular aunque sea lista: `"pais"`, `"localidad"`).
- El namespace queda el que genera CXF a partir del paquete (`http://services.localidadesws.das.ubp.edu.ar/`); no se personaliza.
- **`@Transactional` en el Service**, sólo en operaciones de escritura: *"se ubica en el Service porque representa la operación de negocio. El Repository se concentra en el acceso a datos."*
- Transformaciones para el consumidor (ej. agrupar una lista plana por provincia con `stream().collect(Collectors.groupingBy(..., LinkedHashMap::new, Collectors.toList()))`) se hacen en el **Service**, no en el Repository ni en la BD.
- El endpoint *"delega el procesamiento en la lógica de negocio, evitando concentrar toda la funcionalidad en el punto de entrada"*.

---

## 7. Servicios REST

```java
@RestController
@RequestMapping("/localidades")
public class LocalidadesResource {

    @Autowired
    private LocalidadesRepository repository;

    @GetMapping("/{codPais}/{codProvincia}")
    public ResponseEntity<List<LocalidadBE>> getLocalidades(
            @PathVariable String codPais,
            @PathVariable String codProvincia) {
        return ResponseEntity.ok(
                repository.getLocalidades(codPais, codProvincia)
        );
    }

    @PostMapping("/localidad")
    public ResponseEntity<Integer> insertarLocalidad(@RequestBody LocalidadDataBE localidad) {
        return ResponseEntity.ok(repository.insertarLocalidad(localidad));
    }
}
```

Reglas:
- Clase `XxxResource` (no `Controller`), en paquete `resources`, con `@RestController` + `@RequestMapping("/<recurso-plural>")`.
- Todos los métodos devuelven **`ResponseEntity<T>`** usando **`ResponseEntity.ok(...)`**.
- Parámetros por **`@PathVariable`** (jerarquía `/localidades/{codPais}/{codProvincia}`); el body con **`@RequestBody`** de un `XxxDataBE`. No se usa `@RequestParam` (*"Evitar el uso de query"*, apunte M2-1).
- **Context path** `server.servlet.context-path=/api/v1` y puerto `server.port=8085` → `http://localhost:8085/api/v1/paises`.
- URIs con sustantivos en minúscula, jerárquicas; nunca `/obtenerUsuario`, `/getEntity?id=`.
- En el ejemplo REST el Resource llama **directo al Repository** (no hay capa Service) y la transformación agrupada está en el propio Resource. Ver §13 sobre cuándo agregar Service.

---

## 8. Seguridad: API Key

Elegida explícitamente (clase 10): *"Es sencilla… simple de implementar… permite identificar distintos consumidores… puede utilizarse tanto en REST como en SOAP"*. Objetivo de la guía M2-2-4: *"manteniendo la seguridad separada de la lógica funcional"*. Misma estrategia, adaptada: **REST → Filter, SOAP → Interceptor**.

`application.properties`:
```properties
security.api-key=${API_KEY:UBPU5iOKq7Cne3p3mzdrwEpBj7XyQsaiXq6}
```

REST (`config/ApiKeyFilter`):
```java
@Component
public class ApiKeyFilter extends OncePerRequestFilter {

    @Value("${security.api-key}")
    private String configuredApiKey;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {
        String apiKey = request.getHeader("X-API-Key");
        if (!configuredApiKey.equals(apiKey)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        filterChain.doFilter(request, response);
    }
}
```

SOAP (`config/ApiKeyInterceptor`, sin `@Component`, se instancia en `ServicesConfig`): extiende `AbstractSoapInterceptor`, fase `Phase.PRE_PROTOCOL`, lee el header SOAP cuyo *local part* es `apiKey` y lanza `new SoapFault("Invalid API Key", SoapFault.FAULT_CODE_CLIENT)` si no coincide.

Notas:
- Header REST: **`X-API-Key`** → 401 si falta o no coincide.
- Header SOAP: `<apiKey xmlns="...">valor</apiKey>` dentro de `<soapenv:Header>`.
- La clave nunca va en el código: siempre `@Value("${security.api-key}")`.
- Para Mascotas Córdoba (RNF04, "credenciales propias por organización") el modelo lógico de la municipalidad ya prevé un atributo **"Api Key"** en `configuracion_veterinarias`, y la Plantilla SOA lo llama *"Token de la Municipalidad"* (cadena, 100). Es la misma práctica.

---

## 9. Programación orientada a aspectos (AOP)

Dos aspectos en `aspects/`, con `@Aspect @Component` y logger SLF4J estático:

```java
@Aspect
@Component
public class AuditAspect {

    private static final Logger logger = LoggerFactory.getLogger(AuditAspect.class);

    @Pointcut("execution(* ar.edu.ubp.das.localidadesws.services..*(..))")
    public void serviceMethods() {}

    @Before("serviceMethods()")
    public void logBefore(JoinPoint joinPoint) {
        logger.info("Entrando en {}.{} con argumentos: {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                joinPoint.getArgs());
    }

    @AfterReturning(pointcut = "serviceMethods()", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        logger.info("Método: {}.{} ejecutado con éxito. Resultado: {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                result);
    }
}
```

```java
@Aspect
@Component
public class ExceptionHandlingAspect {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlingAspect.class);

    @Around("execution(* ar.edu.ubp.das.localidadesws.services..*(..))")
    public Object handleExceptions(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        }
        catch (Exception e) {
            logger.error(
                    "Error en el método: {}.{}. Detalle: {}",
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName(),
                    e.getMessage(),
                    e
            );
            throw e;
        }
    }
}
```

- Pointcut sobre el paquete **`services..*`** en SOAP y **`resources..*`** en REST.
- El aspecto de excepciones **loguea y relanza** (no transforma la respuesta).
- Es la forma de cubrir **RNF08 Auditoría** sin ensuciar los servicios: *"nuestros módulos de servicios ahora están más limpios"*.
- ⚠️ El `AuditAspect` del ejemplo **REST** tiene un bug (3 placeholders `{}` y sólo 2 argumentos: falta `getDeclaringTypeName()`). Usar la versión del ejemplo **WS** (arriba), que es la correcta.

---

## 10. Configuración, Docker y despliegue

`application.properties` (sin `.yml`, sin perfiles):
```properties
spring.application.name=localidades-rest
server.servlet.context-path=/api/v1
server.port=8085
spring.datasource.url=jdbc:sqlserver://${DB_HOST:localhost}:${DB_PORT:1433};databaseName=${DB_NAME:das};encrypt=false
spring.datasource.username=${DB_USER:sa}
spring.datasource.password=${DB_PASSWORD:pyxis}
spring.datasource.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver
security.api-key=${API_KEY:...}
```
Principio (clase 09): *"Un único archivo WAR sin direcciones o credenciales hardcodeadas"*, *"El código de la aplicación no debería depender del entorno donde se ejecuta."*

`Dockerfile`:
```dockerfile
FROM tomcat:10.1-jdk17-temurin
LABEL authors="Mariela Pastarini"

# Limpiar aplicaciones predeterminadas de Tomcat
RUN rm -rf /usr/local/tomcat/webapps/*

# Copiar el WAR asignándole el context path /api/v1 mediante #
COPY target/*.war "/usr/local/tomcat/webapps/api#v1.war"

# El puerto interno de Tomcat dentro del contenedor sigue siendo 8080
EXPOSE 8080

CMD ["catalina.sh", "run"]
```
(El WS copia a `webapps/services.war`.)

`docker-compose.yaml`: `version: '3.8'`, dos instancias de la **misma imagen** (`app-dev` con `build: .` en `8085:8080`, `app-test` en `8086:8080`), variables `DB_HOST=host.docker.internal`, `DB_PORT`, `DB_NAME` (das / das_test), `DB_USER`, `DB_PASSWORD`. Build: `mvn clean package`.

---

## 11. Consumo de servicios (clientes)

Clase 11 / guía M2-2-5. Se encapsula toda la comunicación en dos utilitarios dentro de `ar.edu.ubp.das.utils` y el código que consume sólo indica *qué* invocar:

- **`Httpful`** (REST): builder fluido sobre **JAX-RS Client (Jersey 3.1.10)** + **Gson**.
  ```java
  List<ProvinciaBean> provincias = new Httpful(baseUrl)
          .path("/provincias/AR")
          .apiKey("X-Api-Key", apiKey)
          .get()
          .execute(new TypeToken<List<ProvinciaBean>>() {}.getType());
  ```
  Lanza `RuntimeException("Error HTTP " + status + ...)` en respuestas no 2xx; usa `//region` para organizar.
- **`SOAPClient`** (SOAP): configurado desde un JSON (`wsdlUrl`, `namespace`, `serviceName`, `portName`, `apiKey`), usa `Service` + `Dispatch<SOAPMessage>` + JAXB, agrega `apiKey` al SOAP Header. Métodos `callServiceForList` / `callServiceForObject`.
- **"Clases generadas: No"**: no se usa wsimport ni se generan stubs desde el WSDL.
- Beans del cliente: paquete `beans`, sufijo **`Bean`** (`PaisBean`), con `toString()`; para SOAP, anotados `@XmlRootElement(name = "pais")` + `@XmlAccessorType(XmlAccessType.FIELD)`.

**Aplicación a Mascotas Córdoba:** el proceso batch de la Municipalidad (diagrama `MascotasServicios.pdf`) consume los servicios de cada veterinaria según su configuración (URL, tecnología REST/SOAP, parámetros, Api Key: tabla `configuracion_veterinarias` del modelo lógico). Corresponde reutilizar `Httpful` y `SOAPClient` tal cual.

---

## 12. Base de datos (SQL Server) – estilo de scripts

Del `script-localidades.sql` y `script-personas.sql`:

- `use das` / `go`; al inicio `drop table if exists dbo.xxx` (en orden inverso de dependencias).
- Encabezado comentado por objeto:
  ```sql
  /* -------------------------------------
     Procedimiento: get_paises
     ------------------------------------- */
  ```
- Tablas: `dbo.` siempre, nombres **snake_case plural** (`localidades`), columnas con prefijos `cod_`, `nom_`, `nro_`, `id_`, `fecha_`, `desc_`; tipos `varchar(n)`, `integer`, `smallint`, `date`; columnas alineadas con tabs; autonuméricos con `identity`.
- Constraints con nombre: `PK__tabla__END`, `FK__tabla__tablaref__END`, `UK__tabla__xxx__END`; `on delete cascade on update cascade` en tablas hijas.
- Procedimientos: **`create or alter procedure dbo.<verbo>_<objeto>`** con verbos `get_`, `ins_`, `act_`, `del_`; parámetros entre paréntesis con `@snake_case`, opcionales `= null`, salidas `output`.
- `select alias = columna` con **alias camelCase** para mapear a BE: `select codPais = cod_pais, nomPais = nom_pais from dbo.paises (nolock) order by nomPais`.
- Lecturas con `(nolock)`; filtros opcionales con `(col = @param or @param is null)`.
- Escrituras: `set nocount on` … `set nocount off`; normalizar vacíos a null (`if trim(@x) = '' set @x = null` o `ltrim(rtrim())`); `upper(trim(...))` en textos; id generado con `set @nro = @@identity` como parámetro OUTPUT.
- Listas recibidas como **JSON string** (`'[1,2]'`) y parseadas con `openjson(...) with(...)` en variables tabla; actualización de hijos con `insert ... where not exists` + `delete ... where not exists`.
- IDs opacos hacia afuera con `encryptbypassphrase` / `decryptbypassphrase` (ejercicio personas).
- Debajo de cada SP, una línea comentada de prueba: `-- execute dbo.get_provincias @cod_pais='AR'`.

---

## 13. Diferencias entre ejemplos y decisiones a tomar

1. **¿Service en REST?** El ejemplo REST no tiene capa Service (Resource → Repository) y por eso no tiene `@Transactional`. La teoría (clase 06 y guía M2-2-3) dice que el endpoint debe delegar en la lógica de negocio y que `@Transactional` va en el Service. **Recomendación:** si una operación REST tiene lógica (deduplicar mascotas, generar NRM, varias escrituras), agregar `services/XxxService` con `@Service` y `@Transactional` como en el WS; si es una consulta simple, Resource → Repository como el ejemplo.
2. **Inyección:** los ejemplos finales usan **campo `@Autowired`**. `factorial-ws` usa constructor y `ServicesConfig` usa parámetros de método `@Bean`. Mantener campo `@Autowired` en Repository/Resource/Service.
3. **Códigos HTTP:** la teoría menciona `201 Created`, pero el código siempre responde `ResponseEntity.ok`. La Plantilla SOA del proyecto pide códigos 200/400/401/404/500 y "mensaje de respuesta": eso exige devolver `ResponseEntity.status(...)` en los casos de error definidos. Mantener `ResponseEntity`, sin inventar `@ControllerAdvice` (no aparece en el material).
4. **POST de alta:** el ejemplo usa `POST /localidades/localidad` (sub-ruta en singular para la creación).
5. **Namespace del header SOAP `apiKey`:** la guía y el cliente usan namespaces distintos; el interceptor sólo compara el *local part*, así que cualquiera sirve.
6. **Java:** servicios en Java 17; los clientes de consumo usan Java 26 (`static void main()` sin `String[]`). Para el backend, Java 17.

---

## 14. Lo que la profesora evita (explícito o por omisión)

Explícito en el material:
- ORM (*"puede ocultar detalles"*) → JDBC + SP.
- Escribir WSDL a mano → code-first con CXF.
- Credenciales/URLs en el código → variables de entorno con default.
- Verbos en URIs y query strings.
- Concentrar la lógica en el punto de entrada.
- Mezclar seguridad con lógica funcional → Filter/Interceptor.
- Código transversal disperso → aspectos.
- RowMapper manual, JdbcTemplate creado a mano.
- Generar clases cliente a partir del WSDL.
- Asumir BE = tabla.

Por omisión (no aparece en ningún ejemplo; no introducir sin motivo):
- Lombok, `record`, MapStruct, `Optional`.
- JPA/Hibernate, Spring Data repositories derivados, Flyway/Liquibase.
- Spring Security, JWT, OAuth2.
- `@ControllerAdvice` / `@ExceptionHandler`, Bean Validation (`@Valid`, `@NotNull`).
- Swagger/OpenAPI, `application.yml`, perfiles Spring.
- RestTemplate, WebClient, Feign.
- Tests más allá de `contextLoads`.
- Comentarios Javadoc en el código (el código casi no tiene comentarios; sí `//region` en utilitarios y comentarios en Dockerfile/SQL).

---

## 15. Contexto del proyecto Mascotas Córdoba (para orientar el diseño)

- **RNF01–02, 11–12:** SOA, servicios **REST y SOAP**, **SQL Server**, **Spring Boot** (backend) y Angular (portal).
- **RNF04:** cada organización con credenciales propias → API Key.
- **RNF06:** tolerar caída de una veterinaria sin cortar el resto (el diagrama registra log y sigue con la siguiente).
- **RNF07:** integridad del Registro Único (→ `@Transactional` en el Service + lógica en SP).
- **RNF08:** auditoría (→ `AuditAspect`).
- **Diagrama `MascotasServicios.pdf`** (proceso batch "Registro de mascotas pendientes"): obtener veterinarias Activas → por cada una `ObtenerMascotasPendientes()` → por cada mascota buscar por microchip (si tiene) o por nombre + edad aproximada + CUIL del dueño → si existe obtener NRM, si no generar NRM y registrar → registrar NRM en "pendientes de informar" → ante errores guardar log (veterinaria, [mascota], fecha, detalle) y acumular → al final, si hubo errores, enviar mail con los logs.
- **Plantilla SOA:** Servicio 1 ubicado en la **Veterinaria**, operaciones `ObtenerMascotasPendientes()` (token, nro. de página opcional, fecha de corte opcional → código, mensaje, lista de mascotas) y `ActualizarNRM()` (token, ID interno, NRM → código 200/400/401/404/500 y mensaje). El proceso 2 "Informar NRM a las veterinarias" usa `ActualizarNRM()`.
- Modelos lógicos: `02-Modelo-logico-veterinaria.pdf` (sistema de la veterinaria) y `03-Modelo-logico-mascotas-cba.pdf` (municipalidad: `veterinarias`, `configuracion_veterinarias`, `atributos_sistema`, `mascotas` con `nro_reg_municipal`, `informacion_sanitaria`, `publicaciones_adopcion`, etc.).
