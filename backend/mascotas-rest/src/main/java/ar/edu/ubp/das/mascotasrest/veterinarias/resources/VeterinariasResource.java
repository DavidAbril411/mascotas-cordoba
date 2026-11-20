package ar.edu.ubp.das.mascotasrest.resources;

import ar.edu.ubp.das.mascotasrest.BE.TipoAtencionBE;
import ar.edu.ubp.das.mascotasrest.BE.VeterinariaBE;
import ar.edu.ubp.das.mascotasrest.repositories.VeterinariasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/veterinarias")
public class VeterinariasResource {

    @Autowired
    private VeterinariasRepository repository;

    @GetMapping("/listado")
    public ResponseEntity<List<VeterinariaBE>> getVeterinarias() {
        return ResponseEntity.ok(repository.getVeterinarias());
    }

    @GetMapping("/servicios")
    public ResponseEntity<List<TipoAtencionBE>> getServicios() {
        return ResponseEntity.ok(repository.getServicios());
    }

}
