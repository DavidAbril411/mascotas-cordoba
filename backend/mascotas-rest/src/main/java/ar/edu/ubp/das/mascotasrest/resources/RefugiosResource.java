package ar.edu.ubp.das.mascotasrest.resources;

import ar.edu.ubp.das.mascotasrest.BE.CiudadanoDataBE;
import ar.edu.ubp.das.mascotasrest.BE.RefugioDataBE;
import ar.edu.ubp.das.mascotasrest.repositories.CiudadanosRepository;
import ar.edu.ubp.das.mascotasrest.repositories.RefugiosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/refugios")
public class RefugiosResource {

    @Autowired
    private RefugiosRepository repository;

    @Autowired
    private CiudadanosRepository ciudadanosRepository;

    @PostMapping
    public ResponseEntity registrarRefugio(@RequestBody RefugioDataBE refugio) {
        CiudadanoDataBE responsable = refugio.getResponsable();

        if (vacio(refugio.getRazonSocial()) ||
            vacio(refugio.getCorreo()) ||
            vacio(refugio.getHabilitacionMunicipal()) ||
            responsable == null ||
            vacio(responsable.getApellido()) ||
            vacio(responsable.getNombre()) ||
            vacio(responsable.getCuil()) ||
            vacio(responsable.getClave()) ||
            vacio(responsable.getCorreo())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Faltan datos obligatorios del refugio o del responsable.");
        }

        if (!ciudadanosRepository.getCiudadano(responsable.getCuil()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El CUIL del responsable ya está registrado.");
        }

        return ResponseEntity.ok(repository.insertarRefugio(refugio));
    }

    private boolean vacio(String valor) {
        return valor == null || valor.isBlank();
    }

}
