import { Component, computed, input } from '@angular/core';
import {output } from '@angular/core';
import {
  LucideAngularModule,
  TreePalm,
  Mountain,
  Bike,
  Cloud,
  WashingMachine,
  Moon
} from 'lucide-angular';

const icons = {
  washingMachine: WashingMachine,
  cloud: Cloud,
  palmTree: TreePalm,
  mountain: Mountain,
  bike: Bike,
  moon: Moon
} as const;

export type CardIcon = keyof typeof icons;

@Component({
  selector: 'card',
  standalone: true,
  imports: [LucideAngularModule],
  templateUrl: './card.html',
  styleUrl: './card.css',
})
export class Card {
  readonly title = input.required<string>();
  readonly icon = input.required<CardIcon>();
  
  readonly iconImg = computed(() => icons[this.icon()]);

  readonly activityClick = output<string>();

  onCardClick(): void {
    this.activityClick.emit(this.title());
  }
}
