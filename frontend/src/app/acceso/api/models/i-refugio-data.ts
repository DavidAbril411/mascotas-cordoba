import { ICiudadanoData } from './i-ciudadano-data';

export interface IRefugioData {
  razonSocial: string;
  correo: string;
  telefono: string;
  domicilio: string;
  habilitacionMunicipal: string;
  responsable: ICiudadanoData;
}
