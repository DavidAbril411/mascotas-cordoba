import { Component, inject, Injector, OnInit, signal, WritableSignal } from '@angular/core';
import { FieldTree, form, FormField, FormRoot, required } from '@angular/forms/signals';
import { Router, RouterLink } from '@angular/router';
import { SesionesResource } from '../../api/resources/sesiones-resource';
import { ILoginData } from '../../api/models/i-login-data';
import { IUsuario, SesionService } from '../../../core/services/sesion-service';
import { AutofocusDirective } from '../../../shared/directives/autofocus';

@Component({
  imports: [
    FormField,
    FormRoot,
    RouterLink,
    AutofocusDirective
  ],
  providers: [
    SesionesResource
  ],
  selector: 'app-login-page',
  styleUrl: './login-page.scss',
  templateUrl: './login-page.html',
})
export class LoginPage implements OnInit {

  private _injector = inject(Injector);
  private _router = inject(Router);
  private _api = inject(SesionesResource);
  private _sesion = inject(SesionService);

  login!: WritableSignal<ILoginData>;

  loginForm!: FieldTree<ILoginData>;

  ngOnInit(): void {
    this.login = signal<ILoginData>({ cuil: '', clave: '' });
    this.initForm();
  }

  iniciarSesion(): void {
    if (this.loginForm().valid()) {
      this._api.iniciarSesion(this.login()).subscribe((usuario: IUsuario) => {
        this._sesion.iniciar(usuario);
        this._router.navigate(['/publicaciones']);
      });
    }
  }

  private initForm(): void {
    this.loginForm = form(this.login, (schemaPath) => {
      required(schemaPath.cuil);
      required(schemaPath.clave);
    }, {
      injector: this._injector,
      submission: {
        action: async () => {
          this.iniciarSesion()
        },
        onInvalid: () => {
          this.loginForm().markAsTouched();
        }
      }
    });
  }

}
