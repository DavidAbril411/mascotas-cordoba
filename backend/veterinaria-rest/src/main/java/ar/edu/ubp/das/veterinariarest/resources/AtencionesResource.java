package ar.edu.ubp.das.veterinariarest.resources;

import ar.edu.ubp.das.veterinariarest.BE.AtencionBE;
import ar.edu.ubp.das.veterinariarest.repositories.AtencionesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/atenciones")
public class AtencionesResource {

    @Autowired
    private AtencionesRepository repository;

    @PostMapping("/listado")
    public ResponseEntity<List<AtencionBE>> getAtenciones() {
        return ResponseEntity.ok(repository.getAtenciones());
    }

}
