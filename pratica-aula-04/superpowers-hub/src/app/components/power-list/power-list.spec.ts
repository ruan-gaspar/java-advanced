import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PowerList } from './power-list';

describe('PowersList', () => {
  let component: PowerList;
  let fixture: ComponentFixture<PowerList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PowerList]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PowerList);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
