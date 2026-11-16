import { ResolveFn } from '@angular/router';
import { inject } from '@angular/core';
import { VeterinariasResource } from '../api/resources/veterinarias-resource';
import { IVeterinaria } from '../api/models/i-veterinaria';

export const veterinariasResolver: ResolveFn<IVeterinaria[]> = (route, state) => {
  return inject(VeterinariasResource).getVeterinarias();
};
