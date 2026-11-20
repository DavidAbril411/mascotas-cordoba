package ar.edu.ubp.das.veterinariarest.repositories;

import ar.edu.ubp.das.veterinariarest.BE.AtencionBE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AtencionesRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<AtencionBE> getAtenciones() {
        return jdbcTemplate.query(
            "exec dbo.get_atenciones",
            new BeanPropertyRowMapper<>(AtencionBE.class)
        );
    }

}
