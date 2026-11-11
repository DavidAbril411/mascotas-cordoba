import { Routes } from '@angular/router';
import { PublicacionesResource } from './api/resources/publicaciones-resource';
import { publicacionesResolver } from './resolvers/publicaciones-resolver';
import { sesionGuard } from '../core/guards/sesion-guard';

export const routes: Routes = [
  { path: '', loadComponent: () => import('./pages/list-page/list-page').then(m => m.ListPage),
    resolve: {
      listado: publicacionesResolver
    }, canActivate: [sesionGuard], providers: [PublicacionesResource] }
];