import { TestBed } from '@angular/core/testing';
import { RefugiosResource } from './refugios-resource';

describe('RefugiosResource', () => {
  let service: RefugiosResource;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RefugiosResource);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
