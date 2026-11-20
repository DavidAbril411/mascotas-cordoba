package ar.edu.ubp.das.veterinariaws.services;

import ar.edu.ubp.das.veterinariaws.BE.MascotaPendienteBE;
import ar.edu.ubp.das.veterinariaws.BE.NroRegMunicipalBE;
import ar.edu.ubp.das.veterinariaws.repositories.MascotasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MascotasService implements IMascotasService {

    @Autowired
    private MascotasRepository repository;

    @Override
    public List<MascotaPendienteBE> getMascotasPendientes() {
        return repository.getMascotasPendientes();
    }

    @Override
    @Transactional
    public void actNroRegMunicipal(NroRegMunicipalBE nroRegMunicipal) {
        if (nroRegMunicipal == null ||
            nroRegMunicipal.getIdInterno() == null ||
            !nroRegMunicipal.getIdInterno().matches("\\d+") ||
            nroRegMunicipal.getNroRegMunicipal() <= 0) {
            throw new RuntimeException("Faltan datos obligatorios del número de registro municipal.");
        }

        if (repository.actNroRegMunicipal(nroRegMunicipal) == 0) {
            throw new RuntimeException("La mascota no existe.");
        }
    }

}
