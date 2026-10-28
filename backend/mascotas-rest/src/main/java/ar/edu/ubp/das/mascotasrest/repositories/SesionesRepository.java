package ar.edu.ubp.das.mascotasrest.repositories;

import ar.edu.ubp.das.mascotasrest.BE.UsuarioBE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SesionesRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<UsuarioBE> getUsuarioRefugio(String cuil, String clave) {
        return jdbcTemplate.query(
            "exec dbo.get_usuario_refugio ?, ?",
            new BeanPropertyRowMapper<>(UsuarioBE.class),
            cuil,
            clave
        );
    }

}
