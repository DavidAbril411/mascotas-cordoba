import { Component, inject } from '@angular/core';
import { LoaderService } from '../../services/loader-service';

@Component({
  selector: 'app-loader',
  styleUrl: './loader.scss',
  templateUrl: './loader.html',
})
export class Loader {

  private _service = inject(LoaderService);

  readonly isLoading = this._service.loading;

}