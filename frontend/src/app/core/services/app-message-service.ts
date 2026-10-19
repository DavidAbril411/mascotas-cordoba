import { inject, Service } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ModalMessage } from '../layouts/modal-message/modal-message';

export interface IMessage {
  text: string;
  num?: number;
  title?: string;
}

@Service()
export class AppMessageService {
  
  private dialog = inject(MatDialog);

  showMessage(message: IMessage): void {
    this.dialog.open(ModalMessage, {
      data: message,
      width: '380px',
      disableClose: true,
      autoFocus: false,
    });
  }

}

