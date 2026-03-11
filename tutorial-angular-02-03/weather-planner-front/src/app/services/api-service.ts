import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class ApiService {
  private http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8080/api/planner';

  getPlanner(activity: string) {
    return this.http.get<{ result: string }>(
      `${this.apiUrl}?activity=${activity}`
    );
  }
}
