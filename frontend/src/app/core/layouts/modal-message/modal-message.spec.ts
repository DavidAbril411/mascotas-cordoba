import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ModalMessage } from './modal-message';

describe('ModalMessage', () => {
  let component: ModalMessage;
  let fixture: ComponentFixture<ModalMessage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ModalMessage],
    }).compileComponents();

    fixture = TestBed.createComponent(ModalMessage);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
