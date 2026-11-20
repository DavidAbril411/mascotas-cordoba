package ar.edu.ubp.das.veterinariaws.services;

import ar.edu.ubp.das.veterinariaws.BE.AtencionBE;
import ar.edu.ubp.das.veterinariaws.repositories.AtencionesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AtencionesService implements IAtencionesService {

    @Autowired
    private AtencionesRepository repository;

    @Override
    public List<AtencionBE> getAtenciones() {
        return repository.getAtenciones();
    }

}
