import { ComponentFixture, TestBed } from '@angular/core/testing';
import { VeterinariasPage } from './veterinarias-page';

describe('VeterinariasPage', () => {
  let component: VeterinariasPage;
  let fixture: ComponentFixture<VeterinariasPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VeterinariasPage],
    }).compileComponents();

    fixture = TestBed.createComponent(VeterinariasPage);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
