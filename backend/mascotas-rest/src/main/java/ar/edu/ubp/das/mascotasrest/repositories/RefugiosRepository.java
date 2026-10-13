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

    public int insertarRefugio(RefugioDataBE refugio, int idResponsable) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
            .withProcedureName("ins_refugio")
            .declareParameters(
                new SqlParameter("razon_social", Types.VARCHAR),
                new SqlParameter("correo", Types.VARCHAR),
                new SqlParameter("telefono", Types.VARCHAR),
                new SqlParameter("domicilio", Types.VARCHAR),
                new SqlParameter("habilitacion_municipal", Types.VARCHAR),
                new SqlParameter("id_responsable", Types.INTEGER),
                new SqlOutParameter("id_refugio", Types.INTEGER)
            );

        Map<String, Object> params = new HashMap<>();

        params.put("razon_social", refugio.getRazonSocial());
        params.put("correo", refugio.getCorreo());
        params.put("telefono", refugio.getTelefono());
        params.put("domicilio", refugio.getDomicilio());
        params.put("habilitacion_municipal", refugio.getHabilitacionMunicipal());
        params.put("id_responsable", idResponsable);

        Map<String, Object> result = jdbcCall.execute(params);

        return (int) result.get("id_refugio");
    }

}
