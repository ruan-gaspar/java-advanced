import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { FavoriteService } from '../../../../core/services/favorite.service';
import { FavoriteResponse } from '../../../../core/models/favorite.model';

@Component({
  selector: 'app-favorites',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './favorites.component.html'
})
export class FavoritesComponent {
  private favoriteService = inject(FavoriteService);

  favorites = signal<FavoriteResponse[]>([]);
  loading = signal(true);
  message = signal('');

  constructor() {
    this.loadFavorites();
  }

  loadFavorites(): void {
    this.favoriteService.listFavorites().subscribe({
      next: (response) => {
        this.favorites.set(response);
        this.loading.set(false);
      },
      error: () => {
        this.message.set('Erro ao carregar favoritos.');
        this.loading.set(false);
      }
    });
  }

  removeFavorite(externalPlaceId: string): void {
    this.favoriteService.removeFavorite(externalPlaceId).subscribe({
      next: () => {
        this.favorites.update(items =>
          items.filter(item => item.externalPlaceId !== externalPlaceId)
        );
      },
      error: () => {
        this.message.set('Erro ao remover favorito.');
      }
    });
  }
}
