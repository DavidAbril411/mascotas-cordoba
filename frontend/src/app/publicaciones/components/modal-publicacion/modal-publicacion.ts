import { Component, Injector, OnInit, signal, WritableSignal } from '@angular/core';
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { FieldTree, form, FormField, FormRoot, maxLength, min, required } from '@angular/forms/signals';
import { IEspecie } from '../../api/models/i-especie';
import { IPublicacionData } from '../../api/models/i-publicacion-data';
import { AutofocusDirective } from '../../../shared/directives/autofocus';
import { PublicacionesResource } from '../../api/resources/publicaciones-resource';

@Component({
  imports: [
    MatDialogModule,
    MatButtonModule,
    MatIconModule,
    FormField,
    FormRoot,
    AutofocusDirective
  ],
  providers: [
    PublicacionesResource
  ],
  selector: 'app-modal-publicacion',
  styleUrl: './modal-publicacion.scss',
  templateUrl: './modal-publicacion.html',
})
export class ModalPublicacion implements OnInit {

  especies = signal<IEspecie[]>([]);
  publicacion!: WritableSignal<IPublicacionData>;

  pubForm!: FieldTree<IPublicacionData>;

  constructor(
    private _dialogRef: MatDialogRef<ModalPublicacion, boolean>,
    private _api: PublicacionesResource,
    private _injector: Injector
  ) { }

  ngOnInit(): void {
    this.publicacion = signal<IPublicacionData>(this.nuevaPublicacion());
    this.initForm();
    this._api.getEspecies().subscribe((especies: IEspecie[]) => {
      this.especies.set(especies);
    });
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

  save(): void {
    if (this.pubForm().valid()) {
      this._api.insertarPublicacion(this.publicacion()).subscribe(() => {
        this._dialogRef.close(true);
      });
    }
  }

  close(): void {
    this._dialogRef.close();
  }

  private initForm(): void {
    this.pubForm = form(this.publicacion, (schemaPath) => {
      required(schemaPath.nombre, { message: 'Debe informar el nombre.' });
      maxLength(schemaPath.nombre, 100);

      required(schemaPath.codEspecie, { message: 'Debe seleccionar la especie.' });
      required(schemaPath.sexo, { message: 'Debe seleccionar el sexo.' });

      min(schemaPath.edad, 0, { message: 'La edad no puede ser negativa.' });

      maxLength(schemaPath.raza, 255);

      required(schemaPath.caracteristicasMascota, { message: 'Debe informar la descripción general.' });
      maxLength(schemaPath.caracteristicasMascota, 4000);

      required(schemaPath.condicionAdopcion, { message: 'Debe informar las condiciones de adopción.' });
      maxLength(schemaPath.condicionAdopcion, 4000);

      required(schemaPath.foto, { message: 'Debe seleccionar una imagen.' });
    }, {
      injector: this._injector,
      submission: {
        action: async () => {
          this.save()
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
