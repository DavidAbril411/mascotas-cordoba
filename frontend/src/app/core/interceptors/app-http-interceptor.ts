import { HttpInterceptorFn } from '@angular/common/http';
import { LoaderService } from '../services/loader-service';
import { inject } from '@angular/core';
import { finalize, takeUntil } from 'rxjs';
import { HttpCancelService } from '../services/http-cancel-service';
import { environment } from '../../../environments/environment';

export const appHttpInterceptor: HttpInterceptorFn = (req, next) => {
  const cancel = inject(HttpCancelService);
  const loader = inject(LoaderService);

  loader.start();

  const apiReq = req.clone({
    setHeaders: {
      'X-API-Key': environment.apiKey
    }
  });

  return next(apiReq).pipe(
    takeUntil(cancel.pendingHTTPRequests$),
    finalize(() => loader.complete())
  );
};
