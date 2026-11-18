import { TestBed } from '@angular/core/testing';
import { VeterinariasResource } from './veterinarias-resource';

describe('VeterinariasResource', () => {
  let service: VeterinariasResource;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VeterinariasResource);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
