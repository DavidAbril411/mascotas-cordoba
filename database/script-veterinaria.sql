/* ------------------------------------------
   Sistema de Gestión Veterinaria
   Modelo lógico de la veterinaria (02-Modelo-logico-veterinaria).
   Cada veterinaria tiene su propia base: ejecutar este script en
   veterinaria_1, veterinaria_2, veterinaria_3 y veterinaria_4
   (cambiar el use de la primera línea) y después el
   datos-veterinaria-N.sql de cada una.
   ------------------------------------------ */
use veterinaria_1
go

drop table if exists dbo.detalle_atencion_sanitaria
drop table if exists dbo.atencion_sanitaria
drop table if exists dbo.vacunas
drop table if exists dbo.tipos_atencion_sanitaria
drop table if exists dbo.profesionales
drop table if exists dbo.responsables_mascotas
drop table if exists dbo.mascotas
drop table if exists dbo.razas
drop table if exists dbo.especies
drop table if exists dbo.personas
drop table if exists dbo.configuracion_sistema
go

/* ------------------------------------------
   Tabla: personas
   ------------------------------------------ */
create table dbo.personas
(
 id_persona				integer			not null identity
						constraint PK__personas__END primary key,
 apellido				varchar(40)		not null,
 nombre					varchar(100)	not null,
 tipo_documento			varchar(4)		not null,
 nro_documento			varchar(11)		not null,
 correo					varchar(255)	null,
 telefono				varchar(20)		null,
 domicilio				varchar(255)	null,
 constraint UK__personas__documento__END
			unique (tipo_documento, nro_documento)
)
go

/* ------------------------------------------
   Tabla: especies
   ------------------------------------------ */
create table dbo.especies
(
 cod_especie			varchar(3)		not null
						constraint PK__especies__END primary key,
 desc_especie			varchar(50)		not null
)
go

insert into dbo.especies(cod_especie, desc_especie)
values('PER', 'Perro'),
      ('GAT', 'Gato'),
      ('CON', 'Conejo')
go

/* ------------------------------------------
   Tabla: razas
   ------------------------------------------ */
create table dbo.razas
(
 cod_especie			varchar(3)		not null
						constraint FK__razas__especies__END references dbo.especies
						on delete cascade
						on update cascade,
 id_raza				smallint		not null,
 nom_raza				varchar(100)	not null,
 constraint PK__razas__END
			primary key (cod_especie, id_raza)
)
go

insert into dbo.razas(cod_especie, id_raza, nom_raza)
values('PER', 1, 'Mestizo'),
      ('PER', 2, 'Labrador'),
      ('PER', 3, 'Border Collie'),
      ('GAT', 1, 'Mestizo'),
      ('GAT', 2, 'Siamés'),
      ('CON', 1, 'Mestizo')
go

/* ------------------------------------------
   Tabla: mascotas
   ------------------------------------------ */
create table dbo.mascotas
(
 id_mascota				integer			not null identity
						constraint PK__mascotas__END primary key,
 nombre					varchar(100)	not null,
 cod_especie			varchar(3)		not null
						constraint FK__mascotas__especies__END references dbo.especies,
 id_raza				smallint		null,
 sexo					varchar(1)		not null,
 fecha_nacimiento		date			null,
 color					varchar(50)		null,
 pelaje					varchar(50)		null,
 microchip				varchar(20)		null,
 vive					bit				not null,
 nro_reg_municipal		integer			null,
 constraint FK__mascotas__razas__END
			foreign key (cod_especie, id_raza) references dbo.razas
)
go

/* ------------------------------------------
   Tabla: responsables_mascotas
   ------------------------------------------ */
create table dbo.responsables_mascotas
(
 id_mascota				integer			not null
						constraint FK__responsables_mascotas__mascotas__END references dbo.mascotas
						on delete cascade
						on update cascade,
 id_persona				integer			not null
						constraint FK__responsables_mascotas__personas__END references dbo.personas,
 fecha_desde			date			not null,
 fecha_hasta			date			null,
 observaciones			varchar(255)	null,
 principal				bit				not null,
 constraint PK__responsables_mascotas__END
			primary key (id_mascota, id_persona)
)
go

/* ------------------------------------------
   Tabla: profesionales
   ------------------------------------------ */
create table dbo.profesionales
(
 id_persona				integer			not null
						constraint PK__profesionales__END primary key
						constraint FK__profesionales__personas__END references dbo.personas,
 matricula_profesional	varchar(20)		not null,
 fecha_inicio			date			not null,
 fecha_fin				date			null
)
go

/* ------------------------------------------
   Tabla: tipos_atencion_sanitaria
   (los códigos VAC, DES, EST y CAS son los del programa municipal)
   ------------------------------------------ */
create table dbo.tipos_atencion_sanitaria
(
 cod_tipo_atencion		varchar(3)		not null
						constraint PK__tipos_atencion_sanitaria__END primary key,
 desc_tipo_atencion		varchar(50)		not null
)
go

insert into dbo.tipos_atencion_sanitaria(cod_tipo_atencion, desc_tipo_atencion)
values('CON', 'Consulta clínica'),
      ('CTL', 'Control / seguimiento'),
      ('VAC', 'Vacunación'),
      ('DES', 'Desparasitación'),
      ('EST', 'Esterilización'),
      ('CAS', 'Castración'),
      ('CIR', 'Cirugía')
go

/* ------------------------------------------
   Tabla: vacunas
   ------------------------------------------ */
create table dbo.vacunas
(
 cod_vacuna				varchar(5)		not null
						constraint PK__vacunas__END primary key,
 nom_vacuna				varchar(50)		not null,
 desc_vacuna			varchar(255)	null
)
go

insert into dbo.vacunas(cod_vacuna, nom_vacuna, desc_vacuna)
values('ANT', 'Antirrábica', 'Vacuna contra la rabia'),
      ('SEX', 'Séxtuple', 'Moquillo, hepatitis, parvovirus, parainfluenza, leptospirosis y coronavirus'),
      ('TRI', 'Triple felina', 'Panleucopenia, rinotraqueitis y calicivirus'),
      ('LEU', 'Leucemia felina', null)
go

/* ------------------------------------------
   Tabla: atencion_sanitaria
   ------------------------------------------ */
create table dbo.atencion_sanitaria
(
 id_mascota				integer			not null
						constraint FK__atencion_sanitaria__mascotas__END references dbo.mascotas
						on delete cascade
						on update cascade,
 nro_atencion			smallint		not null,
 id_profesional			integer			not null
						constraint FK__atencion_sanitaria__profesionales__END references dbo.profesionales,
 fecha_atencion			date			not null,
 fecha_prox_atencion	date			null,
 observ_atencion		varchar(4000)	null,
 constraint PK__atencion_sanitaria__END
			primary key (id_mascota, nro_atencion)
)
go

/* ------------------------------------------
   Tabla: detalle_atencion_sanitaria
   ------------------------------------------ */
create table dbo.detalle_atencion_sanitaria
(
 id_mascota				integer			not null,
 nro_atencion			smallint		not null,
 cod_tipo_atencion		varchar(3)		not null
						constraint FK__detalle_atencion_sanitaria__tipos_atencion_sanitaria__END references dbo.tipos_atencion_sanitaria,
 nro_detalle			smallint		not null,
 cod_vacuna				varchar(5)		null
						constraint FK__detalle_atencion_sanitaria__vacunas__END references dbo.vacunas,
 lote_vacuna			varchar(20)		null,
 fecha_prox_vacunacion	date			null,
 indicaciones			varchar(4000)	null,
 intervencion			varchar(4000)	null,
 constraint FK__detalle_atencion_sanitaria__atencion_sanitaria__END
			foreign key (id_mascota, nro_atencion) references dbo.atencion_sanitaria
			on delete cascade
			on update cascade,
 constraint PK__detalle_atencion_sanitaria__END
			primary key (id_mascota, nro_atencion, cod_tipo_atencion, nro_detalle)
)
go

/* ------------------------------------------
   Tabla: configuracion_sistema
   ------------------------------------------ */
create table dbo.configuracion_sistema
(
 clave_primaria			varchar(20)		not null,
 clave_secundaria		varchar(20)		not null,
 desc_clave				varchar(255)	not null,
 tipo_dato				varchar(20)		not null,
 valor					varchar(4000)	null,
 constraint PK__configuracion_sistema__END
			primary key (clave_primaria, clave_secundaria)
)
go

/* ------------------------------------------
   Procedimiento: get_mascotas_pendientes
   Mascotas que todavía no tienen número de registro municipal (NRM),
   con los datos de su responsable principal en la misma fila.
   ------------------------------------------ */
create or alter procedure dbo.get_mascotas_pendientes
as
begin

  select idInterno            = convert(varchar(20), m.id_mascota),
         nombre               = m.nombre,
         especie              = e.desc_especie,
         raza                 = r.nom_raza,
         sexo                 = m.sexo,
         fechaNacimiento      = convert(varchar(10), m.fecha_nacimiento, 23),
         pelaje               = concat_ws(', ', m.color, m.pelaje),
         microchip            = m.microchip,
         vive                 = m.vive,
         cuilResponsable      = p.nro_documento,
         apellidoResponsable  = p.apellido,
         nombreResponsable    = p.nombre,
         correoResponsable    = p.correo,
         telefonoResponsable  = p.telefono,
         domicilioResponsable = p.domicilio
    from dbo.mascotas m (nolock)
         join dbo.especies e (nolock)
           on m.cod_especie = e.cod_especie
         left join dbo.razas r (nolock)
           on m.cod_especie = r.cod_especie
          and m.id_raza     = r.id_raza
         left join dbo.responsables_mascotas rm (nolock)
           on m.id_mascota  = rm.id_mascota
          and rm.principal  = 1
          and rm.fecha_hasta is null
         left join dbo.personas p (nolock)
           on rm.id_persona = p.id_persona
   where m.nro_reg_municipal is null
   order by m.id_mascota

end
go

-- execute dbo.get_mascotas_pendientes

/* ------------------------------------------
   Procedimiento: act_nro_reg_municipal
   Guarda el NRM que informó la Municipalidad.
   ------------------------------------------ */
create or alter procedure dbo.act_nro_reg_municipal
(
 @id_mascota			integer,
 @nro_reg_municipal		integer,
 @actualizadas			integer		output
)
as
begin

  set nocount on

  update dbo.mascotas
     set nro_reg_municipal = @nro_reg_municipal
   where id_mascota = @id_mascota

  set @actualizadas = @@rowcount

  set nocount off

end
go

-- declare @n integer execute dbo.act_nro_reg_municipal @id_mascota=1, @nro_reg_municipal=1, @actualizadas=@n output select @n

/* ------------------------------------------
   Procedimiento: get_atenciones
   Atenciones de las mascotas que ya tienen NRM, "planchadas":
   una fila por atención y tipo, con todas las vacunas o
   intervenciones de ese tipo juntas en el detalle.
   ------------------------------------------ */
create or alter procedure dbo.get_atenciones
as
begin

  select nroRegMunicipal      = m.nro_reg_municipal,
         vive                 = m.vive,
         fechaAtencion        = convert(varchar(10), a.fecha_atencion, 23),
         codTipoAtencion      = d.cod_tipo_atencion,
         detalle              = concat_ws('. ',
                                          string_agg(nullif(concat_ws(' - ',
                                                                      v.nom_vacuna + isnull(' (lote ' + d.lote_vacuna + ')', ''),
                                                                      d.intervencion,
                                                                      d.indicaciones), ''), ' · ') within group (order by d.nro_detalle),
                                          a.observ_atencion),
         fechaProxima         = convert(varchar(10), isnull(min(d.fecha_prox_vacunacion), a.fecha_prox_atencion), 23),
         cuilProfesional      = p.nro_documento,
         apellidoProfesional  = p.apellido,
         nombreProfesional    = p.nombre,
         correoProfesional    = p.correo,
         matriculaProfesional = f.matricula_profesional
    from dbo.atencion_sanitaria a (nolock)
         join dbo.mascotas m (nolock)
           on a.id_mascota = m.id_mascota
         join dbo.detalle_atencion_sanitaria d (nolock)
           on a.id_mascota   = d.id_mascota
          and a.nro_atencion = d.nro_atencion
         left join dbo.vacunas v (nolock)
           on d.cod_vacuna = v.cod_vacuna
         join dbo.profesionales f (nolock)
           on a.id_profesional = f.id_persona
         join dbo.personas p (nolock)
           on f.id_persona = p.id_persona
   where m.nro_reg_municipal is not null
   group by m.nro_reg_municipal, m.vive, a.id_mascota, a.nro_atencion, a.fecha_atencion, a.fecha_prox_atencion,
            a.observ_atencion, d.cod_tipo_atencion, p.nro_documento, p.apellido, p.nombre, p.correo, f.matricula_profesional
   order by a.fecha_atencion, m.nro_reg_municipal, d.cod_tipo_atencion

end
go

-- execute dbo.get_atenciones
