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

  showDeleteModal = signal(false);
  favoriteToDelete = signal<string | null>(null);

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

  openDeleteModal(externalPlaceId: string): void {
    this.favoriteToDelete.set(externalPlaceId);
    this.showDeleteModal.set(true);
    this.message.set('');
  }

  closeDeleteModal(): void {
    this.favoriteToDelete.set(null);
    this.showDeleteModal.set(false);
  }

  confirmRemoveFavorite(): void {
    const externalPlaceId = this.favoriteToDelete();

    if (!externalPlaceId) {
      return;
    }

    this.favoriteService.removeFavorite(externalPlaceId).subscribe({
      next: () => {
        this.favorites.update(items =>
          items.filter(item => item.externalPlaceId !== externalPlaceId)
        );
        this.message.set('Favorito removido com sucesso.');
        this.closeDeleteModal();
      },
      error: () => {
        this.message.set('Erro ao remover favorito.');
        this.closeDeleteModal();
      }
    });
  }
}
