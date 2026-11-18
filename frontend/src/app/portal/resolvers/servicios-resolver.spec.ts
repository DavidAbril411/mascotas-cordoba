import { TestBed } from '@angular/core/testing';
import { ResolveFn } from '@angular/router';
import { serviciosResolver } from './servicios-resolver';

describe('serviciosResolver', () => {
  const executeResolver: ResolveFn<boolean> = (...resolverParameters) =>
    TestBed.runInInjectionContext(() => serviciosResolver(...resolverParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeResolver).toBeTruthy();
  });
});
