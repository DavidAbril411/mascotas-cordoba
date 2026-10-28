import { Routes } from '@angular/router';
import { PublicacionesResource } from './api/resources/publicaciones-resource';
import { publicacionesResolver } from './resolvers/publicaciones-resolver';
import { especiesResolver } from './resolvers/especies-resolver';
import { sesionGuard } from '../core/guards/sesion-guard';

export const routes: Routes = [
  { path: '', loadComponent: () => import('./pages/list-page/list-page').then(m => m.ListPage),
    resolve: {
      listado: publicacionesResolver,
      especies: especiesResolver
    }, canActivate: [sesionGuard], providers: [PublicacionesResource] }
];
