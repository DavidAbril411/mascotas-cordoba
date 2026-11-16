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

  private _usuario = signal<IUsuario | null>(this.getUsuarioGuardado());

  readonly usuario = computed(() =>
    this._usuario()
  );

  iniciar(usuario: IUsuario): void {
    sessionStorage.setItem('usuario', JSON.stringify(usuario));
    this._usuario.set(usuario);
  }

  cerrar(): void {
    sessionStorage.removeItem('usuario');
    this._usuario.set(null);
  }

  private getUsuarioGuardado(): IUsuario | null {
    try {
      const usuario = JSON.parse(sessionStorage.getItem('usuario') ?? 'null');
      return usuario?.token ? usuario : null;
    }
    catch {
      return null;
    }
  }

}
