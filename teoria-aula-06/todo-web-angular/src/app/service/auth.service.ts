import { Injectable } from "@angular/core";

@Injectable({providedIn: 'root'})

export class AuthService {

  login(token: string) {
    localStorage.setItem('token', token); // cuidado XSS
  }

  logout(){
    localStorage.removeItem('token');
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  isAuthenticated(): boolean {
    return this.getToken() !== null;
  }

}
