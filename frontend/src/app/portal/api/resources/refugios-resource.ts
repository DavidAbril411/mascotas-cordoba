import { inject, Service } from '@angular/core';
import { environment } from '../../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IRefugio } from '../models/i-refugio';
import { IAdopcion } from '../models/i-adopcion';

@Service()
export class RefugiosResource {

  private readonly _baseUrl = `${environment.apiUrl}/refugios`;
  private _http: HttpClient = inject(HttpClient);

  getRefugios(): Observable<IRefugio[]> {
    return this._http.get<IRefugio[]>(`${this._baseUrl}/listado`);
  }

  getRefugio(idRefugio: number): Observable<IRefugio> {
    return this._http.get<IRefugio>(`${this._baseUrl}/${idRefugio}`);
  }

  getPublicacionesActivas(idRefugio: number): Observable<IAdopcion[]> {
    return this._http.get<IAdopcion[]>(`${this._baseUrl}/${idRefugio}/publicaciones`);
  }

}
