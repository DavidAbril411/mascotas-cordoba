import { inject, Service } from '@angular/core';
import { environment } from '../../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IRefugioData } from '../models/i-refugio-data';

@Service()
export class RefugiosResource {

  private readonly _baseUrl = `${environment.apiUrl}/refugios`;
  private _http: HttpClient = inject(HttpClient);

  registrarRefugio(refugio: IRefugioData): Observable<number> {
    return this._http.post<number>(this._baseUrl, refugio);
  }

}
