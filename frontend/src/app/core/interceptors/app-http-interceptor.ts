import { HttpInterceptorFn } from '@angular/common/http';
import { LoaderService } from '../services/loader-service';
import { inject } from '@angular/core';
import { finalize, takeUntil } from 'rxjs';
import { HttpCancelService } from '../services/http-cancel-service';
import { SesionService } from '../services/sesion-service';
import { environment } from '../../../environments/environment';

export const appHttpInterceptor: HttpInterceptorFn = (req, next) => {
  const cancel = inject(HttpCancelService);
  const loader = inject(LoaderService);
  const usuario = inject(SesionService).usuario();

  loader.start();

  const apiReq = req.clone({
    setHeaders: usuario ? {
      'X-API-Key': environment.apiKey,
      'Authorization': `Bearer ${usuario.token}`
    } : {
      'X-API-Key': environment.apiKey
    }
  });

  return next(apiReq).pipe(
    takeUntil(cancel.pendingHTTPRequests$),
    finalize(() => loader.complete())
  );
};
