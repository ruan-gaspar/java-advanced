import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';
import { environment } from '../../../environments/environment';
import { AuthResponse, LoginRequest, RegisterRequest, UpdateUserRequest } from '../models/auth.models';
import { StorageService } from './storage.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private http = inject(HttpClient);
  private storageService = inject(StorageService);
  private apiUrl = `${environment.apiUrl}/auth`;

  login(payload: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, payload).pipe(
      tap(response => {
        if (response.token) {
          this.storageService.setToken(response.token);
        }
      })
    );
  }

  register(payload: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/register`, payload).pipe(
      tap(response => {
        if (response.token) {
          this.storageService.setToken(response.token);
        }
      })
    );
  }

  me(): Observable<AuthResponse> {
    return this.http.get<AuthResponse>(`${this.apiUrl}/me`);
  }

  updateMe(payload: UpdateUserRequest): Observable<AuthResponse> {
    return this.http.put<AuthResponse>(`${this.apiUrl}/me`, payload);
  }

  logout(): void {
    this.storageService.removeToken();
  }

  getToken(): string | null {
    return this.storageService.getToken();
  }

  isAuthenticated(): boolean {
    return this.storageService.hasToken();
  }
}
