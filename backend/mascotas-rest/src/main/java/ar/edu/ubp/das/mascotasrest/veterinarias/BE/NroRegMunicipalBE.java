package ar.edu.ubp.das.mascotasrest.veterinarias.BE;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "nroRegMunicipal")
@XmlAccessorType(XmlAccessType.FIELD)
public class NroRegMunicipalBE {

    private String idInterno;
    private int nroRegMunicipal;

    public String getIdInterno() {
        return idInterno;
    }

    public void setIdInterno(String idInterno) {
        this.idInterno = idInterno;
    }

    public int getNroRegMunicipal() {
        return nroRegMunicipal;
    }

    public void setNroRegMunicipal(int nroRegMunicipal) {
        this.nroRegMunicipal = nroRegMunicipal;
    }

}
