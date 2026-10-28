import { Component, inject, input, model, signal } from '@angular/core';
import { SlicePipe } from '@angular/common';
import { Router } from '@angular/router';
import { form, FormField } from '@angular/forms/signals';
import { MatDialog } from '@angular/material/dialog';
import { PublicacionesResource } from '../../api/resources/publicaciones-resource';
import { IPublicacion } from '../../api/models/i-publicacion';
import { IPublicacionData } from '../../api/models/i-publicacion-data';
import { IEspecie } from '../../api/models/i-especie';
import { ModalPublicacion } from '../../components/modal-publicacion/modal-publicacion';
import { SesionService } from '../../../core/services/sesion-service';
import { FiltrarPorPipe } from '../../../shared/pipes/filtrar-por-pipe';

@Component({
  imports: [
    SlicePipe,
    FiltrarPorPipe,
    FormField
  ],
  selector: 'app-list-page',
  styleUrl: './list-page.scss',
  templateUrl: './list-page.html',
})
export class ListPage {

  private _api = inject(PublicacionesResource);
  private _sesion = inject(SesionService);
  private _router = inject(Router);
  private _dialog = inject(MatDialog);

  readonly usuario = this._sesion.usuario;

  listado = model<IPublicacion[]>([]);
  especies = input.required<IEspecie[]>();

  filtroNombre = signal<string>('');
  filtroNombreForm = form(this.filtroNombre);

  filtroEstado = signal<string>('');
  filtroEstadoForm = form(this.filtroEstado);

  nuevaPublicacion(): void {
    const ref = this._dialog.open(ModalPublicacion, {
      data: this.especies(),
      width: '800px',
      maxWidth: '95vw',
      panelClass: 'modal-publicacion',
      disableClose: true,
      autoFocus: false
    });
    ref.afterClosed().subscribe((result: IPublicacionData) => {
      if (result) {
        this._api.insertarPublicacion(result).subscribe(() => {
          this._api.getPublicaciones().subscribe((publicaciones: IPublicacion[]) => {
            this.listado.set(publicaciones);
          });
        });
      }
    });
  }

  setEstado(nroPublicacion: number, estadoPublicacion: string): void {
    this._api.actPublicacion(nroPublicacion, estadoPublicacion).subscribe(() => {
      this.listado.update((publicaciones: IPublicacion[]) => publicaciones.map(p =>
        p.nroPublicacion === nroPublicacion ? { ...p, estadoPublicacion } : p
      ));
    });
  }

  cerrarSesion(): void {
    this._sesion.cerrar();
    this._router.navigate(['/acceso']);
  }

}
