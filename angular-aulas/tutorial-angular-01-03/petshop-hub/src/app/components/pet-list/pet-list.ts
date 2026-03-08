import { Component } from '@angular/core';
import { PetService } from '../../services/pet-service';
import { inject } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';

@Component({
  selector: 'app-pet-list',
  imports: [],
  templateUrl: './pet-list.html',
  styleUrl: './pet-list.css',
})
export class PetList {
  private petService = inject(PetService);

  pets = toSignal(this.petService.getPets(), { initialValue: [] });
}
