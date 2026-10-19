import { Routes } from '@angular/router';
import { SesionesResource } from './api/resources/sesiones-resource';
import { RefugiosResource } from './api/resources/refugios-resource';

export const routes: Routes = [
  { path: '', loadComponent: () => import('./pages/login-page/login-page').then(m => m.LoginPage),
    providers: [SesionesResource] },
  { path: 'registro', loadComponent: () => import('./pages/registro-page/registro-page').then(m => m.RegistroPage),
    providers: [RefugiosResource] }
];
