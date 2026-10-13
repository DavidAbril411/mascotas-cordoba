package ar.edu.ubp.das.mascotasrest.resources;

import ar.edu.ubp.das.mascotasrest.BE.RefugioDataBE;
import ar.edu.ubp.das.mascotasrest.services.RefugiosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/refugios")
public class RefugiosResource {

    @Autowired
    private RefugiosService service;

    @PostMapping
    public ResponseEntity<Integer> registrarRefugio(@RequestBody RefugioDataBE refugio) {
        if (!service.datosCompletos(refugio) ||
            service.existeCiudadano(refugio.getResponsable().getCuil())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.ok(service.registrarRefugio(refugio));
    }

}
