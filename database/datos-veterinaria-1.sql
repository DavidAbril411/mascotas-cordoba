/* ------------------------------------------
   Datos de prueba: Veterinaria 1 - VETERINARIA SAN ROQUE (REST)
   ------------------------------------------ */
use veterinaria_1
go

insert into dbo.personas(apellido, nombre, tipo_documento, nro_documento, correo, telefono, domicilio)
values('LOPEZ', 'CAROLINA', 'CUIL', '27280001112', 'carolina.lopez@mail.com', '3515550101', 'AV. VELEZ SARSFIELD 1200'),
      ('RIOS', 'LAURA', 'CUIL', '27250003334', 'laura.rios@vetsanroque.com.ar', '3514221101', null)
go

insert into dbo.profesionales(id_persona, matricula_profesional, fecha_inicio, fecha_fin)
values(2, 'MP-1234', '2015-03-01', null)
go

insert into dbo.mascotas(nombre, cod_especie, id_raza, sexo, fecha_nacimiento, color, pelaje, microchip, vive, nro_reg_municipal)
values('Firulais', 'PER', 1, 'M', '2021-03-10', 'Marrón', 'Corto', '900100000000001', 1, null),
      ('Michi', 'GAT', 2, 'H', '2023-06-01', 'Crema', 'Corto', null, 1, null)
go

insert into dbo.responsables_mascotas(id_mascota, id_persona, fecha_desde, fecha_hasta, observaciones, principal)
values(1, 1, '2021-05-01', null, null, 1),
      (2, 1, '2023-08-15', null, null, 1)
go

insert into dbo.atencion_sanitaria(id_mascota, nro_atencion, id_profesional, fecha_atencion, fecha_prox_atencion, observ_atencion)
values(1, 1, 2, '2026-10-15', '2027-01-15', 'Control anual sin observaciones'),
      (2, 1, 2, '2026-09-01', null, 'Alta sin complicaciones')
go

insert into dbo.detalle_atencion_sanitaria(id_mascota, nro_atencion, cod_tipo_atencion, nro_detalle, cod_vacuna, lote_vacuna, fecha_prox_vacunacion, indicaciones, intervencion)
values(1, 1, 'VAC', 1, 'ANT', 'AR-2291', '2027-10-15', null, null),
      (1, 1, 'VAC', 2, 'SEX', 'SX-118', '2027-10-15', null, null),
      (1, 1, 'DES', 1, null, null, null, 'Antiparasitario interno, 1 comprimido', null),
      (2, 1, 'EST', 1, null, null, null, 'Reposo 10 días', 'Ovariohisterectomía')
go
