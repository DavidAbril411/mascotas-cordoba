package ar.edu.ubp.das.mascotasrest.services;

import ar.edu.ubp.das.mascotasrest.BE.LoginDataBE;
import ar.edu.ubp.das.mascotasrest.BE.UsuarioBE;
import ar.edu.ubp.das.mascotasrest.repositories.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuariosService {

    @Autowired
    private UsuariosRepository repository;

    public UsuarioBE iniciarSesion(LoginDataBE login) {
        if (login.getCuil() == null || login.getCuil().isBlank() ||
            login.getClave() == null || login.getClave().isBlank()) {
            return null;
        }

        List<UsuarioBE> usuarios = repository.getUsuarioRefugio(login.getCuil(), login.getClave());

        return usuarios.isEmpty() ? null : usuarios.get(0);
    }

}
