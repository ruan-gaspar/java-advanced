import { Component, computed, input, output } from '@angular/core';
import { LucideAngularModule, FileIcon, Gamepad2 } from 'lucide-angular';

const icons = {
  file: FileIcon,
  gamepad: Gamepad2
} as const;

export type CardIcon = keyof typeof icons;

@Component({
  standalone: true,
  selector: 'card-game',
  imports: [LucideAngularModule],
  templateUrl: './card-game.html',
  styleUrls: ['./card-game.css'],
})
export class CardGame {

  readonly title = input.required<string>();
  readonly icon = input.required<CardIcon>();

  readonly iconImg = computed(() => icons[this.icon()]);

  readonly activityClick = output<string>();

  onCardClick() {
    this.activityClick.emit(this.title());
  }
}
