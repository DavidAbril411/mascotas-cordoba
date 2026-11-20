package ar.edu.ubp.das.mascotasrest.veterinarias.clientes;

import ar.edu.ubp.das.mascotasrest.utils.Httpful;
import ar.edu.ubp.das.mascotasrest.veterinarias.BE.AtencionVeterinariaBE;
import ar.edu.ubp.das.mascotasrest.veterinarias.BE.ConfiguracionVeterinariaBE;
import ar.edu.ubp.das.mascotasrest.veterinarias.BE.MascotaVeterinariaBE;
import ar.edu.ubp.das.mascotasrest.veterinarias.BE.NroRegMunicipalBE;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("REST")
public class ClienteRest extends ClienteVeterinaria {

    @Override
    public List<MascotaVeterinariaBE> getMascotasPendientes(ConfiguracionVeterinariaBE veterinaria) {
        return new Httpful(veterinaria.getUrl())
                .path(getRuta(veterinaria, "mascotasPendientes"))
                .apiKey("X-API-Key", veterinaria.getApiKey())
                .post(new JsonObject())
                .execute(new TypeToken<List<MascotaVeterinariaBE>>() {}.getType());
    }

    @Override
    public void actNroRegMunicipal(ConfiguracionVeterinariaBE veterinaria, NroRegMunicipalBE nroRegMunicipal) {
        new Httpful(veterinaria.getUrl())
                .path(getRuta(veterinaria, "nroRegMunicipal"))
                .apiKey("X-API-Key", veterinaria.getApiKey())
                .post(nroRegMunicipal)
                .execute(String.class);
    }

    @Override
    public List<AtencionVeterinariaBE> getAtenciones(ConfiguracionVeterinariaBE veterinaria) {
        return new Httpful(veterinaria.getUrl())
                .path(getRuta(veterinaria, "atenciones"))
                .apiKey("X-API-Key", veterinaria.getApiKey())
                .post(new JsonObject())
                .execute(new TypeToken<List<AtencionVeterinariaBE>>() {}.getType());
    }

    private String getRuta(ConfiguracionVeterinariaBE veterinaria, String operacion) {
        return getParametros(veterinaria).get(operacion).getAsString();
    }

}
