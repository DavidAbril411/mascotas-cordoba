package ar.edu.ubp.das.mascotasrest.repositories;

import ar.edu.ubp.das.mascotasrest.BE.TipoAtencionBE;
import ar.edu.ubp.das.mascotasrest.BE.VeterinariaBE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class VeterinariasRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<VeterinariaBE> getVeterinarias() {
        return jdbcTemplate.query(
            "exec dbo.get_veterinarias",
            new BeanPropertyRowMapper<>(VeterinariaBE.class)
        );
    }

    public List<TipoAtencionBE> getServicios() {
        return jdbcTemplate.query(
            "exec dbo.get_tipos_atencion",
            new BeanPropertyRowMapper<>(TipoAtencionBE.class)
        );
    }

}
