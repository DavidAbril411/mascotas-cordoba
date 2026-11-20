# veterinaria-ws

Sistema de Gestión Veterinaria con servicios **SOAP** (JAX-WS + Apache CXF, como `localidades-ws`). Emula a las veterinarias adheridas que se integran por SOAP: cada instancia es una organización independiente, con su propia base, su puerto y su API Key.

| Veterinaria | Puerto | Base | API Key | En la demo |
|---|---|---|---|---|
| 3 - VETERINARIA HUELLAS | 8095 | `veterinaria_3` | `VET3pL6qR1sT5vW9xZ2bC4nM8kH3jD7fG0aJ` | levantada |
| 4 - CENTRO VETERINARIO ALBERDI | 8096 | `veterinaria_4` | `VET4sD2fG6hJ1kL5zX9cV3bN7mQ4wE8rT0aJ` | apagada (tolerancia a fallos) |

La base es la misma que la de `veterinaria-rest` (`database/script-veterinaria.sql` + `database/datos-veterinaria-N.sql`), ver su README.

## Ejecutar

Desde IntelliJ: Run a `VeterinariaWsApplication`. Los valores por defecto son los de la veterinaria 3 (puerto 8095, base `veterinaria_3`, clave `Pyxis.2026`). Para la veterinaria 4: `PORT=8096;DB_NAME=veterinaria_4;API_KEY=VET4sD2fG6hJ1kL5zX9cV3bN7mQ4wE8rT0aJ`.

Con Docker: `mvn clean package` y `docker compose up --build veterinaria-3`. El WAR se copia como `ROOT.war` para que la URL sea la misma que desde IntelliJ.

## Contrato

Mismas operaciones y mismos datos que la versión REST (ver `backend/veterinaria-rest/README.md`). La API Key viaja en el header SOAP `apiKey`; si falta o no coincide, el servicio responde un SOAP Fault `Invalid API Key`.

| Servicio | WSDL | Operación | Parámetro | Resultado |
|---|---|---|---|---|
| Mascotas | `http://localhost:8095/services/mascotas?wsdl` | `getMascotasPendientes` | — | lista de `mascota` |
| | | `actNroRegMunicipal` | `nroRegMunicipal` (`idInterno`, `nroRegMunicipal`) | vacío, o SOAP Fault `La mascota no existe.` |
| Atenciones | `http://localhost:8095/services/atenciones?wsdl` | `getAtenciones` | — | lista de `atencion` |

Ejemplo:

```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ser="http://services.veterinariaws.das.ubp.edu.ar/">
   <soapenv:Header>
      <apiKey>VET3pL6qR1sT5vW9xZ2bC4nM8kH3jD7fG0aJ</apiKey>
   </soapenv:Header>
   <soapenv:Body>
      <ser:actNroRegMunicipal>
         <nroRegMunicipal>
            <idInterno>1</idInterno>
            <nroRegMunicipal>3</nroRegMunicipal>
         </nroRegMunicipal>
      </ser:actNroRegMunicipal>
   </soapenv:Body>
</soapenv:Envelope>
```
