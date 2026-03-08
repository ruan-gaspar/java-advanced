import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { Pet } from '../models/pet';

export interface PetResponse {
  _embedded: { pets: Pet[] };
}

@Injectable({
  providedIn: 'root',
})
export class PetService {
  private http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8080/pets';

  getPets() {
    return this.http.get<PetResponse>(this.apiUrl).pipe(
      map(res => res._embedded.pets)
    );
  }
}
