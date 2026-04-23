import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { PlaceDetail, PlaceSummary } from '../models/place.model';

@Injectable({
  providedIn: 'root'
})
export class PlaceService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiUrl}/places`;

  searchPlaces(query: string, city: string, limit = 10, category?: string): Observable<PlaceSummary[]> {
    let params = new HttpParams()
      .set('query', query.trim())
      .set('city', city.trim())
      .set('limit', limit);

    if (category?.trim()) {
      params = params.set('category', category.trim());
    }

    return this.http.get<PlaceSummary[]>(`${this.apiUrl}/search`, { params });
  }

  searchNearby(latitude: number, longitude: number, radius = 5000, limit = 10, category?: string): Observable<PlaceSummary[]> {
    let params = new HttpParams()
      .set('latitude', latitude)
      .set('longitude', longitude)
      .set('radius', radius)
      .set('limit', limit);

    if (category?.trim()) {
      params = params.set('category', category.trim());
    }

    return this.http.get<PlaceSummary[]>(`${this.apiUrl}/nearby`, { params });
  }

  searchNearbyByTerm(
    latitude: number,
    longitude: number,
    query: string,
    radius = 5000,
    limit = 10,
    category?: string
  ): Observable<PlaceSummary[]> {
    let params = new HttpParams()
      .set('latitude', latitude)
      .set('longitude', longitude)
      .set('query', query.trim())
      .set('radius', radius)
      .set('limit', limit);

    if (category?.trim()) {
      params = params.set('category', category.trim());
    }

    return this.http.get<PlaceSummary[]>(`${this.apiUrl}/nearby/search`, { params });
  }

  getPlaceDetails(id: string): Observable<PlaceDetail> {
    return this.http.get<PlaceDetail>(`${this.apiUrl}/${id}`);
  }
}
