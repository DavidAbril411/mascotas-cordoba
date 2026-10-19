import { TestBed } from '@angular/core/testing';
import { SesionesResource } from './sesiones-resource';

describe('SesionesResource', () => {
  let service: SesionesResource;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SesionesResource);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
