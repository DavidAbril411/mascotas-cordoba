import { Routes } from '@angular/router';
import { VeterinariasResource } from './api/resources/veterinarias-resource';
import { RefugiosResource } from './api/resources/refugios-resource';
import { veterinariasResolver } from './resolvers/veterinarias-resolver';
import { serviciosResolver } from './resolvers/servicios-resolver';
import { refugiosResolver } from './resolvers/refugios-resolver';
import { datosRefugioResolver } from './resolvers/datos-refugio-resolver';
import { adopcionesResolver } from './resolvers/adopciones-resolver';

export const routes: Routes = [
  { path: '', redirectTo: 'inicio', pathMatch: 'full' },
  { path: 'inicio', loadComponent: () => import('./pages/inicio-page/inicio-page').then(m => m.InicioPage) },
  { path: 'veterinarias', loadComponent: () => import('./pages/veterinarias-page/veterinarias-page').then(m => m.VeterinariasPage),
    resolve: {
      listado: veterinariasResolver,
      servicios: serviciosResolver
    }, providers: [VeterinariasResource] },
  { path: 'refugios', loadComponent: () => import('./pages/refugios-page/refugios-page').then(m => m.RefugiosPage),
    resolve: {
      listado: refugiosResolver
    }, providers: [RefugiosResource] },
  { path: 'refugios/:id', loadComponent: () => import('./pages/refugio-page/refugio-page').then(m => m.RefugioPage),
    resolve: {
      info: datosRefugioResolver,
      publicaciones: adopcionesResolver
    }, providers: [RefugiosResource] }
];
