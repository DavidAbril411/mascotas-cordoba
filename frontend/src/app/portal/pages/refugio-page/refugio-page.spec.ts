import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RefugioPage } from './refugio-page';

describe('RefugioPage', () => {
  let component: RefugioPage;
  let fixture: ComponentFixture<RefugioPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RefugioPage],
    }).compileComponents();

    fixture = TestBed.createComponent(RefugioPage);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
