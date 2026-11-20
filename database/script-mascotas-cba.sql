use mascotas_cba
go

drop procedure if exists dbo.ins_sesion
drop table if exists dbo.sesiones
drop table if exists dbo.logs_integraciones
drop table if exists dbo.horarios_refugios
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
      ('API_KEY', 'Api Key', 'varchar', null),
      ('ESTADO', 'Estado de la veterinaria', 'varchar', 'A: Activa / I: Inactiva'),
      ('HORARIOS', 'Horarios de atención', 'varchar', null),
      ('SERVICIOS', 'Servicios que presta', 'varchar', 'Códigos de tipos_atencion_sanitaria separados por coma')
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

insert into dbo.dominio_rasgos_mascotas(cod_rasgo, nro_valor_dominio, nom_valor_dominio)
values('ESP', 1, 'Perro'),
      ('ESP', 2, 'Gato')
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
   Tabla: horarios_refugios
   (agregada al modelo lógico: horarios de atención de cada refugio, RF18)
   ------------------------------------------ */
create table dbo.horarios_refugios
(
 id_refugio				integer			not null
						constraint FK__horarios_refugios__refugios__END references dbo.refugios
						on delete cascade
						on update cascade,
 nro_horario			smallint		not null,
 dias					varchar(50)		not null,
 hora_desde				time			not null,
 hora_hasta				time			not null,
 constraint PK__horarios_refugios__END
			primary key (id_refugio, nro_horario)
)
go

/* ------------------------------------------
   Tabla: logs_integraciones
   (agregada al modelo lógico: registro de lo que pasó en cada
   integración con las veterinarias, RNF06 y RNF08)
   ------------------------------------------ */
create table dbo.logs_integraciones
(
 nro_log				integer			not null identity
						constraint PK__logs_integraciones__END primary key,
 fecha					datetime		not null,
 proceso				varchar(50)		not null,
 organizacion			varchar(255)	null,
 referencia				varchar(100)	null,
 detalle				varchar(4000)	not null,
 error					bit				not null
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

  select top 1
         idCiudadano = c.id_ciudadano,
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
   Procedimiento: get_especies
   ------------------------------------------ */
create or alter procedure dbo.get_especies
as
begin

  select codigo = nro_valor_dominio,
         nombre = nom_valor_dominio
    from dbo.dominio_rasgos_mascotas (nolock)
   where cod_rasgo = 'ESP'
   order by nom_valor_dominio

end
go

-- execute dbo.get_especies

/* ------------------------------------------
   Procedimiento: get_publicaciones
   ------------------------------------------ */
create or alter procedure dbo.get_publicaciones
(
 @id_refugio		integer
)
as
begin

  select nroPublicacion         = p.nro_publicacion,
         nrm                    = 'MUN-' + right('000000' + convert(varchar(10), m.nro_reg_municipal), 6),
         nombre                 = m.nombre,
         nomEspecie             = iif(m.sexo = 'H', left(d.nom_valor_dominio, len(d.nom_valor_dominio) - 1) + 'a', d.nom_valor_dominio),
         sexo                   = m.sexo,
         edad                   = case
                                    when m.año_nacimiento is null then null
                                    when year(getdate()) - m.año_nacimiento = 1 then '1 año'
                                    else convert(varchar(5), year(getdate()) - m.año_nacimiento) + ' años'
                                  end,
         raza                   = r.valor_caracteristica,
         caracteristicasMascota = p.caracteristicas_mascota,
         condicionAdopcion      = p.condicion_adopcion,
         foto                   = p.foto,
         estadoPublicacion      = p.estado_publicacion
    from dbo.publicaciones_adopcion p (nolock)
         join dbo.mascotas m (nolock)
           on p.nro_reg_municipal = m.nro_reg_municipal
         left join dbo.caracteristicas_mascotas e (nolock)
           on m.nro_reg_municipal = e.nro_reg_municipal
          and e.cod_rasgo         = 'ESP'
         left join dbo.dominio_rasgos_mascotas d (nolock)
           on e.cod_rasgo         = d.cod_rasgo
          and e.nro_valor_dominio = d.nro_valor_dominio
         left join dbo.caracteristicas_mascotas r (nolock)
           on m.nro_reg_municipal = r.nro_reg_municipal
          and r.cod_rasgo         = 'RAZ'
   where p.id_refugio = @id_refugio
   order by p.nro_publicacion

end
go

-- execute dbo.get_publicaciones @id_refugio=1

/* ------------------------------------------
   Procedimiento: ins_publicacion
   ------------------------------------------ */
create or alter procedure dbo.ins_publicacion
(
 @id_refugio				integer,
 @nombre					varchar(100),
 @cod_especie				smallint,
 @sexo						varchar(1),
 @anio_nacimiento			smallint		= null,
 @raza						varchar(255)	= null,
 @caracteristicas_mascota	varchar(4000),
 @condicion_adopcion		varchar(4000),
 @foto						varchar(max),
 @nro_publicacion			integer			output
)
as
begin

  set nocount on

  declare @nro_reg_municipal integer

  if trim(@raza) = ''
     set @raza = null

  insert into dbo.mascotas(nombre, sexo, año_nacimiento, microchip, vive, id_responsable, id_refugio)
  values(trim(@nombre),
         @sexo,
         @anio_nacimiento,
         null,
         1,
         null,
         @id_refugio)

  set @nro_reg_municipal = @@identity

  insert into dbo.caracteristicas_mascotas(nro_reg_municipal, cod_rasgo, nro_caracteristica, nro_valor_dominio, valor_caracteristica)
  values(@nro_reg_municipal, 'ESP', 1, @cod_especie, null)

  if @raza is not null
     insert into dbo.caracteristicas_mascotas(nro_reg_municipal, cod_rasgo, nro_caracteristica, nro_valor_dominio, valor_caracteristica)
     values(@nro_reg_municipal, 'RAZ', 1, null, trim(@raza))

  insert into dbo.publicaciones_adopcion(nro_reg_municipal, id_refugio, fecha_publicacion, caracteristicas_mascota, condicion_adopcion, foto, estado_publicacion)
  values(@nro_reg_municipal,
         @id_refugio,
         getdate(),
         trim(@caracteristicas_mascota),
         trim(@condicion_adopcion),
         @foto,
         'A')

  set @nro_publicacion = @@identity

  set nocount off

end
go

-- declare @nro integer execute dbo.ins_publicacion @id_refugio=1, @nombre='Mora', @cod_especie=1, @sexo='H', @anio_nacimiento=2024, @caracteristicas_mascota='Perrita cariñosa.', @condicion_adopcion='Entrevista previa.', @foto='data:image/jpeg;base64,...', @nro_publicacion=@nro output select @nro

/* ------------------------------------------
   Procedimiento: act_publicacion
   ------------------------------------------ */
create or alter procedure dbo.act_publicacion
(
 @nro_publicacion		integer,
 @id_refugio			integer,
 @estado_publicacion	varchar(1)
)
as
begin

  set nocount on

  update dbo.publicaciones_adopcion
     set estado_publicacion = @estado_publicacion
   where nro_publicacion    = @nro_publicacion
     and id_refugio         = @id_refugio
     and estado_publicacion <> 'F'

  set nocount off

end
go

-- execute dbo.act_publicacion @nro_publicacion=1, @id_refugio=1, @estado_publicacion='P'

/* ------------------------------------------
   Procedimiento: get_veterinarias
   ------------------------------------------ */
create or alter procedure dbo.get_veterinarias
as
begin

  select idVeterinaria = v.id_veterinaria,
         razonSocial   = v.razon_social,
         domicilio     = v.domicilio,
         telefono      = v.telefono,
         correo        = v.correo,
         horarios      = h.valor,
         servicios     = (select string_agg(t.desc_tipo_atencion, ', ') within group (order by t.desc_tipo_atencion)
                            from string_split(s.valor, ',') c
                                 join dbo.tipos_atencion_sanitaria t (nolock)
                                   on trim(c.value) = t.cod_tipo_atencion)
    from dbo.veterinarias v (nolock)
         join dbo.configuracion_veterinatarias e (nolock)
           on v.id_veterinaria = e.id_veterinaria
          and e.cod_atributo   = 'ESTADO'
          and e.valor          = 'A'
         left join dbo.configuracion_veterinatarias h (nolock)
           on v.id_veterinaria = h.id_veterinaria
          and h.cod_atributo   = 'HORARIOS'
         left join dbo.configuracion_veterinatarias s (nolock)
           on v.id_veterinaria = s.id_veterinaria
          and s.cod_atributo   = 'SERVICIOS'
   order by v.razon_social

end
go

-- execute dbo.get_veterinarias

/* ------------------------------------------
   Procedimiento: get_tipos_atencion
   ------------------------------------------ */
create or alter procedure dbo.get_tipos_atencion
as
begin

  select codigo = cod_tipo_atencion,
         nombre = desc_tipo_atencion
    from dbo.tipos_atencion_sanitaria (nolock)
   order by desc_tipo_atencion

end
go

-- execute dbo.get_tipos_atencion

/* ------------------------------------------
   Procedimiento: get_refugios
   ------------------------------------------ */
create or alter procedure dbo.get_refugios
(
 @id_refugio		integer		= null
)
as
begin

  select idRefugio         = r.id_refugio,
         razonSocial       = r.razon_social,
         domicilio         = r.domicilio,
         telefono          = r.telefono,
         correo            = r.correo,
         horarios          = (select string_agg(h.dias + ' de ' + convert(varchar(5), h.hora_desde, 108) + ' a ' + convert(varchar(5), h.hora_hasta, 108), ' · ') within group (order by h.nro_horario)
                                from dbo.horarios_refugios h (nolock)
                               where h.id_refugio = r.id_refugio),
         cantPublicaciones = (select count(*)
                                from dbo.publicaciones_adopcion p (nolock)
                               where p.id_refugio         = r.id_refugio
                                 and p.estado_publicacion = 'A')
    from dbo.refugios r (nolock)
         join dbo.ciudadanos c (nolock)
           on r.id_responsable = c.id_ciudadano
   where c.habilitado = 1
     and (r.id_refugio = @id_refugio or @id_refugio is null)
   order by r.razon_social

end
go

-- execute dbo.get_refugios
-- execute dbo.get_refugios @id_refugio=1

/* ------------------------------------------
   Procedimiento: get_publicaciones_activas
   ------------------------------------------ */
create or alter procedure dbo.get_publicaciones_activas
(
 @id_refugio		integer
)
as
begin

  select nroPublicacion         = p.nro_publicacion,
         nrm                    = 'MUN-' + right('000000' + convert(varchar(10), m.nro_reg_municipal), 6),
         nombre                 = m.nombre,
         nomEspecie             = iif(m.sexo = 'H', left(d.nom_valor_dominio, len(d.nom_valor_dominio) - 1) + 'a', d.nom_valor_dominio),
         sexo                   = m.sexo,
         edad                   = case
                                    when m.año_nacimiento is null then null
                                    when year(getdate()) - m.año_nacimiento = 1 then '1 año'
                                    else convert(varchar(5), year(getdate()) - m.año_nacimiento) + ' años'
                                  end,
         raza                   = r.valor_caracteristica,
         caracteristicasMascota = p.caracteristicas_mascota,
         condicionAdopcion      = p.condicion_adopcion,
         foto                   = p.foto,
         estadoPublicacion      = p.estado_publicacion
    from dbo.publicaciones_adopcion p (nolock)
         join dbo.refugios f (nolock)
           on p.id_refugio = f.id_refugio
         join dbo.ciudadanos c (nolock)
           on f.id_responsable = c.id_ciudadano
          and c.habilitado     = 1
         join dbo.mascotas m (nolock)
           on p.nro_reg_municipal = m.nro_reg_municipal
         left join dbo.caracteristicas_mascotas e (nolock)
           on m.nro_reg_municipal = e.nro_reg_municipal
          and e.cod_rasgo         = 'ESP'
         left join dbo.dominio_rasgos_mascotas d (nolock)
           on e.cod_rasgo         = d.cod_rasgo
          and e.nro_valor_dominio = d.nro_valor_dominio
         left join dbo.caracteristicas_mascotas r (nolock)
           on m.nro_reg_municipal = r.nro_reg_municipal
          and r.cod_rasgo         = 'RAZ'
   where p.id_refugio         = @id_refugio
     and p.estado_publicacion = 'A'
   order by p.nro_publicacion

end
go

-- execute dbo.get_publicaciones_activas @id_refugio=1

/* ------------------------------------------
   Procedimiento: get_veterinarias_integracion
   Veterinarias activas con la configuración que hace falta para
   consumir sus servicios (una columna por atributo).
   ------------------------------------------ */
create or alter procedure dbo.get_veterinarias_integracion
as
begin

  select idVeterinaria = v.id_veterinaria,
         razonSocial   = v.razon_social,
         tecnologia    = t.valor,
         url           = u.valor,
         parametros    = p.valor,
         apiKey        = k.valor
    from dbo.veterinarias v (nolock)
         join dbo.configuracion_veterinatarias e (nolock)
           on v.id_veterinaria = e.id_veterinaria
          and e.cod_atributo   = 'ESTADO'
          and e.valor          = 'A'
         left join dbo.configuracion_veterinatarias t (nolock)
           on v.id_veterinaria = t.id_veterinaria
          and t.cod_atributo   = 'TECNOLOGIA'
         left join dbo.configuracion_veterinatarias u (nolock)
           on v.id_veterinaria = u.id_veterinaria
          and u.cod_atributo   = 'URL'
         left join dbo.configuracion_veterinatarias p (nolock)
           on v.id_veterinaria = p.id_veterinaria
          and p.cod_atributo   = 'PARAMETROS'
         left join dbo.configuracion_veterinatarias k (nolock)
           on v.id_veterinaria = k.id_veterinaria
          and k.cod_atributo   = 'API_KEY'
   order by v.id_veterinaria

end
go

-- execute dbo.get_veterinarias_integracion

/* ------------------------------------------
   Procedimiento: ins_mascota_veterinaria
   Registra en el Registro Único una mascota que mandó una veterinaria.
   Si ya existe (mismo microchip, o mismo nombre + año de nacimiento +
   responsable) devuelve su NRM; si no, la da de alta con su responsable
   (si todavía no es ciudadano), especie, raza y pelaje.
   ------------------------------------------ */
create or alter procedure dbo.ins_mascota_veterinaria
(
 @nombre					varchar(100),
 @especie					varchar(100),
 @raza						varchar(255)	= null,
 @sexo						varchar(1),
 @fecha_nacimiento			date			= null,
 @pelaje					varchar(255)	= null,
 @microchip					varchar(20)		= null,
 @vive						bit,
 @cuil_responsable			varchar(11),
 @apellido_responsable		varchar(40),
 @nombre_responsable		varchar(100),
 @correo_responsable		varchar(255)	= null,
 @telefono_responsable		varchar(20)		= null,
 @domicilio_responsable		varchar(255)	= null,
 @nro_reg_municipal			integer			output
)
as
begin

  set nocount on

  declare @id_responsable	integer,
          @anio_nacimiento	smallint,
          @nro_especie		smallint

  if trim(@raza) = ''
     set @raza = null
  if trim(@pelaje) = ''
     set @pelaje = null
  if trim(@microchip) = ''
     set @microchip = null

  set @anio_nacimiento   = year(@fecha_nacimiento)
  set @nro_reg_municipal = null

  -- Responsable: si todavía no es ciudadano, se da de alta (clave inicial = CUIL)
  select @id_responsable = id_ciudadano
    from dbo.ciudadanos (nolock)
   where cuil = trim(@cuil_responsable)

  if @id_responsable is null
     begin
       insert into dbo.ciudadanos(apellido, nombre, cuil, clave, correo, telefono, domicilio, habilitado)
       values(upper(trim(@apellido_responsable)),
              upper(trim(@nombre_responsable)),
              trim(@cuil_responsable),
              trim(@cuil_responsable),
              isnull(trim(@correo_responsable), ''),
              nullif(trim(@telefono_responsable), ''),
              nullif(upper(trim(@domicilio_responsable)), ''),
              1)

       set @id_responsable = @@identity
     end

  -- ¿La mascota ya existe en el Registro Único? Primero por microchip
  if @microchip is not null
     select top 1 @nro_reg_municipal = nro_reg_municipal
       from dbo.mascotas (nolock)
      where microchip = @microchip
      order by nro_reg_municipal

  -- Si no tiene microchip (o no se encontró): nombre + año de nacimiento + responsable
  if @nro_reg_municipal is null
     select top 1 @nro_reg_municipal = nro_reg_municipal
       from dbo.mascotas (nolock)
      where upper(trim(nombre)) = upper(trim(@nombre))
        and isnull(año_nacimiento, 0) = isnull(@anio_nacimiento, 0)
        and id_responsable = @id_responsable
      order by nro_reg_municipal

  if @nro_reg_municipal is not null
     begin
       update dbo.mascotas
          set vive      = @vive,
              microchip = isnull(microchip, @microchip)
        where nro_reg_municipal = @nro_reg_municipal

       set nocount off
       return
     end

  -- Mascota nueva
  insert into dbo.mascotas(nombre, sexo, año_nacimiento, microchip, vive, id_responsable, id_refugio)
  values(trim(@nombre),
         upper(@sexo),
         @anio_nacimiento,
         @microchip,
         @vive,
         @id_responsable,
         null)

  set @nro_reg_municipal = @@identity

  -- Especie: se busca por nombre y, si la Municipalidad no la tiene, se agrega
  select @nro_especie = nro_valor_dominio
    from dbo.dominio_rasgos_mascotas (nolock)
   where cod_rasgo         = 'ESP'
     and nom_valor_dominio = trim(@especie)

  if @nro_especie is null
     begin
       select @nro_especie = isnull(max(nro_valor_dominio), 0) + 1
         from dbo.dominio_rasgos_mascotas
        where cod_rasgo = 'ESP'

       insert into dbo.dominio_rasgos_mascotas(cod_rasgo, nro_valor_dominio, nom_valor_dominio)
       values('ESP', @nro_especie, trim(@especie))
     end

  insert into dbo.caracteristicas_mascotas(nro_reg_municipal, cod_rasgo, nro_caracteristica, nro_valor_dominio, valor_caracteristica)
  values(@nro_reg_municipal, 'ESP', 1, @nro_especie, null)

  if @raza is not null
     insert into dbo.caracteristicas_mascotas(nro_reg_municipal, cod_rasgo, nro_caracteristica, nro_valor_dominio, valor_caracteristica)
     values(@nro_reg_municipal, 'RAZ', 1, null, trim(@raza))

  if @pelaje is not null
     insert into dbo.caracteristicas_mascotas(nro_reg_municipal, cod_rasgo, nro_caracteristica, nro_valor_dominio, valor_caracteristica)
     values(@nro_reg_municipal, 'PEL', 1, null, trim(@pelaje))

  set nocount off

end
go

-- declare @nrm integer execute dbo.ins_mascota_veterinaria @nombre='Firulais', @especie='Perro', @raza='Mestizo', @sexo='M', @fecha_nacimiento='2021-03-10', @microchip='900100000000001', @vive=1, @cuil_responsable='27280001112', @apellido_responsable='LOPEZ', @nombre_responsable='CAROLINA', @correo_responsable='carolina.lopez@mail.com', @nro_reg_municipal=@nrm output select @nrm

/* ------------------------------------------
   Procedimiento: ins_atencion_veterinaria
   Registra una atención que mandó una veterinaria.
   La misma atención = misma mascota + fecha de atención + tipo:
   si ya está, se actualiza (por si la veterinaria la corrigió);
   así se puede volver a disparar la integración sin duplicar nada.
   También actualiza si la mascota vive.
   Resultado: N nueva, A actualizada, S sin cambios,
              O otro tipo de atención (no es del programa),
              X el NRM no existe en el Registro Único.
   ------------------------------------------ */
create or alter procedure dbo.ins_atencion_veterinaria
(
 @id_veterinaria			integer,
 @nro_reg_municipal			integer,
 @vive						bit,
 @fecha_atencion			date,
 @cod_tipo_atencion			varchar(3),
 @detalle					varchar(4000)	= null,
 @fecha_proxima				date			= null,
 @cuil_profesional			varchar(11),
 @apellido_profesional		varchar(40),
 @nombre_profesional		varchar(100),
 @correo_profesional		varchar(255)	= null,
 @resultado					varchar(1)		output
)
as
begin

  set nocount on

  declare @id_profesional	integer,
          @nro_registro		integer

  if trim(@detalle) = ''
     set @detalle = null

  if not exists (select 1
                   from dbo.mascotas (nolock)
                  where nro_reg_municipal = @nro_reg_municipal)
     begin
       set @resultado = 'X'
       set nocount off
       return
     end

  update dbo.mascotas
     set vive = @vive
   where nro_reg_municipal = @nro_reg_municipal
     and vive <> @vive

  if not exists (select 1
                   from dbo.tipos_atencion_sanitaria (nolock)
                  where cod_tipo_atencion = @cod_tipo_atencion)
     begin
       set @resultado = 'O'
       set nocount off
       return
     end

  -- Profesional: si todavía no es ciudadano, se da de alta (clave inicial = CUIL)
  select @id_profesional = id_ciudadano
    from dbo.ciudadanos (nolock)
   where cuil = trim(@cuil_profesional)

  if @id_profesional is null
     begin
       insert into dbo.ciudadanos(apellido, nombre, cuil, clave, correo, telefono, domicilio, habilitado)
       values(upper(trim(@apellido_profesional)),
              upper(trim(@nombre_profesional)),
              trim(@cuil_profesional),
              trim(@cuil_profesional),
              isnull(trim(@correo_profesional), ''),
              null,
              null,
              1)

       set @id_profesional = @@identity
     end

  if not exists (select 1
                   from dbo.profesionales_veterinarias (nolock)
                  where id_veterinaria = @id_veterinaria
                    and id_profesional = @id_profesional)
     insert into dbo.profesionales_veterinarias(id_veterinaria, id_profesional, baja)
     values(@id_veterinaria, @id_profesional, 0)

  -- ¿Es una atención que ya estaba registrada?
  select @nro_registro = nro_registro
    from dbo.informacion_sanitaria (nolock)
   where nro_reg_municipal = @nro_reg_municipal
     and fecha_atencion    = @fecha_atencion
     and cod_tipo_atencion = @cod_tipo_atencion

  if @nro_registro is null
     begin
       select @nro_registro = isnull(max(nro_registro), 0) + 1
         from dbo.informacion_sanitaria
        where nro_reg_municipal = @nro_reg_municipal

       insert into dbo.informacion_sanitaria(nro_reg_municipal, nro_registro, fecha_atencion, cod_tipo_atencion, detalle_atencion, fecha_vencimiento, id_veterinaria, id_profesional)
       values(@nro_reg_municipal,
              @nro_registro,
              @fecha_atencion,
              @cod_tipo_atencion,
              trim(@detalle),
              @fecha_proxima,
              @id_veterinaria,
              @id_profesional)

       set @resultado = 'N'
     end
  else if exists (select 1
                    from dbo.informacion_sanitaria (nolock)
                   where nro_reg_municipal = @nro_reg_municipal
                     and nro_registro      = @nro_registro
                     and (isnull(detalle_atencion, '')          <> isnull(trim(@detalle), '')
                      or  isnull(fecha_vencimiento, '19000101') <> isnull(@fecha_proxima, '19000101')
                      or  id_veterinaria                        <> @id_veterinaria
                      or  id_profesional                        <> @id_profesional))
     begin
       update dbo.informacion_sanitaria
          set detalle_atencion  = trim(@detalle),
              fecha_vencimiento = @fecha_proxima,
              id_veterinaria    = @id_veterinaria,
              id_profesional    = @id_profesional
        where nro_reg_municipal = @nro_reg_municipal
          and nro_registro      = @nro_registro

       set @resultado = 'A'
     end
  else
     set @resultado = 'S'

  set nocount off

end
go

-- declare @r varchar(1) execute dbo.ins_atencion_veterinaria @id_veterinaria=1, @nro_reg_municipal=1, @vive=1, @fecha_atencion='2026-10-15', @cod_tipo_atencion='VAC', @detalle='Antirrábica (lote AR-2291)', @fecha_proxima='2027-10-15', @cuil_profesional='27250003334', @apellido_profesional='RIOS', @nombre_profesional='LAURA', @resultado=@r output select @r

/* ------------------------------------------
   Procedimiento: ins_log_integracion
   ------------------------------------------ */
create or alter procedure dbo.ins_log_integracion
(
 @proceso			varchar(50),
 @organizacion		varchar(255)	= null,
 @referencia		varchar(100)	= null,
 @detalle			varchar(4000),
 @error				bit
)
as
begin

  set nocount on

  insert into dbo.logs_integraciones(fecha, proceso, organizacion, referencia, detalle, error)
  values(getdate(),
         @proceso,
         @organizacion,
         @referencia,
         left(@detalle, 4000),
         @error)

  set nocount off

end
go

-- execute dbo.ins_log_integracion @proceso='INTEGRACION VETERINARIAS', @organizacion='VETERINARIA SAN ROQUE', @detalle='Connection refused', @error=1

/* ------------------------------------------
   Datos iniciales: usuario de refugio
   ------------------------------------------ */
insert into dbo.ciudadanos(apellido, nombre, cuil, clave, correo, telefono, domicilio, habilitado)
values('PEREZ', 'JUAN', '20123456789', '123456', 'juan.perez@ubp.edu.ar', '3515551234', 'AV. DONATO ALVAREZ 380', 1)
go

insert into dbo.refugios(razon_social, correo, telefono, domicilio, habilitacion_municipal, id_responsable)
values('REFUGIO HUELLITAS', 'huellitas@refugio.org', '3515554321', 'CAMINO A SAN CARLOS KM 5', 'HM-0001', 1)
go

/* ------------------------------------------
   Datos iniciales: segundo refugio y horarios de atención
   ------------------------------------------ */
insert into dbo.ciudadanos(apellido, nombre, cuil, clave, correo, telefono, domicilio, habilitado)
values('GOMEZ', 'MARIA', '27301234567', '123456', 'maria.gomez@ubp.edu.ar', '3515559876', 'BV. SAN JUAN 1250', 1)
go

insert into dbo.refugios(razon_social, correo, telefono, domicilio, habilitacion_municipal, id_responsable)
values('REFUGIO COLITAS FELICES', 'colitas@refugio.org', '3515558765', 'AV. RAFAEL NUÑEZ 4200', 'HM-0005', 2)
go

insert into dbo.horarios_refugios(id_refugio, nro_horario, dias, hora_desde, hora_hasta)
values(1, 1, 'Lunes a viernes', '09:00', '17:00'),
      (1, 2, 'Sábados', '10:00', '13:00'),
      (2, 1, 'Martes a domingos', '14:00', '19:00')
go

/* ------------------------------------------
   Datos iniciales: veterinarias adheridas
   ------------------------------------------ */
insert into dbo.veterinarias(razon_social, correo, telefono, domicilio, habilitacion_municipal)
values('VETERINARIA SAN ROQUE', 'contacto@vetsanroque.com.ar', '3514221100', 'AV. COLON 1540', 'HV-0001'),
      ('CLINICA VETERINARIA DEL PARQUE', 'turnos@vetdelparque.com.ar', '3514685500', 'AV. HIPOLITO YRIGOYEN 480', 'HV-0002'),
      ('VETERINARIA HUELLAS', 'info@vethuellas.com.ar', '3514773200', 'AV. RECTA MARTINOLLI 7150', 'HV-0003'),
      ('CENTRO VETERINARIO ALBERDI', 'consultas@vetalberdi.com.ar', '3514809900', 'AV. DUARTE QUIROS 2100', 'HV-0004'),
      ('VETERINARIA LOS ALAMOS', 'info@vetlosalamos.com.ar', '3514551800', 'AV. SABATTINI 3300', 'HV-0005')
go

insert into dbo.configuracion_veterinatarias(id_veterinaria, cod_atributo, valor)
values(1, 'ESTADO', 'A'),
      (1, 'HORARIOS', 'Lunes a viernes de 09:00 a 19:00 · Sábados de 09:00 a 13:00'),
      (1, 'SERVICIOS', 'VAC,DES,CAS'),
      (2, 'ESTADO', 'A'),
      (2, 'HORARIOS', 'Lunes a sábados de 08:30 a 20:00'),
      (2, 'SERVICIOS', 'VAC,DES,EST,CAS'),
      (3, 'ESTADO', 'A'),
      (3, 'HORARIOS', 'Lunes a viernes de 10:00 a 18:00'),
      (3, 'SERVICIOS', 'VAC,DES'),
      (4, 'ESTADO', 'A'),
      (4, 'HORARIOS', 'Todos los días de 00:00 a 24:00'),
      (4, 'SERVICIOS', 'VAC,EST,CAS'),
      (5, 'ESTADO', 'I'),
      (5, 'HORARIOS', 'Lunes a viernes de 09:00 a 13:00'),
      (5, 'SERVICIOS', 'VAC')
go

/* ------------------------------------------
   Datos iniciales: integración con las veterinarias
   Todo lo que hace falta para consumir los servicios de cada
   veterinaria está acá, no en el código:
   - TECNOLOGIA: REST o SOAP (elige el cliente que se usa).
   - URL: dirección base de los servicios de la veterinaria.
   - PARAMETROS: JSON con cómo se llama cada operación del contrato
     en esa veterinaria (ruta REST o servicio + operación SOAP).
   - API_KEY: credencial que la veterinaria le dio a la Municipalidad.
   Las veterinarias 2 (REST) y 4 (SOAP) quedan configuradas pero
   apagadas en la demo, para mostrar la tolerancia a fallos.
   ------------------------------------------ */
insert into dbo.configuracion_veterinatarias(id_veterinaria, cod_atributo, valor)
values(1, 'TECNOLOGIA', 'REST'),
      (1, 'URL', 'http://localhost:8091/api/v1'),
      (1, 'PARAMETROS', '{"mascotasPendientes": "/mascotas/pendientes", "nroRegMunicipal": "/mascotas/nro-reg-municipal", "atenciones": "/atenciones/listado"}'),
      (1, 'API_KEY', 'VET1kR7pQ2mZ9xL4sT8vN3bH6yW1cF5dG0aJ'),
      (2, 'TECNOLOGIA', 'REST'),
      (2, 'URL', 'http://localhost:8092/api/v1'),
      (2, 'PARAMETROS', '{"mascotasPendientes": "/mascotas/pendientes", "nroRegMunicipal": "/mascotas/nro-reg-municipal", "atenciones": "/atenciones/listado"}'),
      (2, 'API_KEY', 'VET2mW4nB8vC1xZ6kL9pQ3rT7yH2sD5fG0aJ'),
      (3, 'TECNOLOGIA', 'SOAP'),
      (3, 'URL', 'http://localhost:8095/services'),
      (3, 'PARAMETROS', '{"mascotasPendientes": {"servicio": "/mascotas", "namespace": "http://services.veterinariaws.das.ubp.edu.ar/", "serviceName": "MascotasServiceService", "portName": "MascotasServicePort", "operacion": "getMascotasPendientes"},
                         "nroRegMunicipal": {"servicio": "/mascotas", "namespace": "http://services.veterinariaws.das.ubp.edu.ar/", "serviceName": "MascotasServiceService", "portName": "MascotasServicePort", "operacion": "actNroRegMunicipal"},
                         "atenciones": {"servicio": "/atenciones", "namespace": "http://services.veterinariaws.das.ubp.edu.ar/", "serviceName": "AtencionesServiceService", "portName": "AtencionesServicePort", "operacion": "getAtenciones"}}'),
      (3, 'API_KEY', 'VET3pL6qR1sT5vW9xZ2bC4nM8kH3jD7fG0aJ'),
      (4, 'TECNOLOGIA', 'SOAP'),
      (4, 'URL', 'http://localhost:8096/services'),
      (4, 'PARAMETROS', '{"mascotasPendientes": {"servicio": "/mascotas", "namespace": "http://services.veterinariaws.das.ubp.edu.ar/", "serviceName": "MascotasServiceService", "portName": "MascotasServicePort", "operacion": "getMascotasPendientes"},
                         "nroRegMunicipal": {"servicio": "/mascotas", "namespace": "http://services.veterinariaws.das.ubp.edu.ar/", "serviceName": "MascotasServiceService", "portName": "MascotasServicePort", "operacion": "actNroRegMunicipal"},
                         "atenciones": {"servicio": "/atenciones", "namespace": "http://services.veterinariaws.das.ubp.edu.ar/", "serviceName": "AtencionesServiceService", "portName": "AtencionesServicePort", "operacion": "getAtenciones"}}'),
      (4, 'API_KEY', 'VET4sD2fG6hJ1kL5zX9cV3bN7mQ4wE8rT0aJ')
go
