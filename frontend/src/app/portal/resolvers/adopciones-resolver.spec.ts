import { TestBed } from '@angular/core/testing';
import { ResolveFn } from '@angular/router';
import { adopcionesResolver } from './adopciones-resolver';

describe('adopcionesResolver', () => {
  const executeResolver: ResolveFn<boolean> = (...resolverParameters) =>
    TestBed.runInInjectionContext(() => adopcionesResolver(...resolverParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeResolver).toBeTruthy();
  });
});
