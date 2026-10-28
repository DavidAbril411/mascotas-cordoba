import { inject, Service } from '@angular/core';
import { environment } from '../../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IEspecie } from '../models/i-especie';
import { IPublicacion } from '../models/i-publicacion';
import { IPublicacionData } from '../models/i-publicacion-data';

@Service()
export class PublicacionesResource {

  private readonly _baseUrl = `${environment.apiUrl}/publicaciones`;
  private _http: HttpClient = inject(HttpClient);

  getEspecies(): Observable<IEspecie[]> {
    return this._http.get<IEspecie[]>(`${this._baseUrl}/especies`);
  }

  getPublicaciones(): Observable<IPublicacion[]> {
    return this._http.get<IPublicacion[]>(`${this._baseUrl}/listado`);
  }

  insertarPublicacion(publicacion: IPublicacionData): Observable<number> {
    return this._http.post<number>(`${this._baseUrl}/publicacion`, publicacion);
  }

  actPublicacion(nroPublicacion: number, estadoPublicacion: string): Observable<void> {
    return this._http.post<void>(`${this._baseUrl}/publicacion/${nroPublicacion}/${estadoPublicacion}`, null);
  }

}
