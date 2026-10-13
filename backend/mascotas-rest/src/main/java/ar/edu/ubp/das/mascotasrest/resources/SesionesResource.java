package ar.edu.ubp.das.mascotasrest.resources;

import ar.edu.ubp.das.mascotasrest.BE.LoginDataBE;
import ar.edu.ubp.das.mascotasrest.BE.UsuarioBE;
import ar.edu.ubp.das.mascotasrest.services.UsuariosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sesiones")
public class SesionesResource {

    @Autowired
    private UsuariosService service;

    @PostMapping
    public ResponseEntity<UsuarioBE> iniciarSesion(@RequestBody LoginDataBE login) {
        UsuarioBE usuario = service.iniciarSesion(login);

        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(usuario);
    }

}
