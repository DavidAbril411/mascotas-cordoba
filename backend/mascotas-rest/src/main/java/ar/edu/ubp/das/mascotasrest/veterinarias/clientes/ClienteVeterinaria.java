package ar.edu.ubp.das.mascotasrest.veterinarias.clientes;

import ar.edu.ubp.das.mascotasrest.veterinarias.BE.AtencionVeterinariaBE;
import ar.edu.ubp.das.mascotasrest.veterinarias.BE.ConfiguracionVeterinariaBE;
import ar.edu.ubp.das.mascotasrest.veterinarias.BE.MascotaVeterinariaBE;
import ar.edu.ubp.das.mascotasrest.veterinarias.BE.NroRegMunicipalBE;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.List;

public abstract class ClienteVeterinaria {

    public abstract List<MascotaVeterinariaBE> getMascotasPendientes(ConfiguracionVeterinariaBE veterinaria);

    public abstract void actNroRegMunicipal(ConfiguracionVeterinariaBE veterinaria, NroRegMunicipalBE nroRegMunicipal);

    public abstract List<AtencionVeterinariaBE> getAtenciones(ConfiguracionVeterinariaBE veterinaria);

    protected JsonObject getParametros(ConfiguracionVeterinariaBE veterinaria) {
        return JsonParser.parseString(veterinaria.getParametros()).getAsJsonObject();
    }

}
