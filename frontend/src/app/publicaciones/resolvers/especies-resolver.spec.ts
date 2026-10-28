import { TestBed } from '@angular/core/testing';
import { ResolveFn } from '@angular/router';
import { especiesResolver } from './especies-resolver';

describe('especiesResolver', () => {
  const executeResolver: ResolveFn<boolean> = (...resolverParameters) =>
    TestBed.runInInjectionContext(() => especiesResolver(...resolverParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeResolver).toBeTruthy();
  });
});
