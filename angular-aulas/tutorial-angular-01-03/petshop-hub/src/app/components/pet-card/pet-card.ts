import { Component, input } from '@angular/core';
import { Pet } from '../../models/pet';

@Component({
  selector: 'app-pet-card',
  standalone: true,
  imports: [],
  templateUrl: './pet-card.html',
  styleUrl: './pet-card.css',
})
export class PetCard {
  pet = input.required<Pet>();
}
