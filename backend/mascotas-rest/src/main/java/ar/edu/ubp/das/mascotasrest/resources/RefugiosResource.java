package ar.edu.ubp.das.mascotasrest.resources;

import ar.edu.ubp.das.mascotasrest.BE.CiudadanoDataBE;
import ar.edu.ubp.das.mascotasrest.BE.PublicacionBE;
import ar.edu.ubp.das.mascotasrest.BE.RefugioBE;
import ar.edu.ubp.das.mascotasrest.BE.RefugioDataBE;
import ar.edu.ubp.das.mascotasrest.repositories.CiudadanosRepository;
import ar.edu.ubp.das.mascotasrest.repositories.RefugiosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/refugios")
public class RefugiosResource {

    @Autowired
    private RefugiosRepository repository;

    @Autowired
    private CiudadanosRepository ciudadanosRepository;

    @GetMapping("/listado")
    public ResponseEntity<List<RefugioBE>> getRefugios() {
        return ResponseEntity.ok(repository.getRefugios());
    }

    @GetMapping("/{idRefugio}")
    public ResponseEntity getRefugio(@PathVariable int idRefugio) {
        RefugioBE refugio = new RefugioBE();
        refugio.setIdRefugio(idRefugio);

        List<RefugioBE> refugios = repository.getRefugio(refugio);
        if (refugios.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El refugio no existe o no está habilitado.");
        }

        return ResponseEntity.ok(refugios.get(0));
    }

    @GetMapping("/{idRefugio}/publicaciones")
    public ResponseEntity<List<PublicacionBE>> getPublicacionesActivas(@PathVariable int idRefugio) {
        RefugioBE refugio = new RefugioBE();
        refugio.setIdRefugio(idRefugio);

        return ResponseEntity.ok(repository.getPublicacionesActivas(refugio));
    }

    @PostMapping
    @Transactional
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

        if (!ciudadanosRepository.getCiudadano(responsable).isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El CUIL del responsable ya está registrado.");
        }

        return ResponseEntity.ok(repository.insertarRefugio(refugio));
    }

    private boolean vacio(String valor) {
        return valor == null || valor.isBlank();
    }

}
