import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { User } from '../models/user.model';
import { Restaurant } from '../models/restaurant.model';
import { Recommendation } from '../models/recommendation.model';

@Injectable({ providedIn: 'root' })
export class ApiService {

  private http = inject(HttpClient);

  getUsers() {
    return this.http.get<User[]>('http://localhost:8080/api/users');
  }

  getRestaurants() {
    return this.http.get<Restaurant[]>('http://localhost:8080/api/restaurants');
  }

  getRecommendation(userId: string) {
    return this.http.get<Recommendation>(`http://localhost:8080/api/recommendations/${userId}`);
  }

  getRecommendationAI(userId: string) {
    return this.http.get<Recommendation>(`http://localhost:8080/api/recommendations/${userId}/ai`);
  }
}
