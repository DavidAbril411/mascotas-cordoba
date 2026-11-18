import { Component, input, signal } from '@angular/core';
import { TitleCasePipe } from '@angular/common';
import { form, FormField } from '@angular/forms/signals';
import { IVeterinaria } from '../../api/models/i-veterinaria';
import { IServicio } from '../../api/models/i-servicio';
import { Navbar } from '../../../core/layouts/navbar/navbar';
import { FiltrarPorPipe } from '../../../shared/pipes/filtrar-por-pipe';

@Component({
  imports: [
    TitleCasePipe,
    FiltrarPorPipe,
    FormField,
    Navbar
  ],
  selector: 'app-veterinarias-page',
  styleUrl: './veterinarias-page.scss',
  templateUrl: './veterinarias-page.html',
})
export class VeterinariasPage {

  listado = input.required<IVeterinaria[]>();
  servicios = input.required<IServicio[]>();

  filtroNombre = signal<string>('');
  filtroNombreForm = form(this.filtroNombre);

  filtroServicio = signal<string>('');
  filtroServicioForm = form(this.filtroServicio);

}
