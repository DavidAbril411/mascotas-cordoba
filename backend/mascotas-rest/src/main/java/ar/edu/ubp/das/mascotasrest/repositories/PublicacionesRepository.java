package ar.edu.ubp.das.mascotasrest.repositories;

import ar.edu.ubp.das.mascotasrest.BE.EspecieBE;
import ar.edu.ubp.das.mascotasrest.BE.PublicacionBE;
import ar.edu.ubp.das.mascotasrest.BE.PublicacionDataBE;
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

    public List<PublicacionBE> getPublicaciones(int idRefugio) {
        return jdbcTemplate.query(
            "exec dbo.get_publicaciones ?",
            new BeanPropertyRowMapper<>(PublicacionBE.class),
            idRefugio
        );
    }

    public int insertarPublicacion(int idRefugio, PublicacionDataBE publicacion) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
            .withProcedureName("ins_publicacion")
            .declareParameters(
                new SqlParameter("id_refugio", Types.INTEGER),
                new SqlParameter("nombre", Types.VARCHAR),
                new SqlParameter("cod_especie", Types.SMALLINT),
                new SqlParameter("sexo", Types.VARCHAR),
                new SqlParameter("edad", Types.SMALLINT),
                new SqlParameter("raza", Types.VARCHAR),
                new SqlParameter("caracteristicas_mascota", Types.VARCHAR),
                new SqlParameter("condicion_adopcion", Types.VARCHAR),
                new SqlParameter("foto", Types.VARCHAR),
                new SqlOutParameter("nro_publicacion", Types.INTEGER)
            );

        Map<String, Object> params = new HashMap<>();

        params.put("id_refugio", idRefugio);
        params.put("nombre", publicacion.getNombre());
        params.put("cod_especie", publicacion.getCodEspecie());
        params.put("sexo", publicacion.getSexo());
        params.put("edad", publicacion.getEdad());
        params.put("raza", publicacion.getRaza());
        params.put("caracteristicas_mascota", publicacion.getCaracteristicasMascota());
        params.put("condicion_adopcion", publicacion.getCondicionAdopcion());
        params.put("foto", publicacion.getFoto());

        Map<String, Object> result = jdbcCall.execute(params);

        return (int) result.get("nro_publicacion");
    }

    public void actPublicacion(int idRefugio, int nroPublicacion, String estadoPublicacion) {
        jdbcTemplate.update(
            "exec dbo.act_publicacion ?, ?, ?",
            nroPublicacion,
            idRefugio,
            estadoPublicacion
        );
    }

}
