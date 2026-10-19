import { Component, inject } from '@angular/core';
import { SesionService } from '../../../core/services/sesion-service';

@Component({
  imports: [],
  selector: 'app-list-page',
  styleUrl: './list-page.scss',
  templateUrl: './list-page.html',
})
export class ListPage {

  private _sesion = inject(SesionService);

  readonly usuario = this._sesion.usuario;

}
