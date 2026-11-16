import { TestBed } from '@angular/core/testing';
import { ResolveFn } from '@angular/router';
import { datosRefugioResolver } from './datos-refugio-resolver';

describe('datosRefugioResolver', () => {
  const executeResolver: ResolveFn<boolean> = (...resolverParameters) =>
    TestBed.runInInjectionContext(() => datosRefugioResolver(...resolverParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeResolver).toBeTruthy();
  });
});
