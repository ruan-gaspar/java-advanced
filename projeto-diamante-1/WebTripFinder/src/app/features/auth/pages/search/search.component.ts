import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { PlaceService } from '../../../../core/services/place.service';
import { PlaceSummary } from '../../../../core/models/place.model';

@Component({
  selector: 'app-search',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './search.component.html'
})
export class SearchComponent {
  private fb = inject(FormBuilder);
  private placeService = inject(PlaceService);
  private router = inject(Router);

  loading = signal(false);
  errorMessage = signal('');
  results = signal<PlaceSummary[]>([]);
  searched = signal(false);
  isExploreMode = signal(false);

  form = this.fb.group({
    query: ['', [Validators.required]],
    city: ['']
  });

  constructor() {
    this.isExploreMode.set(this.router.url.includes('/places/explore'));
  }

  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const query = this.form.value.query?.trim() ?? '';
    const city = this.form.value.city?.trim() ?? '';

    this.searched.set(true);
    this.loading.set(true);
    this.errorMessage.set('');
    this.results.set([]);

    if (!this.isExploreMode() && city) {
      this.placeService.searchPlaces(query, city).subscribe({
        next: (response) => {
          this.results.set(response);
          this.loading.set(false);
        },
        error: (err: HttpErrorResponse) => this.handleSearchError(err, false)
      });
      return;
    }

    navigator.geolocation.getCurrentPosition(
      (position: GeolocationPosition) => {
        const latitude = position.coords.latitude;
        const longitude = position.coords.longitude;

        if (query) {
          this.placeService.searchNearbyByTerm(latitude, longitude, query).subscribe({
            next: (response) => {
              this.results.set(response);
              this.loading.set(false);
            },
            error: (err: HttpErrorResponse) => this.handleSearchError(err, true)
          });
          return;
        }

        this.placeService.searchNearby(latitude, longitude).subscribe({
          next: (response) => {
            this.results.set(response);
            this.loading.set(false);
          },
          error: (err: HttpErrorResponse) => this.handleSearchError(err, true)
        });
      },
      () => {
        this.errorMessage.set('Não foi possível obter sua localização. Informe uma cidade.');
        this.loading.set(false);
      }
    );
  }

  openDetail(place: PlaceSummary): void {
    this.router.navigate(['/places', place.id]);
  }

  private handleSearchError(err: HttpErrorResponse, isNearbySearch: boolean): void {
    this.errorMessage.set(this.getSearchErrorMessage(err, isNearbySearch));
    this.loading.set(false);
  }

  private getSearchErrorMessage(err: HttpErrorResponse, isNearbySearch: boolean): string {
    if (err.status === 0) {
      return 'Não foi possível conectar ao servidor. Verifique se o backend está rodando.';
    }

    if (err.status === 400) {
      return 'Os parâmetros da busca estão inválidos. Revise o termo informado.';
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
