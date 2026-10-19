import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { IMessage } from '../../services/app-message-service';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

@Component({
  imports: [
    MatDialogModule,
    MatButtonModule,
    MatIconModule
  ],
  selector: 'app-modal-message',
  styleUrl: './modal-message.scss',
  templateUrl: './modal-message.html',
})
export class ModalMessage implements OnInit {

  title!: string;

  constructor(@Inject(MAT_DIALOG_DATA) public message: IMessage, public ref: MatDialogRef<ModalMessage>) {}
  
  ngOnInit(): void {
    this.title = this.message?.title || 'Mensaje de error';
  }

}
