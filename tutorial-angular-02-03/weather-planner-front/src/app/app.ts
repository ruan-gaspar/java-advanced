import { Component, inject, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { LucideAngularModule } from 'lucide-angular';
import { Moon, Mountain, TreePalm, Bike, Cloud, WashingMachine } from 'lucide-angular';
import { ApiService } from './services/api-service';
import { Card } from './components/card/card';

@Component({
  selector: 'app-root',
  imports: [LucideAngularModule, RouterOutlet, Card],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  washingMachine = WashingMachine;
  moon = Moon;
  mountain = Mountain;
  treePalm = TreePalm;
  bike = Bike;
  cloud = Cloud;
  
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
