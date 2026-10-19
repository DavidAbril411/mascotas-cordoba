import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: 'acceso', loadChildren: () => import('./acceso/acceso.routes').then(m => m.routes) },
  { path: 'publicaciones', loadChildren: () => import('./publicaciones/publicaciones.routes').then(m => m.routes) },
  { path: '**', redirectTo: 'acceso'}
];
