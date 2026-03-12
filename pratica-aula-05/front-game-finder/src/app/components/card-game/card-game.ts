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
  readonly image = input.required<string>();
  readonly icon = input.required<CardIcon>();

  readonly iconImg = computed(() => icons[this.icon()]);

  readonly activityClick = output<string>();

 label = computed(() => this.formatTitle(this.title()));

  formatTitle(name: string) {
    return name
    .replaceAll('-', ' ')
    .split(' ')
    .map(w => w.charAt(0).toUpperCase() + w.slice(1))
    .join(' ');
  }
  onCardClick() {
    this.activityClick.emit(this.title());

  }
}
