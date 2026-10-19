import { inject, Service } from '@angular/core';
import { environment } from '../../../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ILoginData } from '../models/i-login-data';
import { IUsuario } from '../../../core/services/sesion-service';

@Service()
export class SesionesResource {

  private readonly _baseUrl = `${environment.apiUrl}/sesiones`;
  private _http: HttpClient = inject(HttpClient);

  iniciarSesion(login: ILoginData): Observable<IUsuario> {
    return this._http.post<IUsuario>(this._baseUrl, login);
  }

}
