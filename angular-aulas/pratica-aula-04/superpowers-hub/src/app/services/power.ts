import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { Power } from '../models/power.model';

export interface PowerResponse {
  _embedded: { powers: Power[] };
}

@Injectable({
  providedIn: 'root',
})
export class PowerService {
  private http = inject(HttpClient);

  private readonly apiUrl = 'http://localhost:8080/powers';

  getPowers() {
    return this.http.get<PowerResponse>(this.apiUrl).pipe(
      map((res) => res._embedded.powers)
    );
  }
}
