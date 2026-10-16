package ar.edu.ubp.das.mascotasrest.repositories;

import ar.edu.ubp.das.mascotasrest.BE.RefugioDataBE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

@Repository
public class RefugiosRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int insertarRefugio(RefugioDataBE refugio) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
            .withProcedureName("ins_refugio")
            .declareParameters(
                new SqlParameter("razon_social", Types.VARCHAR),
                new SqlParameter("correo", Types.VARCHAR),
                new SqlParameter("telefono", Types.VARCHAR),
                new SqlParameter("domicilio", Types.VARCHAR),
                new SqlParameter("habilitacion_municipal", Types.VARCHAR),
                new SqlParameter("apellido", Types.VARCHAR),
                new SqlParameter("nombre", Types.VARCHAR),
                new SqlParameter("cuil", Types.VARCHAR),
                new SqlParameter("clave", Types.VARCHAR),
                new SqlParameter("correo_responsable", Types.VARCHAR),
                new SqlParameter("telefono_responsable", Types.VARCHAR),
                new SqlParameter("domicilio_responsable", Types.VARCHAR),
                new SqlOutParameter("id_refugio", Types.INTEGER)
            );

        Map<String, Object> params = new HashMap<>();

        params.put("razon_social", refugio.getRazonSocial());
        params.put("correo", refugio.getCorreo());
        params.put("telefono", refugio.getTelefono());
        params.put("domicilio", refugio.getDomicilio());
        params.put("habilitacion_municipal", refugio.getHabilitacionMunicipal());
        params.put("apellido", refugio.getResponsable().getApellido());
        params.put("nombre", refugio.getResponsable().getNombre());
        params.put("cuil", refugio.getResponsable().getCuil());
        params.put("clave", refugio.getResponsable().getClave());
        params.put("correo_responsable", refugio.getResponsable().getCorreo());
        params.put("telefono_responsable", refugio.getResponsable().getTelefono());
        params.put("domicilio_responsable", refugio.getResponsable().getDomicilio());

        Map<String, Object> result = jdbcCall.execute(params);

        return (int) result.get("id_refugio");
    }

}
