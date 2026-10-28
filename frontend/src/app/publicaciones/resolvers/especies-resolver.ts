import { ResolveFn } from '@angular/router';
import { inject } from '@angular/core';
import { PublicacionesResource } from '../api/resources/publicaciones-resource';
import { IEspecie } from '../api/models/i-especie';

export const especiesResolver: ResolveFn<IEspecie[]> = (route, state) => {
  return inject(PublicacionesResource).getEspecies();
};
