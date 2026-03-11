import { Component, inject, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { LucideAngularModule } from 'lucide-angular';
import { ApiService } from './services/api-service';
@Component({
  selector: 'app-root',
  imports: [LucideAngularModule, RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('weather-planner-front');
  readonly WashingMachine = 'WashingMachine';

  private readonly apiService = inject(ApiService);
  protected readonly plannerResponse = signal('');

  handleCardClick(activity: string): void {
      this.apiService.getPlanner(activity).subscribe({
      next: response => this.plannerResponse.set(response.result),
      error: () => this.plannerResponse.set('Erro ao consultar API')
    });
  }
}
