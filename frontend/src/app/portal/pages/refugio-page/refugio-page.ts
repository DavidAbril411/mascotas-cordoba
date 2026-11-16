import { Component, inject, input } from '@angular/core';
import { TitleCasePipe } from '@angular/common';
import { Router } from '@angular/router';
import { IRefugio } from '../../api/models/i-refugio';
import { IAdopcion } from '../../api/models/i-adopcion';
import { Navbar } from '../../../core/layouts/navbar/navbar';

@Component({
  imports: [
    TitleCasePipe,
    Navbar
  ],
  selector: 'app-refugio-page',
  styleUrl: './refugio-page.scss',
  templateUrl: './refugio-page.html',
})
export class RefugioPage {

  private _router = inject(Router);

  info = input.required<IRefugio>();
  publicaciones = input.required<IAdopcion[]>();

  back(): void {
    this._router.navigate(['/refugios']);
  }

}
