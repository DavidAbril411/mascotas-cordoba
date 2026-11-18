import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { Navbar } from '../../../core/layouts/navbar/navbar';

@Component({
  imports: [
    RouterLink,
    Navbar
  ],
  selector: 'app-inicio-page',
  styleUrl: './inicio-page.scss',
  templateUrl: './inicio-page.html',
})
export class InicioPage {}
