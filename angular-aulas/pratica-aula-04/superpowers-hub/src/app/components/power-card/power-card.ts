import { Component, input } from '@angular/core';

@Component({
  selector: 'app-power-card',
  standalone: true,
  imports: [PowerCard],
  templateUrl: './power-card.html',
  styleUrl: './power-card.css',
})
export class PowerCard {
  nome = input<string>();
  descricao = input<string>();
  nivel_inutilidade = input<number>();
}
