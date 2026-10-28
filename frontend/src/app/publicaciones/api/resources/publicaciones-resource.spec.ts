import { TestBed } from '@angular/core/testing';
import { PublicacionesResource } from './publicaciones-resource';

describe('PublicacionesResource', () => {
  let service: PublicacionesResource;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PublicacionesResource);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
