import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class ApiService {
  private http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8080/api/games';

  getRecommendation(game: string) {
    return this.http.get<{
      result: string,
      rating: number,
      reviews: number,
      released: string,
      image: string
    }>(`${this.apiUrl}?game=${game}`);
  }
}
