import { Routes } from '@angular/router';

export const routes: Routes = [
  { path: '', loadComponent: () => import('./pages/list-page/list-page').then(m => m.ListPage) }
];
