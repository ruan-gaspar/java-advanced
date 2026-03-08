import { Component, inject } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { SectionTitle } from './components/section-title/section-title';
import { PetCard } from './components/pet-card/pet-card';
import { PetService } from './services/pet-service';
import { toSignal } from '@angular/core/rxjs-interop';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, SectionTitle, PetCard],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  private petService = inject(PetService);
  pets = toSignal(this.petService.getPets(), { initialValue: [] });
}
