package ar.edu.ubp.das.mascotasrest.veterinarias.resources;

import ar.edu.ubp.das.mascotasrest.veterinarias.BE.AtencionVeterinariaBE;
import ar.edu.ubp.das.mascotasrest.veterinarias.BE.ConfiguracionVeterinariaBE;
import ar.edu.ubp.das.mascotasrest.veterinarias.BE.LogIntegracionBE;
import ar.edu.ubp.das.mascotasrest.veterinarias.BE.MascotaVeterinariaBE;
import ar.edu.ubp.das.mascotasrest.veterinarias.BE.NroRegMunicipalBE;
import ar.edu.ubp.das.mascotasrest.veterinarias.BE.ResultadoIntegracionBE;
import ar.edu.ubp.das.mascotasrest.veterinarias.BE.TipoAtencionBE;
import ar.edu.ubp.das.mascotasrest.veterinarias.BE.VeterinariaBE;
import ar.edu.ubp.das.mascotasrest.veterinarias.clientes.ClienteVeterinaria;
import ar.edu.ubp.das.mascotasrest.veterinarias.repositories.VeterinariasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/veterinarias")
public class VeterinariasResource {

    private static final String PROCESO = "INTEGRACION VETERINARIAS";

    @Autowired
    private VeterinariasRepository repository;

    // Spring arma este mapa con todas las clases que extienden ClienteVeterinaria:
    // la clave es el nombre del @Component ("REST", "SOAP"), que es el valor de TECNOLOGIA en la base
    @Autowired
    private Map<String, ClienteVeterinaria> clientes;

    @GetMapping("/listado")
    public ResponseEntity<List<VeterinariaBE>> getVeterinarias() {
        return ResponseEntity.ok(repository.getVeterinarias());
    }

    @GetMapping("/servicios")
    public ResponseEntity<List<TipoAtencionBE>> getServicios() {
        return ResponseEntity.ok(repository.getServicios());
    }

    @PostMapping("/integracion")
    public ResponseEntity<List<ResultadoIntegracionBE>> integrarVeterinarias() {
        List<ResultadoIntegracionBE> resultados = new ArrayList<>();
        for (ConfiguracionVeterinariaBE veterinaria : repository.getConfiguraciones()) {
            resultados.add(integrarVeterinaria(veterinaria));
        }
        return ResponseEntity.ok(resultados);
    }

    private ResultadoIntegracionBE integrarVeterinaria(ConfiguracionVeterinariaBE veterinaria) {
        ResultadoIntegracionBE resultado = new ResultadoIntegracionBE();
        resultado.setIdVeterinaria(veterinaria.getIdVeterinaria());
        resultado.setRazonSocial(veterinaria.getRazonSocial());
        resultado.setTecnologia(veterinaria.getTecnologia());
        resultado.setErrores(new ArrayList<>());

        ClienteVeterinaria cliente = veterinaria.getTecnologia() == null ? null : clientes.get(veterinaria.getTecnologia());
        if (cliente == null || vacio(veterinaria.getUrl()) || vacio(veterinaria.getParametros()) || vacio(veterinaria.getApiKey())) {
            registrarError(veterinaria, resultado, null, "La veterinaria no tiene bien configurada la integración (tecnología, URL, parámetros o API Key).");
            resultado.setEstado("NO DISPONIBLE");
            return resultado;
        }

        try {
            registrarMascotas(veterinaria, cliente, resultado);
            registrarAtenciones(veterinaria, cliente, resultado);
            resultado.setEstado(resultado.getErrores().isEmpty() ? "OK" : "CON ERRORES");
        }
        catch (Exception e) {
            // La veterinaria no responde: se guarda el log y se sigue con la próxima
            registrarError(veterinaria, resultado, null, "No se pudo comunicar con la veterinaria: " + getMensaje(e));
            resultado.setEstado("NO DISPONIBLE");
        }

        registrarLog(veterinaria, null,
                "Mascotas informadas: " + resultado.getMascotasInformadas() +
                ". Atenciones nuevas: " + resultado.getAtencionesNuevas() +
                ". Atenciones actualizadas: " + resultado.getAtencionesActualizadas() +
                ". Errores: " + resultado.getErrores().size() + ".", false);

        return resultado;
    }

    private void registrarMascotas(ConfiguracionVeterinariaBE veterinaria, ClienteVeterinaria cliente, ResultadoIntegracionBE resultado) {
        for (MascotaVeterinariaBE mascota : cliente.getMascotasPendientes(veterinaria)) {
            String referencia = "Mascota " + mascota.getIdInterno() + " (" + mascota.getNombre() + ")";
            try {
                if (vacio(mascota.getIdInterno()) ||
                    vacio(mascota.getNombre()) ||
                    vacio(mascota.getEspecie()) ||
                    vacio(mascota.getSexo()) ||
                    vacio(mascota.getCuilResponsable()) ||
                    vacio(mascota.getApellidoResponsable()) ||
                    vacio(mascota.getNombreResponsable())) {
                    registrarError(veterinaria, resultado, referencia, "Faltan datos obligatorios de la mascota o de su responsable.");
                    continue;
                }

                NroRegMunicipalBE nroRegMunicipal = new NroRegMunicipalBE();
                nroRegMunicipal.setIdInterno(mascota.getIdInterno());
                nroRegMunicipal.setNroRegMunicipal(repository.insertarMascota(mascota));

                cliente.actNroRegMunicipal(veterinaria, nroRegMunicipal);
                resultado.setMascotasInformadas(resultado.getMascotasInformadas() + 1);
            }
            catch (Exception e) {
                registrarError(veterinaria, resultado, referencia, getMensaje(e));
            }
        }
    }

    private void registrarAtenciones(ConfiguracionVeterinariaBE veterinaria, ClienteVeterinaria cliente, ResultadoIntegracionBE resultado) {
        for (AtencionVeterinariaBE atencion : cliente.getAtenciones(veterinaria)) {
            String referencia = "Atención NRM " + atencion.getNroRegMunicipal() + " del " + atencion.getFechaAtencion() + " (" + atencion.getCodTipoAtencion() + ")";
            try {
                if (atencion.getNroRegMunicipal() <= 0 ||
                    vacio(atencion.getFechaAtencion()) ||
                    vacio(atencion.getCodTipoAtencion()) ||
                    vacio(atencion.getCuilProfesional()) ||
                    vacio(atencion.getApellidoProfesional()) ||
                    vacio(atencion.getNombreProfesional())) {
                    registrarError(veterinaria, resultado, referencia, "Faltan datos obligatorios de la atención o del profesional.");
                    continue;
                }

                atencion.setIdVeterinaria(veterinaria.getIdVeterinaria());

                switch (repository.insertarAtencion(atencion)) {
                    case "N" -> resultado.setAtencionesNuevas(resultado.getAtencionesNuevas() + 1);
                    case "A" -> resultado.setAtencionesActualizadas(resultado.getAtencionesActualizadas() + 1);
                    case "X" -> registrarError(veterinaria, resultado, referencia, "El NRM no existe en el Registro Único.");
                    default -> { }
                }
            }
            catch (Exception e) {
                registrarError(veterinaria, resultado, referencia, getMensaje(e));
            }
        }
    }

    private void registrarError(ConfiguracionVeterinariaBE veterinaria, ResultadoIntegracionBE resultado, String referencia, String detalle) {
        resultado.getErrores().add(referencia == null ? detalle : referencia + ": " + detalle);
        registrarLog(veterinaria, referencia, detalle, true);
    }

    private void registrarLog(ConfiguracionVeterinariaBE veterinaria, String referencia, String detalle, boolean error) {
        LogIntegracionBE log = new LogIntegracionBE();
        log.setProceso(PROCESO);
        log.setOrganizacion(veterinaria.getRazonSocial());
        log.setReferencia(referencia);
        log.setDetalle(detalle);
        log.setError(error);
        repository.insertarLog(log);
    }

    private String getMensaje(Exception e) {
        Throwable causa = e;
        while (causa.getCause() != null) {
            causa = causa.getCause();
        }
        return causa.getMessage() == null ? causa.getClass().getSimpleName() : causa.getMessage();
    }

    private boolean vacio(String valor) {
        return valor == null || valor.isBlank();
    }

}
