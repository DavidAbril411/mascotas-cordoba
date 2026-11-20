package ar.edu.ubp.das.mascotasrest.veterinarias.repositories;

import ar.edu.ubp.das.mascotasrest.veterinarias.BE.AtencionVeterinariaBE;
import ar.edu.ubp.das.mascotasrest.veterinarias.BE.ConfiguracionVeterinariaBE;
import ar.edu.ubp.das.mascotasrest.veterinarias.BE.LogIntegracionBE;
import ar.edu.ubp.das.mascotasrest.veterinarias.BE.MascotaVeterinariaBE;
import ar.edu.ubp.das.mascotasrest.veterinarias.BE.TipoAtencionBE;
import ar.edu.ubp.das.mascotasrest.veterinarias.BE.VeterinariaBE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public List<ConfiguracionVeterinariaBE> getConfiguraciones() {
        return jdbcTemplate.query(
            "exec dbo.get_veterinarias_integracion",
            new BeanPropertyRowMapper<>(ConfiguracionVeterinariaBE.class)
        );
    }

    @Transactional
    public int insertarMascota(MascotaVeterinariaBE mascota) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
            .withProcedureName("ins_mascota_veterinaria")
            .declareParameters(
                new SqlParameter("nombre", Types.VARCHAR),
                new SqlParameter("especie", Types.VARCHAR),
                new SqlParameter("raza", Types.VARCHAR),
                new SqlParameter("sexo", Types.VARCHAR),
                new SqlParameter("fecha_nacimiento", Types.DATE),
                new SqlParameter("pelaje", Types.VARCHAR),
                new SqlParameter("microchip", Types.VARCHAR),
                new SqlParameter("vive", Types.BIT),
                new SqlParameter("cuil_responsable", Types.VARCHAR),
                new SqlParameter("apellido_responsable", Types.VARCHAR),
                new SqlParameter("nombre_responsable", Types.VARCHAR),
                new SqlParameter("correo_responsable", Types.VARCHAR),
                new SqlParameter("telefono_responsable", Types.VARCHAR),
                new SqlParameter("domicilio_responsable", Types.VARCHAR),
                new SqlOutParameter("nro_reg_municipal", Types.INTEGER)
            );

        Map<String, Object> params = new HashMap<>();

        params.put("nombre", mascota.getNombre());
        params.put("especie", mascota.getEspecie());
        params.put("raza", mascota.getRaza());
        params.put("sexo", mascota.getSexo());
        params.put("fecha_nacimiento", mascota.getFechaNacimiento());
        params.put("pelaje", mascota.getPelaje());
        params.put("microchip", mascota.getMicrochip());
        params.put("vive", mascota.isVive());
        params.put("cuil_responsable", mascota.getCuilResponsable());
        params.put("apellido_responsable", mascota.getApellidoResponsable());
        params.put("nombre_responsable", mascota.getNombreResponsable());
        params.put("correo_responsable", mascota.getCorreoResponsable());
        params.put("telefono_responsable", mascota.getTelefonoResponsable());
        params.put("domicilio_responsable", mascota.getDomicilioResponsable());

        Map<String, Object> result = jdbcCall.execute(params);

        return (int) result.get("nro_reg_municipal");
    }

    @Transactional
    public String insertarAtencion(AtencionVeterinariaBE atencion) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
            .withProcedureName("ins_atencion_veterinaria")
            .declareParameters(
                new SqlParameter("id_veterinaria", Types.INTEGER),
                new SqlParameter("nro_reg_municipal", Types.INTEGER),
                new SqlParameter("vive", Types.BIT),
                new SqlParameter("fecha_atencion", Types.DATE),
                new SqlParameter("cod_tipo_atencion", Types.VARCHAR),
                new SqlParameter("detalle", Types.VARCHAR),
                new SqlParameter("fecha_proxima", Types.DATE),
                new SqlParameter("cuil_profesional", Types.VARCHAR),
                new SqlParameter("apellido_profesional", Types.VARCHAR),
                new SqlParameter("nombre_profesional", Types.VARCHAR),
                new SqlParameter("correo_profesional", Types.VARCHAR),
                new SqlOutParameter("resultado", Types.VARCHAR)
            );

        Map<String, Object> params = new HashMap<>();

        params.put("id_veterinaria", atencion.getIdVeterinaria());
        params.put("nro_reg_municipal", atencion.getNroRegMunicipal());
        params.put("vive", atencion.isVive());
        params.put("fecha_atencion", atencion.getFechaAtencion());
        params.put("cod_tipo_atencion", atencion.getCodTipoAtencion());
        params.put("detalle", atencion.getDetalle());
        params.put("fecha_proxima", atencion.getFechaProxima());
        params.put("cuil_profesional", atencion.getCuilProfesional());
        params.put("apellido_profesional", atencion.getApellidoProfesional());
        params.put("nombre_profesional", atencion.getNombreProfesional());
        params.put("correo_profesional", atencion.getCorreoProfesional());

        Map<String, Object> result = jdbcCall.execute(params);

        return (String) result.get("resultado");
    }

    public void insertarLog(LogIntegracionBE log) {
        jdbcTemplate.update(
            "exec dbo.ins_log_integracion ?, ?, ?, ?, ?",
            log.getProceso(),
            log.getOrganizacion(),
            log.getReferencia(),
            log.getDetalle(),
            log.isError()
        );
    }

}
