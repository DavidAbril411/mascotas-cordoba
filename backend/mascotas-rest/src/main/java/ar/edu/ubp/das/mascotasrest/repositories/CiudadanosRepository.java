package ar.edu.ubp.das.mascotasrest.repositories;

import ar.edu.ubp.das.mascotasrest.BE.CiudadanoBE;
import ar.edu.ubp.das.mascotasrest.BE.CiudadanoDataBE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CiudadanosRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<CiudadanoBE> getCiudadano(String cuil) {
        return jdbcTemplate.query(
            "exec dbo.get_ciudadano ?",
            new BeanPropertyRowMapper<>(CiudadanoBE.class),
            cuil
        );
    }

    public int insertarCiudadano(CiudadanoDataBE ciudadano) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
            .withProcedureName("ins_ciudadano")
            .declareParameters(
                new SqlParameter("apellido", Types.VARCHAR),
                new SqlParameter("nombre", Types.VARCHAR),
                new SqlParameter("cuil", Types.VARCHAR),
                new SqlParameter("clave", Types.VARCHAR),
                new SqlParameter("correo", Types.VARCHAR),
                new SqlParameter("telefono", Types.VARCHAR),
                new SqlParameter("domicilio", Types.VARCHAR),
                new SqlOutParameter("id_ciudadano", Types.INTEGER)
            );

        Map<String, Object> params = new HashMap<>();

        params.put("apellido", ciudadano.getApellido());
        params.put("nombre", ciudadano.getNombre());
        params.put("cuil", ciudadano.getCuil());
        params.put("clave", ciudadano.getClave());
        params.put("correo", ciudadano.getCorreo());
        params.put("telefono", ciudadano.getTelefono());
        params.put("domicilio", ciudadano.getDomicilio());

        Map<String, Object> result = jdbcCall.execute(params);

        return (int) result.get("id_ciudadano");
    }

}
