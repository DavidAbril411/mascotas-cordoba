import { TestBed } from '@angular/core/testing';
import { ResolveFn } from '@angular/router';
import { veterinariasResolver } from './veterinarias-resolver';

describe('veterinariasResolver', () => {
  const executeResolver: ResolveFn<boolean> = (...resolverParameters) =>
    TestBed.runInInjectionContext(() => veterinariasResolver(...resolverParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeResolver).toBeTruthy();
  });
});
