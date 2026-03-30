import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../../core/services/auth.service';
import { PlaceService } from '../../../../core/services/place.service';
import { PlaceSummary } from '../../../../core/models/place.model';

type CategoryCard = {
  label: string;
  value: string;
  imageUrl: string;
  description: string;
};

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterLink, ReactiveFormsModule],
  templateUrl: './home.component.html'
})
export class HomeComponent {
  private authService = inject(AuthService);
  private placeService = inject(PlaceService);
  private router = inject(Router);
  private fb = inject(FormBuilder);

  loadingCitySearch = signal(false);
  loadingNearbySearch = signal(false);
  errorMessage = signal('');
  cityResults = signal<PlaceSummary[]>([]);
  nearbyResults = signal<PlaceSummary[]>([]);

  cityForm = this.fb.group({
    city: ['', [Validators.required]]
  });

  categories = signal<CategoryCard[]>([
    {
      label: 'Museus',
      value: 'museum',
      imageUrl: 'https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?auto=format&fit=crop&w=900&q=80',
      description: 'Acervos, arte e história'
    },
    {
      label: 'Cinemas',
      value: 'cinema',
      imageUrl: 'https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?auto=format&fit=crop&w=900&q=80',
      description: 'Salas de cinema e lazer'
    },
    {
      label: 'Teatros',
      value: 'theatre',
      imageUrl: 'https://images.unsplash.com/photo-1503095396549-807759245b35?auto=format&fit=crop&w=900&q=80',
      description: 'Espetáculos e cultura'
    },
    {
      label: 'Monumentos',
      value: 'monument',
      imageUrl: 'https://images.unsplash.com/photo-1521295121783-8a321d551ad2?auto=format&fit=crop&w=900&q=80',
      description: 'Marcos históricos e turísticos'
    },
    {
      label: 'Parques',
      value: 'park',
      imageUrl: 'https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&w=900&q=80',
      description: 'Natureza e ar livre'
    },
    {
      label: 'Igrejas',
      value: 'church',
      imageUrl: 'https://images.unsplash.com/photo-1519681393784-d120267933ba?auto=format&fit=crop&w=900&q=80',
      description: 'Arquitetura e espiritualidade'
    }
  ]);

  logout(): void {
    this.authService.logout();
    this.router.navigateByUrl('/login');
  }

  searchByCity(category: string): void {
    if (this.cityForm.invalid) {
      this.cityForm.markAllAsTouched();
      this.errorMessage.set('Informe a cidade para realizar a busca.');
      return;
    }

    const city = this.cityForm.value.city?.trim() ?? '';

    this.loadingCitySearch.set(true);
    this.errorMessage.set('');
    this.cityResults.set([]);

    this.placeService.searchPlaces(category, city).subscribe({
      next: (response) => {
        this.cityResults.set(response);
        this.loadingCitySearch.set(false);
      },
      error: (err: HttpErrorResponse) => {
        this.errorMessage.set(this.getSearchErrorMessage(err, false));
        this.loadingCitySearch.set(false);
      }
    });
  }

  searchNearby(category: string): void {
    this.loadingNearbySearch.set(true);
    this.errorMessage.set('');
    this.nearbyResults.set([]);

    navigator.geolocation.getCurrentPosition(
      (position: GeolocationPosition) => {
        this.placeService.searchNearbyByTerm(
          position.coords.latitude,
          position.coords.longitude,
          category
        ).subscribe({
          next: (response) => {
            this.nearbyResults.set(response);
            this.loadingNearbySearch.set(false);
          },
          error: (err: HttpErrorResponse) => {
            this.errorMessage.set(this.getSearchErrorMessage(err, true));
            this.loadingNearbySearch.set(false);
          }
        });
      },
      () => {
        this.errorMessage.set('Não foi possível obter sua localização.');
        this.loadingNearbySearch.set(false);
      }
    );
  }

  openDetail(place: PlaceSummary): void {
    this.router.navigate(['/places', place.id]);
  }

  private getSearchErrorMessage(err: HttpErrorResponse, isNearbySearch: boolean): string {
    const backendMessage = err.error?.message;

    if (backendMessage) {
      return backendMessage;
    }

    if (err.status === 0) {
      return 'Não foi possível conectar ao servidor. Verifique se o backend está rodando.';
    }

    if (err.status === 400) {
      return 'Os parâmetros da busca estão inválidos.';
    }

    if (err.status === 401) {
      return 'Sua sessão expirou. Faça login novamente.';
    }

    if (err.status === 403) {
      return 'Você não tem permissão para acessar esta funcionalidade.';
    }

    if (err.status === 404) {
      return isNearbySearch
        ? 'Nenhum lugar próximo foi encontrado.'
        : 'Nenhum lugar foi encontrado para essa busca.';
    }

    if (err.status >= 500) {
      return 'O servidor teve um problema ao processar sua busca. Tente novamente em instantes.';
    }

    return isNearbySearch
      ? 'Erro ao buscar lugares próximos.'
      : 'Erro ao buscar lugares.';
  }
}
