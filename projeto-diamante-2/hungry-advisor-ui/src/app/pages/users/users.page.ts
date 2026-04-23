import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ApiService } from '../../services/api.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-users',
  standalone: true,
  imports: [CommonModule], // 🔥 ESSENCIAL
  templateUrl: './users.page.html'
})
export class UsersPage {

  private api = inject(ApiService);
  private router = inject(Router);

  users: any[] = [];
loading = true;
  ngOnInit() {
  if (this.users.length === 0) {
    this.loadUsers();
  }
}
loadUsers() {
  this.loading = true;

  this.api.getUsers().subscribe({
    next: data => {
      console.log("USUÁRIOS:", data);
      this.users = data || [];
      this.loading = false;
    },
    error: err => {
      console.error(err);
      this.users = [];
      this.loading = false;
    }
  });
}
  goToRecommendation(id: string) {
    this.router.navigate(['/recommendation', id]);
  }

  formatPrice(value: string): string {
    switch (value) {
      case 'BAIXO': return '$';
      case 'MEDIO': return '$$';
      case 'ALTO': return '$$$';
      default: return value;
    }
  }
}
