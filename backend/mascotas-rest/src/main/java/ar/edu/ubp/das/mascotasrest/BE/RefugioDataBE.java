package ar.edu.ubp.das.mascotasrest.BE;

public class RefugioDataBE {

    private String razonSocial;
    private String correo;
    private String telefono;
    private String domicilio;
    private String habilitacionMunicipal;
    private CiudadanoDataBE responsable;

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getHabilitacionMunicipal() {
        return habilitacionMunicipal;
    }

    public void setHabilitacionMunicipal(String habilitacionMunicipal) {
        this.habilitacionMunicipal = habilitacionMunicipal;
    }

    public CiudadanoDataBE getResponsable() {
        return responsable;
    }

    public void setResponsable(CiudadanoDataBE responsable) {
        this.responsable = responsable;
    }

}
