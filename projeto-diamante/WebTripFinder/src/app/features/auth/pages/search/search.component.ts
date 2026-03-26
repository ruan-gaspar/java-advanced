import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
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

  form = this.fb.group({
    query: ['', [Validators.required]],
    city: ['']
  });

  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const query = this.form.value.query?.trim() ?? '';
    const city = this.form.value.city?.trim() ?? '';

    this.loading.set(true);
    this.errorMessage.set('');
    this.results.set([]);

    if (city) {
      this.placeService.searchPlaces(query, city).subscribe({
        next: (response) => {
          this.results.set(response);
          this.loading.set(false);
        },
        error: () => {
          this.errorMessage.set('Erro ao buscar lugares');
          this.loading.set(false);
        }
      });
      return;
    }

    navigator.geolocation.getCurrentPosition(
      (position: GeolocationPosition) => {
        this.placeService.searchNearby(
          position.coords.latitude,
          position.coords.longitude,
          query
        ).subscribe({
          next: (response) => {
            this.results.set(response);
            this.loading.set(false);
          },
          error: () => {
            this.errorMessage.set('Erro ao buscar lugares próximos');
            this.loading.set(false);
          }
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
}
