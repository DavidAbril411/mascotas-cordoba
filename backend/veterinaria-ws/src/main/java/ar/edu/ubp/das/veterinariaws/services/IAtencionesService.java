package ar.edu.ubp.das.veterinariaws.services;

import ar.edu.ubp.das.veterinariaws.BE.AtencionBE;
import jakarta.jws.WebMethod;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;

import java.util.List;

@WebService
public interface IAtencionesService {

    @WebMethod
    @WebResult(name = "atencion")
    List<AtencionBE> getAtenciones();

}
