import { inject, Service } from '@angular/core';
import { environment } from '../../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IVeterinaria } from '../models/i-veterinaria';
import { IServicio } from '../models/i-servicio';

@Service()
export class VeterinariasResource {

  private readonly _baseUrl = `${environment.apiUrl}/veterinarias`;
  private _http: HttpClient = inject(HttpClient);

  getVeterinarias(): Observable<IVeterinaria[]> {
    return this._http.get<IVeterinaria[]>(`${this._baseUrl}/listado`);
  }

  getServicios(): Observable<IServicio[]> {
    return this._http.get<IServicio[]>(`${this._baseUrl}/servicios`);
  }

}
