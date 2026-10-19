import { Injectable, signal } from '@angular/core';

export interface IUsuario {
  idSesion: string;
  idCiudadano: number;
  apellido: string;
  nombre: string;
  cuil: string;
  correo: string;
  idRefugio: number;
  razonSocial: string;
}

@Injectable({
  providedIn: 'root'
})
export class SesionService {

  private _usuario = signal<IUsuario | null>(null);

  readonly usuario = this._usuario.asReadonly();

  iniciar(usuario: IUsuario): void {
    this._usuario.set(usuario);
  }

}
