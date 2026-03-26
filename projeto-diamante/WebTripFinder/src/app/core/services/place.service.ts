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

  searchPlaces(query: string, city?: string, limit = 10): Observable<PlaceSummary[]> {
    let params = new HttpParams()
      .set('query', query)
      .set('limit', limit);

    if (city?.trim()) {
      params = params.set('city', city.trim());
    }

    return this.http.get<PlaceSummary[]>(`${this.apiUrl}/search`, { params });
  }

  searchNearby(latitude: number, longitude: number, query?: string, limit = 10): Observable<PlaceSummary[]> {
    let params = new HttpParams()
      .set('latitude', latitude)
      .set('longitude', longitude)
      .set('radius', 5000)
      .set('limit', limit);

    if (query?.trim()) {
      params = params.set('query', query.trim());
      return this.http.get<PlaceSummary[]>(`${this.apiUrl}/nearby/search`, { params });
    }

    return this.http.get<PlaceSummary[]>(`${this.apiUrl}/nearby`, { params });
  }

  getPlaceDetails(id: string): Observable<PlaceDetail> {
    return this.http.get<PlaceDetail>(`${this.apiUrl}/${id}`);
  }
}
