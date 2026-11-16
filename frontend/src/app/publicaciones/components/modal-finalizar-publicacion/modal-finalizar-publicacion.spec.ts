import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ModalFinalizarPublicacion } from './modal-finalizar-publicacion';

describe('ModalFinalizarPublicacion', () => {
  let component: ModalFinalizarPublicacion;
  let fixture: ComponentFixture<ModalFinalizarPublicacion>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ModalFinalizarPublicacion],
    }).compileComponents();

    fixture = TestBed.createComponent(ModalFinalizarPublicacion);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
