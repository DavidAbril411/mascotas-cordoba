import { Component, inject, model, signal } from '@angular/core';
import { form, FormField } from '@angular/forms/signals';
import { MatDialog } from '@angular/material/dialog';
import { PublicacionesResource } from '../../api/resources/publicaciones-resource';
import { IPublicacion } from '../../api/models/i-publicacion';
import { ModalPublicacion } from '../../components/modal-publicacion/modal-publicacion';
import { ModalFinalizarPublicacion } from '../../components/modal-finalizar-publicacion/modal-finalizar-publicacion';
import { Navbar } from '../../../core/layouts/navbar/navbar';
import { FiltrarPorPipe } from '../../../shared/pipes/filtrar-por-pipe';

@Component({
  imports: [
    FiltrarPorPipe,
    FormField,
    Navbar
  ],
  selector: 'app-list-page',
  styleUrl: './list-page.scss',
  templateUrl: './list-page.html',
})
export class ListPage {

  private _api = inject(PublicacionesResource);
  private _dialog = inject(MatDialog);

  listado = model<IPublicacion[]>([]);

  filtroNombre = signal<string>('');
  filtroNombreForm = form(this.filtroNombre);

  filtroEstado = signal<string>('');
  filtroEstadoForm = form(this.filtroEstado);

  nuevaPublicacion(): void {
    const ref = this._dialog.open(ModalPublicacion, {
      width: '800px',
      maxWidth: '95vw',
      panelClass: 'modal-publicacion',
      disableClose: true,
      autoFocus: false
    });
    ref.afterClosed().subscribe((result: boolean) => {
      if (result) {
        this._api.getPublicaciones().subscribe((publicaciones: IPublicacion[]) => {
          this.listado.set(publicaciones);
        });
      }
    });
  }

  setEstado(nroPublicacion: number, estadoPublicacion: string): void {
    if (estadoPublicacion === 'F') {
      const ref = this._dialog.open(ModalFinalizarPublicacion, {
        width: '480px',
        disableClose: true
      });
      ref.afterClosed().subscribe((result: boolean) => {
        if (result) {
          this.actualizarEstado(nroPublicacion, estadoPublicacion);
        }
      });
      return;
    }
    this.actualizarEstado(nroPublicacion, estadoPublicacion);
  }

  private actualizarEstado(nroPublicacion: number, estadoPublicacion: string): void {
    this._api.actPublicacion({ nroPublicacion, estadoPublicacion }).subscribe(() => {
      this.listado.update((publicaciones: IPublicacion[]) => publicaciones.map(p =>
        p.nroPublicacion === nroPublicacion ? { ...p, estadoPublicacion } : p
      ));
    });
  }

}
