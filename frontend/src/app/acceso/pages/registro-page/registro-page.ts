import { Component, inject, Injector, OnInit, signal, WritableSignal } from '@angular/core';
import { email, FieldTree, form, FormField, FormRoot, maxLength, minLength, required, validate } from '@angular/forms/signals';
import { Router, RouterLink } from '@angular/router';
import { RefugiosResource } from '../../api/resources/refugios-resource';
import { IRefugioData } from '../../api/models/i-refugio-data';
import { AppMessageService } from '../../../core/services/app-message-service';
import { AutofocusDirective } from '../../../shared/directives/autofocus';

@Component({
  imports: [
    FormField,
    FormRoot,
    RouterLink,
    AutofocusDirective
  ],
  providers: [
    RefugiosResource
  ],
  selector: 'app-registro-page',
  styleUrl: './registro-page.scss',
  templateUrl: './registro-page.html',
})
export class RegistroPage implements OnInit {

  private _injector = inject(Injector);
  private _router = inject(Router);
  private _api = inject(RefugiosResource);
  private _message = inject(AppMessageService);

  refugio!: WritableSignal<IRefugioData>;

  refForm!: FieldTree<IRefugioData>;

  ngOnInit(): void {
    this.refugio = signal<IRefugioData>(this.nuevoRefugio());
    this.initForm();
  }

  save(): void {
    if (this.refForm().valid()) {
      this._api.registrarRefugio(this.refugio()).subscribe(() => {
        this._message.showMessage({
          title: 'Refugio registrado',
          text: 'Ya podés iniciar sesión con tu CUIL y contraseña.'
        });
        this._router.navigate(['/acceso']);
      });
    }
  }

  private initForm(): void {
    this.refForm = form(this.refugio, (schemaPath) => {
      required(schemaPath.razonSocial);
      maxLength(schemaPath.razonSocial, 255);

      required(schemaPath.correo);
      email(schemaPath.correo);
      maxLength(schemaPath.correo, 255);

      maxLength(schemaPath.telefono, 20);
      maxLength(schemaPath.domicilio, 255);

      required(schemaPath.habilitacionMunicipal);
      maxLength(schemaPath.habilitacionMunicipal, 20);

      required(schemaPath.responsable.apellido);
      maxLength(schemaPath.responsable.apellido, 40);

      required(schemaPath.responsable.nombre);
      maxLength(schemaPath.responsable.nombre, 100);

      required(schemaPath.responsable.cuil);
      validate(schemaPath.responsable.cuil, ({ value }) => {
        if (value() && !/^\d{11}$/.test(value())) {
          return {
            kind: 'cuil',
            message: 'El CUIL debe tener 11 números, sin guiones.'
          };
        }
        return null;
      });

      required(schemaPath.responsable.correo);
      email(schemaPath.responsable.correo);
      maxLength(schemaPath.responsable.correo, 255);

      maxLength(schemaPath.responsable.telefono, 20);
      maxLength(schemaPath.responsable.domicilio, 255);

      required(schemaPath.responsable.clave);
      minLength(schemaPath.responsable.clave, 6);
      maxLength(schemaPath.responsable.clave, 20);

      required(schemaPath.responsable.confirmarClave);
      validate(schemaPath.responsable.confirmarClave, ({ value, valueOf }) => {
        if (value() !== valueOf(schemaPath.responsable.clave)) {
          return {
            kind: 'passwordMismatch',
            message: 'Las contraseñas no coinciden.'
          };
        }
        return null;
      });
    }, {
      injector: this._injector,
      submission: {
        action: async () => {
          this.save()
        },
        onInvalid: () => {
          this.refForm().markAsTouched();
        }
      }
    });
  }

  private nuevoRefugio(): IRefugioData {
    return {
      razonSocial: '',
      correo: '',
      telefono: '',
      domicilio: '',
      habilitacionMunicipal: '',
      responsable: {
        apellido: '',
        nombre: '',
        cuil: '',
        clave: '',
        confirmarClave: '',
        correo: '',
        telefono: '',
        domicilio: ''
      }
    }
  }

}
