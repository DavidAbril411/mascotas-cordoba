package ar.edu.ubp.das.veterinariarest.BE;

public class AtencionBE {

    private int nroRegMunicipal;
    private boolean vive;
    private String fechaAtencion;
    private String codTipoAtencion;
    private String detalle;
    private String fechaProxima;
    private String cuilProfesional;
    private String apellidoProfesional;
    private String nombreProfesional;
    private String correoProfesional;
    private String matriculaProfesional;

    public int getNroRegMunicipal() {
        return nroRegMunicipal;
    }

    public void setNroRegMunicipal(int nroRegMunicipal) {
        this.nroRegMunicipal = nroRegMunicipal;
    }

    public boolean isVive() {
        return vive;
    }

    public void setVive(boolean vive) {
        this.vive = vive;
    }

    public String getFechaAtencion() {
        return fechaAtencion;
    }

    public void setFechaAtencion(String fechaAtencion) {
        this.fechaAtencion = fechaAtencion;
    }

    public String getCodTipoAtencion() {
        return codTipoAtencion;
    }

    public void setCodTipoAtencion(String codTipoAtencion) {
        this.codTipoAtencion = codTipoAtencion;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getFechaProxima() {
        return fechaProxima;
    }

    public void setFechaProxima(String fechaProxima) {
        this.fechaProxima = fechaProxima;
    }

    public String getCuilProfesional() {
        return cuilProfesional;
    }

    public void setCuilProfesional(String cuilProfesional) {
        this.cuilProfesional = cuilProfesional;
    }

    public String getApellidoProfesional() {
        return apellidoProfesional;
    }

    public void setApellidoProfesional(String apellidoProfesional) {
        this.apellidoProfesional = apellidoProfesional;
    }

    public String getNombreProfesional() {
        return nombreProfesional;
    }

    public void setNombreProfesional(String nombreProfesional) {
        this.nombreProfesional = nombreProfesional;
    }

    public String getCorreoProfesional() {
        return correoProfesional;
    }

    public void setCorreoProfesional(String correoProfesional) {
        this.correoProfesional = correoProfesional;
    }

    public String getMatriculaProfesional() {
        return matriculaProfesional;
    }

    public void setMatriculaProfesional(String matriculaProfesional) {
        this.matriculaProfesional = matriculaProfesional;
    }

}
