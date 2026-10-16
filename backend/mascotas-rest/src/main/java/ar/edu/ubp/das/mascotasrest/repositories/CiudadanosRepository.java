package ar.edu.ubp.das.mascotasrest.repositories;

import ar.edu.ubp.das.mascotasrest.BE.CiudadanoBE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

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

}
