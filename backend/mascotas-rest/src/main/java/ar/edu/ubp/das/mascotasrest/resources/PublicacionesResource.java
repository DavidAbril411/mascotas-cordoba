package ar.edu.ubp.das.mascotasrest.resources;

import ar.edu.ubp.das.mascotasrest.BE.EspecieBE;
import ar.edu.ubp.das.mascotasrest.BE.PublicacionBE;
import ar.edu.ubp.das.mascotasrest.BE.PublicacionDataBE;
import ar.edu.ubp.das.mascotasrest.repositories.PublicacionesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/publicaciones")
public class PublicacionesResource {

    @Autowired
    private PublicacionesRepository repository;

    @GetMapping("/especies")
    public ResponseEntity<List<EspecieBE>> getEspecies() {
        return ResponseEntity.ok(repository.getEspecies());
    }

    @GetMapping("/listado")
    public ResponseEntity<List<PublicacionBE>> getPublicaciones(@RequestAttribute int idRefugio) {
        return ResponseEntity.ok(repository.getPublicaciones(idRefugio));
    }

    @PostMapping("/publicacion")
    public ResponseEntity insertarPublicacion(@RequestAttribute int idRefugio, @RequestBody PublicacionDataBE publicacion) {
        if (vacio(publicacion.getNombre()) ||
            publicacion.getCodEspecie() <= 0 ||
            vacio(publicacion.getSexo()) ||
            vacio(publicacion.getCaracteristicasMascota()) ||
            vacio(publicacion.getCondicionAdopcion()) ||
            vacio(publicacion.getFoto())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Faltan datos obligatorios de la publicación.");
        }

        return ResponseEntity.ok(repository.insertarPublicacion(idRefugio, publicacion));
    }

    @PostMapping("/publicacion/{nroPublicacion}/{estadoPublicacion}")
    public ResponseEntity actPublicacion(@RequestAttribute int idRefugio, @PathVariable int nroPublicacion, @PathVariable String estadoPublicacion) {
        if (!estadoPublicacion.equals("A") &&
            !estadoPublicacion.equals("P") &&
            !estadoPublicacion.equals("F")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El estado de la publicación no es válido.");
        }

        repository.actPublicacion(idRefugio, nroPublicacion, estadoPublicacion);
        return ResponseEntity.ok().build();
    }

    private boolean vacio(String valor) {
        return valor == null || valor.isBlank();
    }

}
