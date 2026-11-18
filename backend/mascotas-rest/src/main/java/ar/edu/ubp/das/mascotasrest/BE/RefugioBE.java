package ar.edu.ubp.das.mascotasrest.BE;

public class RefugioBE {

    private int idRefugio;
    private String razonSocial;
    private String domicilio;
    private String telefono;
    private String correo;
    private String horarios;
    private int cantPublicaciones;

    public int getIdRefugio() {
        return idRefugio;
    }

    public void setIdRefugio(int idRefugio) {
        this.idRefugio = idRefugio;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getHorarios() {
        return horarios;
    }

    public void setHorarios(String horarios) {
        this.horarios = horarios;
    }

    public int getCantPublicaciones() {
        return cantPublicaciones;
    }

    public void setCantPublicaciones(int cantPublicaciones) {
        this.cantPublicaciones = cantPublicaciones;
    }

}
