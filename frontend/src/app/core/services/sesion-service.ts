import { computed, Injectable, signal } from '@angular/core';

export interface IUsuario {
  idCiudadano: number;
  apellido: string;
  nombre: string;
  cuil: string;
  correo: string;
  idRefugio: number;
  razonSocial: string;
  token: string;
}

@Injectable({
  providedIn: 'root'
})
export class SesionService {

  private _usuario = signal<IUsuario | null>(JSON.parse(localStorage.getItem('usuario') ?? 'null'));

  readonly usuario = computed(() =>
    this._usuario()
  );

  iniciar(usuario: IUsuario): void {
    localStorage.setItem('usuario', JSON.stringify(usuario));
    this._usuario.set(usuario);
  }

  cerrar(): void {
    localStorage.removeItem('usuario');
    this._usuario.set(null);
  }

}
