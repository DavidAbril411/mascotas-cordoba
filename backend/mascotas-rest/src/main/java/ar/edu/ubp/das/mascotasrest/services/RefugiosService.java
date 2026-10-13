package ar.edu.ubp.das.mascotasrest.services;

import ar.edu.ubp.das.mascotasrest.BE.CiudadanoDataBE;
import ar.edu.ubp.das.mascotasrest.BE.RefugioDataBE;
import ar.edu.ubp.das.mascotasrest.repositories.CiudadanosRepository;
import ar.edu.ubp.das.mascotasrest.repositories.RefugiosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RefugiosService {

    @Autowired
    private RefugiosRepository repository;

    @Autowired
    private CiudadanosRepository ciudadanosRepository;

    public boolean datosCompletos(RefugioDataBE refugio) {
        CiudadanoDataBE responsable = refugio.getResponsable();

        return !vacio(refugio.getRazonSocial()) &&
               !vacio(refugio.getCorreo()) &&
               !vacio(refugio.getHabilitacionMunicipal()) &&
               responsable != null &&
               !vacio(responsable.getApellido()) &&
               !vacio(responsable.getNombre()) &&
               !vacio(responsable.getCuil()) &&
               !vacio(responsable.getClave()) &&
               !vacio(responsable.getCorreo());
    }

    public boolean existeCiudadano(String cuil) {
        return !ciudadanosRepository.getCiudadano(cuil).isEmpty();
    }

    @Transactional
    public int registrarRefugio(RefugioDataBE refugio) {
        int idResponsable = ciudadanosRepository.insertarCiudadano(refugio.getResponsable());
        return repository.insertarRefugio(refugio, idResponsable);
    }

    private boolean vacio(String valor) {
        return valor == null || valor.isBlank();
    }

}
