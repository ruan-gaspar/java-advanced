import { Component, signal, inject } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { LucideAngularModule, FileIcon, GamepadIcon } from 'lucide-angular';
import { ApiService } from './services/api-service';
import { CardGame } from './components/card-game/card-game';

@Component({
  standalone: true,
  selector: 'app-root',
  imports: [
    RouterOutlet,
    LucideAngularModule,
    CardGame
  ],
  templateUrl: './app.html',
  styleUrls: ['./app.css']
})
export class App {

  private readonly api = inject(ApiService);

  gameInfo = signal<any>(null);

  //result = signal<string>('');

  checkGame(game: string) {

    console.log("Clicou no jogo: " + game)

    this.api.getRecommendation(game)
      .subscribe(r => {
        this.gameInfo.set(r);
      });
  }
}
