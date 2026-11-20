package ar.edu.ubp.das.veterinariarest.resources;

import ar.edu.ubp.das.veterinariarest.BE.MascotaPendienteBE;
import ar.edu.ubp.das.veterinariarest.BE.NroRegMunicipalBE;
import ar.edu.ubp.das.veterinariarest.repositories.MascotasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mascotas")
public class MascotasResource {

    @Autowired
    private MascotasRepository repository;

    @PostMapping("/pendientes")
    public ResponseEntity<List<MascotaPendienteBE>> getMascotasPendientes() {
        return ResponseEntity.ok(repository.getMascotasPendientes());
    }

    @PostMapping("/nro-reg-municipal")
    @Transactional
    public ResponseEntity actNroRegMunicipal(@RequestBody NroRegMunicipalBE nroRegMunicipal) {
        if (nroRegMunicipal.getIdInterno() == null ||
            !nroRegMunicipal.getIdInterno().matches("\\d+") ||
            nroRegMunicipal.getNroRegMunicipal() <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Faltan datos obligatorios del número de registro municipal.");
        }

        if (repository.actNroRegMunicipal(nroRegMunicipal) == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La mascota no existe.");
        }

        return ResponseEntity.ok().build();
    }

}
