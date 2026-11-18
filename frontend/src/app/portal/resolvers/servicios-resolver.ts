import { ResolveFn } from '@angular/router';
import { inject } from '@angular/core';
import { VeterinariasResource } from '../api/resources/veterinarias-resource';
import { IServicio } from '../api/models/i-servicio';

export const serviciosResolver: ResolveFn<IServicio[]> = (route, state) => {
  return inject(VeterinariasResource).getServicios();
};
