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
  {
    id: 'elden-ring',
    name: 'Elden Ring',
    image: 'https://cdn.cloudflare.steamstatic.com/steam/apps/1245620/header.jpg'
  },
  {
    id: 'cyberpunk-2077',
    name: 'Cyberpunk 2077',
    image: 'https://cdn.cloudflare.steamstatic.com/steam/apps/1091500/header.jpg'
  },
  {
    id: 'no-mans-sky',
    name: 'No Man\'s Sky',
    image: 'https://cdn.cloudflare.steamstatic.com/steam/apps/275850/header.jpg'
  },
  {
    id: 'the-witcher-3-wild-hunt',
    name: 'The Witcher 3: Wild Hunt',
    image: 'https://cdn.cloudflare.steamstatic.com/steam/apps/292030/header.jpg'
  },
  {
    id: 'red-dead-redemption-2',
    name: 'Red Dead Redemption 2',
    image: 'https://cdn.cloudflare.steamstatic.com/steam/apps/1174180/header.jpg'
  },
  {
    id: 'god-of-war',
    name: 'God of War',
    image: 'https://cdn.cloudflare.steamstatic.com/steam/apps/1593500/header.jpg'
  },
  {
    id: 'baldurs-gate-3',
    name: 'Baldur\'s Gate 3',
    image: 'https://cdn.cloudflare.steamstatic.com/steam/apps/1086940/header.jpg'
  },
  {
    id: 'hades',
    name: 'Hades',
    image: 'https://cdn.cloudflare.steamstatic.com/steam/apps/1145360/header.jpg'
  },
  {
    id: 'stardew-valley',
    name: 'Stardew Valley',
    image: 'https://cdn.cloudflare.steamstatic.com/steam/apps/413150/header.jpg'
  },
  {
    id: 'grand-theft-auto-v',
    name: 'Grand Theft Auto V',
    image: 'https://cdn.cloudflare.steamstatic.com/steam/apps/271590/header.jpg'
  },
  {
    id: 'hollow-knight',
    name: 'Hollow Knight',
    image: 'https://cdn.cloudflare.steamstatic.com/steam/apps/367520/header.jpg'
  },
  {
    id: 'sekiro-shadows-die-twice',
    name: 'Sekiro: Shadows Die Twice',
    image: 'https://cdn.cloudflare.steamstatic.com/steam/apps/814380/header.jpg'
  }
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
