package ar.edu.ubp.das.mascotasrest.repositories;

import ar.edu.ubp.das.mascotasrest.BE.EspecieBE;
import ar.edu.ubp.das.mascotasrest.BE.EstadoPublicacionBE;
import ar.edu.ubp.das.mascotasrest.BE.PublicacionBE;
import ar.edu.ubp.das.mascotasrest.BE.PublicacionDataBE;
import ar.edu.ubp.das.mascotasrest.BE.UsuarioBE;
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
public class PublicacionesRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<EspecieBE> getEspecies() {
        return jdbcTemplate.query(
            "exec dbo.get_especies",
            new BeanPropertyRowMapper<>(EspecieBE.class)
        );
    }

    public List<PublicacionBE> getPublicaciones(UsuarioBE usuario) {
        return jdbcTemplate.query(
            "exec dbo.get_publicaciones ?",
            new BeanPropertyRowMapper<>(PublicacionBE.class),
            usuario.getIdRefugio()
        );
    }

    public int insertarPublicacion(PublicacionDataBE publicacion) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
            .withProcedureName("ins_publicacion")
            .declareParameters(
                new SqlParameter("id_refugio", Types.INTEGER),
                new SqlParameter("nombre", Types.VARCHAR),
                new SqlParameter("cod_especie", Types.SMALLINT),
                new SqlParameter("sexo", Types.VARCHAR),
                new SqlParameter("anio_nacimiento", Types.SMALLINT),
                new SqlParameter("raza", Types.VARCHAR),
                new SqlParameter("caracteristicas_mascota", Types.VARCHAR),
                new SqlParameter("condicion_adopcion", Types.VARCHAR),
                new SqlParameter("foto", Types.VARCHAR),
                new SqlOutParameter("nro_publicacion", Types.INTEGER)
            );

        Map<String, Object> params = new HashMap<>();

        params.put("id_refugio", publicacion.getIdRefugio());
        params.put("nombre", publicacion.getNombre());
        params.put("cod_especie", publicacion.getCodEspecie());
        params.put("sexo", publicacion.getSexo());
        params.put("anio_nacimiento", publicacion.getAnioNacimiento());
        params.put("raza", publicacion.getRaza());
        params.put("caracteristicas_mascota", publicacion.getCaracteristicasMascota());
        params.put("condicion_adopcion", publicacion.getCondicionAdopcion());
        params.put("foto", publicacion.getFoto());

        Map<String, Object> result = jdbcCall.execute(params);

        return (int) result.get("nro_publicacion");
    }

    public void actPublicacion(EstadoPublicacionBE estado) {
        jdbcTemplate.update(
            "exec dbo.act_publicacion ?, ?, ?",
            estado.getNroPublicacion(),
            estado.getIdRefugio(),
            estado.getEstadoPublicacion()
        );
    }

}
