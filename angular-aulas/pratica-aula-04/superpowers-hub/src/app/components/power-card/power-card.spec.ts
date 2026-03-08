import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PowerCard } from './power-card';

describe('PowerCard', () => {
  let component: PowerCard;
  let fixture: ComponentFixture<PowerCard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PowerCard]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PowerCard);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
