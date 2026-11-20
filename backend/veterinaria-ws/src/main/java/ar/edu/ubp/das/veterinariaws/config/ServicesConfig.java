package ar.edu.ubp.das.veterinariaws.config;

import ar.edu.ubp.das.veterinariaws.services.IAtencionesService;
import ar.edu.ubp.das.veterinariaws.services.IMascotasService;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServicesConfig {

    @Value("${security.api-key}")
    private String apiKey;

    @Bean
    public EndpointImpl mascotasEndpoint(Bus bus, IMascotasService service) {
        return buildEndpoint(bus, service, "/mascotas");
    }

    @Bean
    public EndpointImpl atencionesEndpoint(Bus bus, IAtencionesService service) {
        return buildEndpoint(bus, service, "/atenciones");
    }

    private EndpointImpl buildEndpoint(Bus bus, Object service, String path) {
        EndpointImpl endpoint =
                new EndpointImpl(bus, service);
        endpoint.getInInterceptors()
                .add(new ApiKeyInterceptor(apiKey));
        endpoint.publish(path);
        return endpoint;
    }

}
