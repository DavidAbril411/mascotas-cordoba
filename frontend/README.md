# Frontend

This project was generated using [Angular CLI](https://github.com/angular/angular-cli) version 22.1.8.

## Development server

To start a local development server, run:

```bash
ng serve
```

Once the server is running, open your browser and navigate to `http://localhost:4200/`. The application will automatically reload whenever you modify any of the source files.

## Code scaffolding

Angular CLI includes powerful code scaffolding tools. To generate a new component, run:

```bash
ng generate component component-name
```

For a complete list of available schematics (such as `components`, `directives`, or `pipes`), run:

```bash
ng generate --help
```

## Building

To build the project run:

```bash
ng build
```

This will compile your project and store the build artifacts in the `dist/` directory. By default, the production build optimizes your application for performance and speed.

## Running unit tests

To execute unit tests with the [Vitest](https://vitest.dev/) test runner, use the following command:

```bash
ng test
```

## Running end-to-end tests

For end-to-end (e2e) testing, run:

```bash
ng e2e
```

Angular CLI does not come with an end-to-end testing framework by default. You can choose one that suits your needs.

## Additional Resources

For more information on using the Angular CLI, including detailed command references, visit the [Angular CLI Overview and Command Reference](https://angular.dev/tools/cli) page.

## Portal Mascotas Córdoba

Estructura de `src/app`: `core/` (infraestructura común), `shared/` (pipes y directivas) y una carpeta por funcionalidad con su archivo de rutas: `portal/` (público), `acceso/` (login y registro de refugio) y `publicaciones/` (perfil Refugio).

### Rutas

| Ruta | Página | Resolvers | Acceso |
|---|---|---|---|
| `/inicio` | `portal/pages/inicio-page` | | Público |
| `/veterinarias` | `portal/pages/veterinarias-page` | `listado`: `veterinariasResolver` · `servicios`: `serviciosResolver` | Público |
| `/refugios` | `portal/pages/refugios-page` | `listado`: `refugiosResolver` | Público |
| `/refugios/:id` | `portal/pages/refugio-page` | `info`: `datosRefugioResolver` · `publicaciones`: `adopcionesResolver` | Público |
| `/acceso` | `acceso/pages/login-page` | | Público |
| `/acceso/registro` | `acceso/pages/registro-page` | | Público |
| `/publicaciones` | `publicaciones/pages/list-page` | `listado`: `publicacionesResolver` | `sesionGuard` |

Cualquier otra dirección va a `/inicio`.

### Componentes del portal público (RF16, RF17 y RF18)

| Pieza | Qué hace |
|---|---|
| `core/layouts/navbar` (`<app-navbar>`) | Barra verde común a todas las páginas. Sin sesión muestra Inicio, Veterinarias, Refugios e Ingresar; con sesión de refugio suma Publicaciones y el menú del usuario con Cerrar sesión. Lee el usuario de `SesionService` |
| `InicioPage` | Portada del programa con accesos a Veterinarias, Refugios y a la gestión de publicaciones (si no hay sesión, `sesionGuard` lleva al login). No consume servicios |
| `VeterinariasPage` | Tarjetas de las veterinarias activas con domicilio, teléfono, correo, horarios y servicios. Buscador por nombre o domicilio y combo de servicio, los dos con `FiltrarPorPipe` |
| `RefugiosPage` | Tarjetas de los refugios habilitados con sus datos de contacto, horarios, cantidad de mascotas en adopción y el botón "Ver mascotas" |
| `RefugioPage` | Datos del refugio y sus publicaciones activas, con las mismas tarjetas que el listado del refugio pero sin estado ni botones. Botón Volver |
| `portal/api/resources/veterinarias-resource` | `getVeterinarias()` → `GET /veterinarias/listado` · `getServicios()` → `GET /veterinarias/servicios` |
| `portal/api/resources/refugios-resource` | `getRefugios()` → `GET /refugios/listado` · `getRefugio(idRefugio)` → `GET /refugios/{idRefugio}` · `getPublicacionesActivas(idRefugio)` → `GET /refugios/{idRefugio}/publicaciones` |
| `portal/api/models` | `IVeterinaria`, `IServicio`, `IRefugio`, `IAdopcion` |

Los datos que muestra cada página llegan por resolver y la página los recibe como `input()` con el mismo nombre de la clave de `resolve`.
