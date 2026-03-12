import { Component, signal, inject } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { LucideAngularModule, FileIcon, GamepadIcon } from 'lucide-angular';
import { ApiService } from './services/api-service';
import { CardGame } from './components/card-game/card-game';

@Component({
  standalone: true,
  selector: 'app-root',
  imports: [
    LucideAngularModule,
    CardGame
  ],
  templateUrl: './app.html',
  styleUrls: ['./app.css']
})
export class App {

  private readonly api = inject(ApiService);

  gameInfo = signal<any>(null);

  games = [
    { id: 'elden-ring', name: 'Elden Ring' },
    { id: 'cyberpunk-2077', name: 'Cyberpunk 2077' },
    { id: 'no-mans-sky', name: 'No Man\'s Sky' },
    { id: 'the-witcher-3-wild-hunt', name: 'The Witcher 3: Wild Hunt' },
    { id: 'red-dead-redemption-2', name: 'Red Dead Redemption 2' },
    { id: 'god-of-war', name: 'God of War' },
    { id: 'baldurs-gate-3', name: 'Baldur\'s Gate 3' },
    { id: 'hades', name: 'Hades' },
    { id: 'stardew-valley', name: 'Stardew Valley' },
    { id: 'grand-theft-auto-v', name: 'Grand Theft Auto V' }
  ];

  //result = signal<string>('');

  checkGame(game: string) {

    console.log("Clicou no jogo: " + game)

    this.api.getRecommendation(game)
      .subscribe(r => {
        this.gameInfo.set(r);
      });
  }
}
