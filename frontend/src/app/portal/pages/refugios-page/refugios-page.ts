import { Component, input } from '@angular/core';
import { TitleCasePipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { IRefugio } from '../../api/models/i-refugio';
import { Navbar } from '../../../core/layouts/navbar/navbar';

@Component({
  imports: [
    TitleCasePipe,
    RouterLink,
    Navbar
  ],
  selector: 'app-refugios-page',
  styleUrl: './refugios-page.scss',
  templateUrl: './refugios-page.html',
})
export class RefugiosPage {

  listado = input.required<IRefugio[]>();

}
