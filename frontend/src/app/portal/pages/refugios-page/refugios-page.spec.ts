import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RefugiosPage } from './refugios-page';

describe('RefugiosPage', () => {
  let component: RefugiosPage;
  let fixture: ComponentFixture<RefugiosPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RefugiosPage],
    }).compileComponents();

    fixture = TestBed.createComponent(RefugiosPage);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
