use mascotas_cba
go

drop table if exists dbo.publicaciones_adopcion
drop table if exists dbo.caracteristicas_mascotas
drop table if exists dbo.dominio_rasgos_mascotas
drop table if exists dbo.rasgos_mascotas
drop table if exists dbo.informacion_sanitaria
drop table if exists dbo.tipos_atencion_sanitaria
drop table if exists dbo.mascotas
drop table if exists dbo.refugios
drop table if exists dbo.profesionales_veterinarias
drop table if exists dbo.ciudadanos
drop table if exists dbo.configuracion_veterinatarias
drop table if exists dbo.veterinarias
drop table if exists dbo.atributos_sistema
go

/* ------------------------------------------
   Tabla: atributos_sistema
   ------------------------------------------ */
create table dbo.atributos_sistema
(
 cod_atributo			varchar(20)		not null
						constraint PK__atributos_sistema__END primary key,
 desc_atributo			varchar(255)	not null,
 tipo_dato				varchar(20)		not null,
 observ_atributo		varchar(255)	null
)
go

insert into dbo.atributos_sistema(cod_atributo, desc_atributo, tipo_dato, observ_atributo)
values('URL', 'URL del servicio', 'varchar', null),
      ('TECNOLOGIA', 'Tecnología servicio', 'varchar', 'REST / SOAP'),
      ('PARAMETROS', 'Parametros servicio', 'varchar', null),
      ('API_KEY', 'Api Key', 'varchar', null)
go

/* ------------------------------------------
   Tabla: veterinarias
   ------------------------------------------ */
create table dbo.veterinarias
(
 id_veterinaria			integer			not null identity
						constraint PK__veterinarias__END primary key,
 razon_social			varchar(255)	not null,
 correo					varchar(255)	not null,
 telefono				varchar(20)		null,
 domicilio				varchar(255)	null,
 habilitacion_municipal	varchar(20)		not null
)
go

/* ------------------------------------------
   Tabla: configuracion_veterinatarias
   ------------------------------------------ */
create table dbo.configuracion_veterinatarias
(
 id_veterinaria			integer			not null
						constraint FK__configuracion_veterinatarias__veterinarias__END references dbo.veterinarias
						on delete cascade
						on update cascade,
 cod_atributo			varchar(20)		not null
						constraint FK__configuracion_veterinatarias__atributos_sistema__END references dbo.atributos_sistema,
 valor					varchar(4000)	not null,
 constraint PK__configuracion_veterinatarias__END
			primary key (id_veterinaria, cod_atributo)
)
go

/* ------------------------------------------
   Tabla: ciudadanos
   ------------------------------------------ */
create table dbo.ciudadanos
(
 id_ciudadano			integer			not null identity
						constraint PK__ciudadanos__END primary key,
 apellido				varchar(40)		not null,
 nombre					varchar(100)	not null,
 cuil					varchar(11)		not null
						constraint UK__ciudadanos__cuil__END unique,
 clave					varchar(20)		not null,
 correo					varchar(255)	not null,
 telefono				varchar(20)		null,
 domicilio				varchar(255)	null,
 habilitado				bit				not null
)
go

/* ------------------------------------------
   Tabla: profesionales_veterinarias
   ------------------------------------------ */
create table dbo.profesionales_veterinarias
(
 id_veterinaria			integer			not null
						constraint FK__profesionales_veterinarias__veterinarias__END references dbo.veterinarias
						on delete cascade
						on update cascade,
 id_profesional			integer			not null
						constraint FK__profesionales_veterinarias__ciudadanos__END references dbo.ciudadanos,
 baja					bit				not null,
 constraint PK__profesionales_veterinarias__END
			primary key (id_veterinaria, id_profesional)
)
go

/* ------------------------------------------
   Tabla: refugios
   ------------------------------------------ */
create table dbo.refugios
(
 id_refugio				integer			not null identity
						constraint PK__refugios__END primary key,
 razon_social			varchar(255)	not null,
 correo					varchar(255)	not null,
 telefono				varchar(20)		null,
 domicilio				varchar(255)	null,
 habilitacion_municipal	varchar(20)		not null,
 id_responsable			integer			not null
						constraint FK__refugios__ciudadanos__END references dbo.ciudadanos
)
go

/* ------------------------------------------
   Tabla: mascotas
   ------------------------------------------ */
create table dbo.mascotas
(
 nro_reg_municipal		integer			not null identity
						constraint PK__mascotas__END primary key,
 nombre					varchar(100)	not null,
 sexo					varchar(1)		not null,
 año_nacimiento			smallint		null,
 microchip				varchar(20)		null,
 vive					bit				not null,
 id_responsable			integer			null
						constraint FK__mascotas__ciudadanos__END references dbo.ciudadanos,
 id_refugio				integer			null
						constraint FK__mascotas__refugios__END references dbo.refugios
)
go

/* ------------------------------------------
   Tabla: tipos_atencion_sanitaria
   ------------------------------------------ */
create table dbo.tipos_atencion_sanitaria
(
 cod_tipo_atencion		varchar(3)		not null
						constraint PK__tipos_atencion_sanitaria__END primary key,
 desc_tipo_atencion		varchar(50)		not null
)
go

insert into dbo.tipos_atencion_sanitaria(cod_tipo_atencion, desc_tipo_atencion)
values('VAC', 'Vacunación'),
      ('DES', 'Desparasitación'),
      ('EST', 'Esterilización'),
      ('CAS', 'Castración')
go

/* ------------------------------------------
   Tabla: informacion_sanitaria
   ------------------------------------------ */
create table dbo.informacion_sanitaria
(
 nro_reg_municipal		integer			not null
						constraint FK__informacion_sanitaria__mascotas__END references dbo.mascotas
						on delete cascade
						on update cascade,
 nro_registro			integer			not null,
 fecha_atencion			date			not null,
 cod_tipo_atencion		varchar(3)		not null
						constraint FK__informacion_sanitaria__tipos_atencion_sanitaria__END references dbo.tipos_atencion_sanitaria,
 detalle_atencion		varchar(4000)	null,
 fecha_vencimiento		date			null,
 id_veterinaria			integer			not null,
 id_profesional			integer			not null,
 constraint FK__informacion_sanitaria__profesionales_veterinarias__END
			foreign key (id_veterinaria, id_profesional) references dbo.profesionales_veterinarias,
 constraint PK__informacion_sanitaria__END
			primary key (nro_reg_municipal, nro_registro)
)
go

/* ------------------------------------------
   Tabla: rasgos_mascotas
   ------------------------------------------ */
create table dbo.rasgos_mascotas
(
 cod_rasgo				varchar(3)		not null
						constraint PK__rasgos_mascotas__END primary key,
 nom_rasgo				varchar(50)		not null
)
go

insert into dbo.rasgos_mascotas(cod_rasgo, nom_rasgo)
values('ESP', 'Especie'),
      ('RAZ', 'Raza'),
      ('PEL', 'Pelaje')
go

/* ------------------------------------------
   Tabla: dominio_rasgos_mascotas
   ------------------------------------------ */
create table dbo.dominio_rasgos_mascotas
(
 cod_rasgo				varchar(3)		not null
						constraint FK__dominio_rasgos_mascotas__rasgos_mascotas__END references dbo.rasgos_mascotas
						on delete cascade
						on update cascade,
 nro_valor_dominio		smallint		not null,
 nom_valor_dominio		varchar(100)	not null,
 constraint PK__dominio_rasgos_mascotas__END
			primary key (cod_rasgo, nro_valor_dominio)
)
go

/* ------------------------------------------
   Tabla: caracteristicas_mascotas
   ------------------------------------------ */
create table dbo.caracteristicas_mascotas
(
 nro_reg_municipal		integer			not null
						constraint FK__caracteristicas_mascotas__mascotas__END references dbo.mascotas
						on delete cascade
						on update cascade,
 cod_rasgo				varchar(3)		not null
						constraint FK__caracteristicas_mascotas__rasgos_mascotas__END references dbo.rasgos_mascotas,
 nro_caracteristica		smallint		not null,
 nro_valor_dominio		smallint		null,
 valor_caracteristica	varchar(255)	null,
 constraint FK__caracteristicas_mascotas__dominio_rasgos_mascotas__END
			foreign key (cod_rasgo, nro_valor_dominio) references dbo.dominio_rasgos_mascotas,
 constraint PK__caracteristicas_mascotas__END
			primary key (nro_reg_municipal, cod_rasgo, nro_caracteristica)
)
go

/* ------------------------------------------
   Tabla: publicaciones_adopcion
   ------------------------------------------ */
create table dbo.publicaciones_adopcion
(
 nro_publicacion		integer			not null identity
						constraint PK__publicaciones_adopcion__END primary key,
 nro_reg_municipal		integer			not null
						constraint FK__publicaciones_adopcion__mascotas__END references dbo.mascotas,
 id_refugio				integer			not null
						constraint FK__publicaciones_adopcion__refugios__END references dbo.refugios,
 fecha_publicacion		date			not null,
 caracteristicas_mascota	varchar(4000)	null,
 condicion_adopcion		varchar(4000)	null,
 foto					varchar(max)	null,
 estado_publicacion		varchar(1)		not null	-- A: Activa, P: Pausada, F: Finalizada
)
go

/* ------------------------------------------
   Procedimiento: get_ciudadano
   ------------------------------------------ */
create or alter procedure dbo.get_ciudadano
(
 @cuil			varchar(11)
)
as
begin

  select idCiudadano = id_ciudadano,
         apellido    = apellido,
         nombre      = nombre,
         cuil        = cuil,
         correo      = correo
    from dbo.ciudadanos (nolock)
   where cuil = @cuil

end
go

-- execute dbo.get_ciudadano @cuil='20123456789'

/* ------------------------------------------
   Procedimiento: ins_refugio
   ------------------------------------------ */
create or alter procedure dbo.ins_refugio
(
 @razon_social				varchar(255),
 @correo					varchar(255),
 @telefono					varchar(20)		= null,
 @domicilio					varchar(255)	= null,
 @habilitacion_municipal	varchar(20),
 @apellido					varchar(40),
 @nombre					varchar(100),
 @cuil						varchar(11),
 @clave						varchar(20),
 @correo_responsable		varchar(255),
 @telefono_responsable		varchar(20)		= null,
 @domicilio_responsable		varchar(255)	= null,
 @id_refugio				integer			output
)
as
begin

  set nocount on

  declare @id_responsable integer

  if trim(@telefono) = ''
     set @telefono = null
  if trim(@domicilio) = ''
     set @domicilio = null
  if trim(@telefono_responsable) = ''
     set @telefono_responsable = null
  if trim(@domicilio_responsable) = ''
     set @domicilio_responsable = null

  insert into dbo.ciudadanos(apellido, nombre, cuil, clave, correo, telefono, domicilio, habilitado)
  values(upper(trim(@apellido)),
         upper(trim(@nombre)),
         trim(@cuil),
         @clave,
         trim(@correo_responsable),
         trim(@telefono_responsable),
         upper(trim(@domicilio_responsable)),
         1)

  set @id_responsable = @@identity

  insert into dbo.refugios(razon_social, correo, telefono, domicilio, habilitacion_municipal, id_responsable)
  values(upper(trim(@razon_social)),
         trim(@correo),
         trim(@telefono),
         upper(trim(@domicilio)),
         upper(trim(@habilitacion_municipal)),
         @id_responsable)

  set @id_refugio = @@identity

  set nocount off

end
go

-- declare @id integer execute dbo.ins_refugio @razon_social='REFUGIO PATITAS', @correo='patitas@refugio.org', @habilitacion_municipal='HM-0002', @apellido='GOMEZ', @nombre='ANA', @cuil='27234567894', @clave='clave123', @correo_responsable='ana@mail.com', @id_refugio=@id output select @id

/* ------------------------------------------
   Procedimiento: get_usuario_refugio
   ------------------------------------------ */
create or alter procedure dbo.get_usuario_refugio
(
 @cuil			varchar(11),
 @clave			varchar(20)
)
as
begin

  select idCiudadano = c.id_ciudadano,
         apellido    = c.apellido,
         nombre      = c.nombre,
         cuil        = c.cuil,
         correo      = c.correo,
         idRefugio   = r.id_refugio,
         razonSocial = r.razon_social
    from dbo.ciudadanos c (nolock)
         join dbo.refugios r (nolock)
           on c.id_ciudadano = r.id_responsable
   where c.cuil       = @cuil
     and c.clave      = @clave
     and c.habilitado = 1
   order by r.id_refugio

end
go

-- execute dbo.get_usuario_refugio @cuil='20123456789', @clave='123456'

/* ------------------------------------------
   Datos iniciales: usuario de refugio
   ------------------------------------------ */
insert into dbo.ciudadanos(apellido, nombre, cuil, clave, correo, telefono, domicilio, habilitado)
values('PEREZ', 'JUAN', '20123456789', '123456', 'juan.perez@ubp.edu.ar', '3515551234', 'AV. DONATO ALVAREZ 380', 1)
go

insert into dbo.refugios(razon_social, correo, telefono, domicilio, habilitacion_municipal, id_responsable)
values('REFUGIO HUELLITAS', 'huellitas@refugio.org', '3515554321', 'CAMINO A SAN CARLOS KM 5', 'HM-0001', 1)
go
