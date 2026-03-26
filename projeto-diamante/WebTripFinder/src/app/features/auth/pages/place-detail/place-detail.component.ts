import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { PlaceService } from '../../../../core/services/place.service';
import { FavoriteService } from '../../../../core/services/favorite.service';
import { PlaceDetail } from '../../../../core/models/place.model';

@Component({
  selector: 'app-place-detail',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './place-detail.component.html'
})
export class PlaceDetailComponent {
  private route = inject(ActivatedRoute);
  private placeService = inject(PlaceService);
  private favoriteService = inject(FavoriteService);

  place = signal<PlaceDetail | null>(null);
  loading = signal(true);
  message = signal('');

  constructor() {
    const id = this.route.snapshot.paramMap.get('id');

    if (!id) {
      this.loading.set(false);
      return;
    }

    this.placeService.getPlaceDetails(id).subscribe({
      next: (response) => {
        this.place.set(response);
        this.loading.set(false);
      },
      error: () => {
        this.message.set('Erro ao carregar detalhes do lugar.');
        this.loading.set(false);
      }
    });
  }

  saveFavorite(): void {
    const place = this.place();
    if (!place) return;

    this.favoriteService.addFavorite({
      id: place.id,
      name: place.name,
      category: place.category,
      address: place.address,
      city: place.city,
      country: place.country,
      latitude: place.latitude,
      longitude: place.longitude,
      imageUrl: place.imageUrl
    }).subscribe({
      next: () => this.message.set('Lugar salvo nos favoritos com sucesso!'),
      error: () => this.message.set('Não foi possível salvar nos favoritos.')
    });
  }
}
