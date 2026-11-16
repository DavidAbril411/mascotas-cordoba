import { ResolveFn } from '@angular/router';
import { inject } from '@angular/core';
import { RefugiosResource } from '../api/resources/refugios-resource';
import { IRefugio } from '../api/models/i-refugio';

export const datosRefugioResolver: ResolveFn<IRefugio> = (route, state) => {
  return inject(RefugiosResource).getRefugio(route.params['id']);
};
