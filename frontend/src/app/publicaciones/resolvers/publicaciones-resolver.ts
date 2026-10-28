import { ResolveFn } from '@angular/router';
import { inject } from '@angular/core';
import { PublicacionesResource } from '../api/resources/publicaciones-resource';
import { IPublicacion } from '../api/models/i-publicacion';

export const publicacionesResolver: ResolveFn<IPublicacion[]> = (route, state) => {
  return inject(PublicacionesResource).getPublicaciones();
};
