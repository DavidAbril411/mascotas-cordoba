package ar.edu.ubp.das.mascotasrest.veterinarias.BE;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonPropertyOrder({"idVeterinaria", "razonSocial", "tecnologia", "estado", "mascotasInformadas", "atencionesNuevas", "atencionesActualizadas", "errores"})
public class ResultadoIntegracionBE {

    private int idVeterinaria;
    private String razonSocial;
    private String tecnologia;
    private String estado;
    private int mascotasInformadas;
    private int atencionesNuevas;
    private int atencionesActualizadas;
    private List<String> errores;

    public int getIdVeterinaria() {
        return idVeterinaria;
    }

    public void setIdVeterinaria(int idVeterinaria) {
        this.idVeterinaria = idVeterinaria;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getTecnologia() {
        return tecnologia;
    }

    public void setTecnologia(String tecnologia) {
        this.tecnologia = tecnologia;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getMascotasInformadas() {
        return mascotasInformadas;
    }

    public void setMascotasInformadas(int mascotasInformadas) {
        this.mascotasInformadas = mascotasInformadas;
    }

    public int getAtencionesNuevas() {
        return atencionesNuevas;
    }

    public void setAtencionesNuevas(int atencionesNuevas) {
        this.atencionesNuevas = atencionesNuevas;
    }

    public int getAtencionesActualizadas() {
        return atencionesActualizadas;
    }

    public void setAtencionesActualizadas(int atencionesActualizadas) {
        this.atencionesActualizadas = atencionesActualizadas;
    }

    public List<String> getErrores() {
        return errores;
    }

    public void setErrores(List<String> errores) {
        this.errores = errores;
    }

}
