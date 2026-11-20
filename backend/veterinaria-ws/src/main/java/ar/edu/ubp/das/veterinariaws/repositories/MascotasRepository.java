package ar.edu.ubp.das.veterinariaws.repositories;

import ar.edu.ubp.das.veterinariaws.BE.MascotaPendienteBE;
import ar.edu.ubp.das.veterinariaws.BE.NroRegMunicipalBE;
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
public class MascotasRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<MascotaPendienteBE> getMascotasPendientes() {
        return jdbcTemplate.query(
            "exec dbo.get_mascotas_pendientes",
            new BeanPropertyRowMapper<>(MascotaPendienteBE.class)
        );
    }

    public int actNroRegMunicipal(NroRegMunicipalBE nroRegMunicipal) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
            .withProcedureName("act_nro_reg_municipal")
            .declareParameters(
                new SqlParameter("id_mascota", Types.INTEGER),
                new SqlParameter("nro_reg_municipal", Types.INTEGER),
                new SqlOutParameter("actualizadas", Types.INTEGER)
            );

        Map<String, Object> params = new HashMap<>();

        params.put("id_mascota", Integer.parseInt(nroRegMunicipal.getIdInterno()));
        params.put("nro_reg_municipal", nroRegMunicipal.getNroRegMunicipal());

        Map<String, Object> result = jdbcCall.execute(params);

        return (int) result.get("actualizadas");
    }

}
