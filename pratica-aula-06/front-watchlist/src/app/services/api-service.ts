import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface toWatch {
  id: number;
  name: string;
  watched: boolean;
}


@Injectable({
  providedIn: 'root',
})
export class ApiService {
  private http = inject(HttpClient);
  private baseUrl = 'http://localhost:8080/api/to-watch';

  getAll(): Observable<toWatch[]> {
    return this.http.get<toWatch[]>(this.baseUrl);
  }
}
