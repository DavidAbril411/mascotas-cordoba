package ar.edu.ubp.das.mascotasrest.veterinarias.clientes;

import ar.edu.ubp.das.mascotasrest.utils.SOAPClient;
import ar.edu.ubp.das.mascotasrest.veterinarias.BE.AtencionVeterinariaBE;
import ar.edu.ubp.das.mascotasrest.veterinarias.BE.ConfiguracionVeterinariaBE;
import ar.edu.ubp.das.mascotasrest.veterinarias.BE.MascotaVeterinariaBE;
import ar.edu.ubp.das.mascotasrest.veterinarias.BE.NroRegMunicipalBE;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("SOAP")
public class ClienteSoap extends ClienteVeterinaria {

    @Override
    public List<MascotaVeterinariaBE> getMascotasPendientes(ConfiguracionVeterinariaBE veterinaria) {
        JsonObject operacion = getOperacion(veterinaria, "mascotasPendientes");
        return getCliente(veterinaria, operacion)
                .callServiceForList(MascotaVeterinariaBE.class, getRespuesta(operacion));
    }

    @Override
    public void actNroRegMunicipal(ConfiguracionVeterinariaBE veterinaria, NroRegMunicipalBE nroRegMunicipal) {
        JsonObject operacion = getOperacion(veterinaria, "nroRegMunicipal");
        getCliente(veterinaria, operacion)
                .callServiceForObject(String.class, getRespuesta(operacion), nroRegMunicipal);
    }

    @Override
    public List<AtencionVeterinariaBE> getAtenciones(ConfiguracionVeterinariaBE veterinaria) {
        JsonObject operacion = getOperacion(veterinaria, "atenciones");
        return getCliente(veterinaria, operacion)
                .callServiceForList(AtencionVeterinariaBE.class, getRespuesta(operacion));
    }

    private JsonObject getOperacion(ConfiguracionVeterinariaBE veterinaria, String operacion) {
        return getParametros(veterinaria).getAsJsonObject(operacion);
    }

    private SOAPClient getCliente(ConfiguracionVeterinariaBE veterinaria, JsonObject operacion) {
        return SOAPClient.fromConfig(operacion.toString())
                .wsdlUrl(veterinaria.getUrl() + operacion.get("servicio").getAsString() + "?wsdl")
                .apiKey(veterinaria.getApiKey())
                .operation(operacion.get("operacion").getAsString())
                .build();
    }

    private String getRespuesta(JsonObject operacion) {
        return operacion.get("operacion").getAsString() + "Response";
    }

}
