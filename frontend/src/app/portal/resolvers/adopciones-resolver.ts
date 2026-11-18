import { ResolveFn } from '@angular/router';
import { inject } from '@angular/core';
import { RefugiosResource } from '../api/resources/refugios-resource';
import { IAdopcion } from '../api/models/i-adopcion';

export const adopcionesResolver: ResolveFn<IAdopcion[]> = (route, state) => {
  return inject(RefugiosResource).getPublicacionesActivas(route.params['id']);
};
