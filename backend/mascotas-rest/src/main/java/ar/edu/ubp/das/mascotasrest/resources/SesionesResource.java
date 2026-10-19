package ar.edu.ubp.das.mascotasrest.resources;

import ar.edu.ubp.das.mascotasrest.BE.LoginDataBE;
import ar.edu.ubp.das.mascotasrest.BE.UsuarioBE;
import ar.edu.ubp.das.mascotasrest.repositories.SesionesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sesiones")
public class SesionesResource {

    @Autowired
    private SesionesRepository repository;

    @PostMapping
    public ResponseEntity iniciarSesion(@RequestBody LoginDataBE login) {
        List<UsuarioBE> usuarios = repository.insertarSesion(login.getCuil(), login.getClave());

        if (usuarios.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("El CUIL o la contraseña son incorrectos.");
        }

        return ResponseEntity.ok(usuarios.get(0));
    }

}
