import { TestBed } from '@angular/core/testing';
import { ResolveFn } from '@angular/router';
import { publicacionesResolver } from './publicaciones-resolver';

describe('publicacionesResolver', () => {
  const executeResolver: ResolveFn<boolean> = (...resolverParameters) =>
    TestBed.runInInjectionContext(() => publicacionesResolver(...resolverParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeResolver).toBeTruthy();
  });
});
