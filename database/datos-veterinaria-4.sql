/* ------------------------------------------
   Datos de prueba: Veterinaria 4 - CENTRO VETERINARIO ALBERDI (SOAP)
   En la demo queda apagada para mostrar la tolerancia a fallos.
   ------------------------------------------ */
use veterinaria_4
go

insert into dbo.personas(apellido, nombre, tipo_documento, nro_documento, correo, telefono, domicilio)
values('GOMEZ', 'SOFIA', 'CUIL', '27330004445', 'sofia.gomez@mail.com', '3515550202', 'BV. CHACABUCO 850'),
      ('MOLINA', 'ANDREA', 'CUIL', '27310007778', 'andrea.molina@vetalberdi.com.ar', '3514809901', null)
go

insert into dbo.profesionales(id_persona, matricula_profesional, fecha_inicio, fecha_fin)
values(2, 'MP-9012', '2020-04-01', null)
go

insert into dbo.mascotas(nombre, cod_especie, id_raza, sexo, fecha_nacimiento, color, pelaje, microchip, vive, nro_reg_municipal)
values('Nala', 'GAT', 1, 'H', '2024-02-14', 'Atigrado', 'Corto', null, 1, null)
go

insert into dbo.responsables_mascotas(id_mascota, id_persona, fecha_desde, fecha_hasta, observaciones, principal)
values(1, 1, '2024-04-01', null, null, 1)
go

insert into dbo.atencion_sanitaria(id_mascota, nro_atencion, id_profesional, fecha_atencion, fecha_prox_atencion, observ_atencion)
values(1, 1, 2, '2026-10-01', null, null)
go

insert into dbo.detalle_atencion_sanitaria(id_mascota, nro_atencion, cod_tipo_atencion, nro_detalle, cod_vacuna, lote_vacuna, fecha_prox_vacunacion, indicaciones, intervencion)
values(1, 1, 'VAC', 1, 'TRI', 'TF-554', '2027-10-01', null, null)
go
