package ar.edu.ubp.das.veterinariaws.services;

import ar.edu.ubp.das.veterinariaws.BE.MascotaPendienteBE;
import ar.edu.ubp.das.veterinariaws.BE.NroRegMunicipalBE;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;

import java.util.List;

@WebService
public interface IMascotasService {

    @WebMethod
    @WebResult(name = "mascota")
    List<MascotaPendienteBE> getMascotasPendientes();

    @WebMethod
    void actNroRegMunicipal(
        @WebParam(name = "nroRegMunicipal") NroRegMunicipalBE nroRegMunicipal
    );

}
