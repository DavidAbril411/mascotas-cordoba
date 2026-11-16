import { ApplicationConfig, ErrorHandler, provideBrowserGlobalErrorListeners } from '@angular/core';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { routes } from './app.routes';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { appHttpInterceptor } from './core/interceptors/app-http-interceptor';
import { provideNativeDateAdapter } from '@angular/material/core';
import { AppErrorHandler } from './core/handlers/app-error-handler';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideRouter(routes,
      withComponentInputBinding()
    ),
    provideHttpClient(
      withInterceptors([appHttpInterceptor])
    ),
    provideNativeDateAdapter(),
    { provide: ErrorHandler, useClass: AppErrorHandler }
  ]
};
