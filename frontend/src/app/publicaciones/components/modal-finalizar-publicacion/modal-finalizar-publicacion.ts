import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';

@Component({
  imports: [
    MatDialogModule,
    MatButtonModule,
    MatIconModule
  ],
  selector: 'app-modal-finalizar-publicacion',
  styleUrl: './modal-finalizar-publicacion.scss',
  templateUrl: './modal-finalizar-publicacion.html',
})
export class ModalFinalizarPublicacion {

  constructor(private _dialogRef: MatDialogRef<ModalFinalizarPublicacion, boolean>) { }

  ok(): void {
    this._dialogRef.close(true);
  }

  cancel(): void {
    this._dialogRef.close(false);
  }

}
