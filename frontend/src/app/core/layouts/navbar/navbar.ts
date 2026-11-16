import { Component, inject } from '@angular/core';
import { SlicePipe } from '@angular/common';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { SesionService } from '../../services/sesion-service';

@Component({
  imports: [
    SlicePipe,
    RouterLink,
    RouterLinkActive
  ],
  selector: 'app-navbar',
  styleUrl: './navbar.scss',
  templateUrl: './navbar.html',
})
export class Navbar {

  private _sesion = inject(SesionService);
  private _router = inject(Router);

  readonly usuario = this._sesion.usuario;

  cerrarSesion(): void {
    this._sesion.cerrar();
    this._router.navigate(['/acceso']);
  }

}
