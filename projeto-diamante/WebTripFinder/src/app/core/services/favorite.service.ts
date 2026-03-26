import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { FavoriteRequest, FavoriteResponse } from '../models/favorite.model';

@Injectable({
  providedIn: 'root'
})
export class FavoriteService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/favorites`;

  listFavorites(): Observable<FavoriteResponse[]> {
    return this.http.get<FavoriteResponse[]>(this.apiUrl);
  }

  addFavorite(payload: FavoriteRequest): Observable<FavoriteResponse> {
    return this.http.post<FavoriteResponse>(this.apiUrl, payload);
  }

  removeFavorite(externalPlaceId: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${externalPlaceId}`);
  }
}
