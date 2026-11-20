/* ------------------------------------------
   Datos de prueba: Veterinaria 3 - VETERINARIA HUELLAS (SOAP)
   Firulais también se atiende en la Veterinaria 1 (mismo microchip):
   la Municipalidad le tiene que dar el mismo NRM.
   Toby murió (vive = 0).
   ------------------------------------------ */
use veterinaria_3
go

insert into dbo.personas(apellido, nombre, tipo_documento, nro_documento, correo, telefono, domicilio)
values('FERNANDEZ', 'MARTIN', 'CUIL', '20290002223', 'martin.fernandez@mail.com', '3515550303', 'AV. COLON 2450'),
      ('LOPEZ', 'CAROLINA', 'CUIL', '27280001112', 'carolina.lopez@mail.com', '3515550101', 'AV. VELEZ SARSFIELD 1200'),
      ('SOSA', 'PABLO', 'CUIL', '20260006667', 'pablo.sosa@vethuellas.com.ar', '3514773201', null)
go

insert into dbo.profesionales(id_persona, matricula_profesional, fecha_inicio, fecha_fin)
values(3, 'MP-5678', '2012-06-01', null)
go

insert into dbo.mascotas(nombre, cod_especie, id_raza, sexo, fecha_nacimiento, color, pelaje, microchip, vive, nro_reg_municipal)
values('Rocco', 'PER', 2, 'M', '2019-08-20', 'Dorado', 'Corto', '900100000000002', 1, null),
      ('Firulais', 'PER', 1, 'M', '2021-03-10', 'Marrón', 'Corto', '900100000000001', 1, null),
      ('Toby', 'PER', 1, 'M', '2010-05-05', 'Negro', 'Corto', null, 0, null)
go

insert into dbo.responsables_mascotas(id_mascota, id_persona, fecha_desde, fecha_hasta, observaciones, principal)
values(1, 1, '2019-10-01', null, null, 1),
      (2, 2, '2026-11-01', null, null, 1),
      (3, 1, '2010-07-01', null, null, 1)
go

insert into dbo.atencion_sanitaria(id_mascota, nro_atencion, id_profesional, fecha_atencion, fecha_prox_atencion, observ_atencion)
values(1, 1, 3, '2026-11-02', null, null),
      (2, 1, 3, '2026-11-10', '2026-11-17', 'Otitis leve'),
      (3, 1, 3, '2026-08-10', null, 'Falleció por causas naturales')
go

insert into dbo.detalle_atencion_sanitaria(id_mascota, nro_atencion, cod_tipo_atencion, nro_detalle, cod_vacuna, lote_vacuna, fecha_prox_vacunacion, indicaciones, intervencion)
values(1, 1, 'VAC', 1, 'ANT', 'AR-3310', '2027-11-02', null, null),
      (2, 1, 'CON', 1, null, null, null, 'Gotas óticas cada 12 horas por 7 días', null),
      (3, 1, 'CON', 1, null, null, null, null, null)
go
