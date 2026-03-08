import { TestBed } from '@angular/core/testing';

import { PowerService } from './power';
import { provideHttpClient } from '@angular/common/http';

describe('Power', () => {
  let service: PowerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [PowerService, provideHttpClient]
    });
    service = TestBed.inject(PowerService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
