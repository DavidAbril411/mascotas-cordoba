import { Component, Inject, Injector, OnInit, signal, WritableSignal } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { FieldTree, form, FormField, FormRoot, maxLength, min, required } from '@angular/forms/signals';
import { IEspecie } from '../../api/models/i-especie';
import { IPublicacionData } from '../../api/models/i-publicacion-data';
import { AutofocusDirective } from '../../../shared/directives/autofocus';

@Component({
  imports: [
    MatDialogModule,
    MatButtonModule,
    MatIconModule,
    FormField,
    FormRoot,
    AutofocusDirective
  ],
  selector: 'app-modal-publicacion',
  styleUrl: './modal-publicacion.scss',
  templateUrl: './modal-publicacion.html',
})
export class ModalPublicacion implements OnInit {

  publicacion!: WritableSignal<IPublicacionData>;

  pubForm!: FieldTree<IPublicacionData>;

  constructor(
    @Inject(MAT_DIALOG_DATA) public especies: IEspecie[],
    private _dialogRef: MatDialogRef<ModalPublicacion, IPublicacionData>,
    private _injector: Injector
  ) { }

  ngOnInit(): void {
    this.publicacion = signal<IPublicacionData>(this.nuevaPublicacion());
    this.initForm();
  }

  setFoto(event: Event): void {
    const archivo = (event.target as HTMLInputElement).files?.[0];
    if (!archivo) {
      this.publicacion.update(publicacion => ({
        ...publicacion,
        foto: ''
      }));
      return;
    }

    const reader = new FileReader();
    reader.onload = () => {
      this.publicacion.update(publicacion => ({
        ...publicacion,
        foto: reader.result as string
      }));
    };
    reader.readAsDataURL(archivo);
  }

  ok(): void {
    if (this.pubForm().valid()) {
      this._dialogRef.close(this.publicacion());
    }
  }

  close(): void {
    this._dialogRef.close();
  }

  private initForm(): void {
    this.pubForm = form(this.publicacion, (schemaPath) => {
      required(schemaPath.nombre);
      maxLength(schemaPath.nombre, 100);

      required(schemaPath.codEspecie);
      required(schemaPath.sexo);

      min(schemaPath.edad, 0);

      maxLength(schemaPath.raza, 255);

      required(schemaPath.caracteristicasMascota);
      maxLength(schemaPath.caracteristicasMascota, 4000);

      required(schemaPath.condicionAdopcion);
      maxLength(schemaPath.condicionAdopcion, 4000);

      required(schemaPath.foto);
    }, {
      injector: this._injector,
      submission: {
        action: async () => {
          this.ok()
        },
        onInvalid: () => {
          this.pubForm().markAsTouched();
        }
      }
    });
  }

  private nuevaPublicacion(): IPublicacionData {
    return {
      nombre: '',
      codEspecie: '',
      sexo: '',
      edad: null,
      raza: '',
      caracteristicasMascota: '',
      condicionAdopcion: '',
      foto: ''
    }
  }

}
