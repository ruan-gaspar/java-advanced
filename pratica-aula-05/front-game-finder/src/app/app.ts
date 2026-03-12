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
  },
  {
  id: 'skyrim',
  name: 'The Elder Scrolls V: Skyrim',
  image: 'https://cdn.cloudflare.steamstatic.com/steam/apps/72850/capsule_616x353.jpg'
},
{
  id: 'god-of-war',
  name: 'God of War',
  image: 'https://cdn.cloudflare.steamstatic.com/steam/apps/1593500/capsule_616x353.jpg'
},
{
  id: 'baldurs-gate-3',
  name: 'Baldur\'s Gate 3',
  image: 'https://cdn.cloudflare.steamstatic.com/steam/apps/1086940/capsule_616x353.jpg'
},
{
  id: 'hades',
  name: 'Hades',
  image: 'https://cdn.cloudflare.steamstatic.com/steam/apps/1145360/capsule_616x353.jpg'
},
{
  id: 'stardew-valley',
  name: 'Stardew Valley',
  image: 'https://cdn.cloudflare.steamstatic.com/steam/apps/413150/capsule_616x353.jpg'
},
{
  id: 'gta-v',
  name: 'Grand Theft Auto V',
  image: 'https://cdn.cloudflare.steamstatic.com/steam/apps/271590/capsule_616x353.jpg'
},
{
  id: 'doom-eternal',
  name: 'DOOM Eternal',
  image: 'https://cdn.cloudflare.steamstatic.com/steam/apps/782330/capsule_616x353.jpg'
},
{
  id: 'monster-hunter-world',
  name: 'Monster Hunter World',
  image: 'https://cdn.cloudflare.steamstatic.com/steam/apps/582010/capsule_616x353.jpg'
},
{
  id: 'resident-evil-4-remake',
  name: 'Resident Evil 4',
  image: 'https://cdn.cloudflare.steamstatic.com/steam/apps/2050650/capsule_616x353.jpg'
},
{
  id: 'sekiro',
  name: 'Sekiro: Shadows Die Twice',
  image: 'https://cdn.cloudflare.steamstatic.com/steam/apps/814380/capsule_616x353.jpg'
},
{
  id: 'dark-souls-3',
  name: 'Dark Souls 3',
  image: 'https://cdn.cloudflare.steamstatic.com/steam/apps/374320/capsule_616x353.jpg'
},
{
  id: 'fallout-4',
  name: 'Fallout 4',
  image: 'https://cdn.cloudflare.steamstatic.com/steam/apps/377160/capsule_616x353.jpg'
},
{
  id: 'the-forest',
  name: 'The Forest',
  image: 'https://cdn.cloudflare.steamstatic.com/steam/apps/242760/capsule_616x353.jpg'
},
{
  id: 'sons-of-the-forest',
  name: 'Sons of the Forest',
  image: 'https://cdn.cloudflare.steamstatic.com/steam/apps/1326470/capsule_616x353.jpg'
},
{
  id: 'terraria',
  name: 'Terraria',
  image: 'https://cdn.cloudflare.steamstatic.com/steam/apps/105600/capsule_616x353.jpg'
},
{
  id: 'factorio',
  name: 'Factorio',
  image: 'https://cdn.cloudflare.steamstatic.com/steam/apps/427520/capsule_616x353.jpg'
},
{
  id: 'rimworld',
  name: 'RimWorld',
  image: 'https://cdn.cloudflare.steamstatic.com/steam/apps/294100/capsule_616x353.jpg'
},
{
  id: 'cities-skylines',
  name: 'Cities Skylines',
  image: 'https://cdn.cloudflare.steamstatic.com/steam/apps/255710/capsule_616x353.jpg'
},
{
  id: 'civilization-vi',
  name: 'Civilization VI',
  image: 'https://cdn.cloudflare.steamstatic.com/steam/apps/289070/capsule_616x353.jpg'
},
{
  id: 'xcom-2',
  name: 'XCOM 2',
  image: 'https://cdn.cloudflare.steamstatic.com/steam/apps/268500/capsule_616x353.jpg'
},
{
  id: 'dead-cells',
  name: 'Dead Cells',
  image: 'https://cdn.cloudflare.steamstatic.com/steam/apps/588650/capsule_616x353.jpg'
},
{
  id: 'hollow-knight',
  name: 'Hollow Knight',
  image: 'https://cdn.cloudflare.steamstatic.com/steam/apps/367520/capsule_616x353.jpg'
},
{
  id: 'celeste',
  name: 'Celeste',
  image: 'https://cdn.cloudflare.steamstatic.com/steam/apps/504230/capsule_616x353.jpg'
},
{
  id: 'cuphead',
  name: 'Cuphead',
  image: 'https://cdn.cloudflare.steamstatic.com/steam/apps/268910/capsule_616x353.jpg'
},
{
  id: 'ori-and-the-blind-forest',
  name: 'Ori and the Blind Forest',
  image: 'https://cdn.cloudflare.steamstatic.com/steam/apps/261570/capsule_616x353.jpg'
},
{
  id: 'ori-will-of-the-wisps',
  name: 'Ori and the Will of the Wisps',
  image: 'https://cdn.cloudflare.steamstatic.com/steam/apps/1057090/capsule_616x353.jpg'
},
{
  id: 'assassins-creed-valhalla',
  name: 'Assassin\'s Creed Valhalla',
  image: 'https://cdn.cloudflare.steamstatic.com/steam/apps/2208920/capsule_616x353.jpg'
},
{
  id: 'assassins-creed-odyssey',
  name: 'Assassin\'s Creed Odyssey',
  image: 'https://cdn.cloudflare.steamstatic.com/steam/apps/812140/capsule_616x353.jpg'
},
{
  id: 'watch-dogs-2',
  name: 'Watch Dogs 2',
  image: 'https://cdn.cloudflare.steamstatic.com/steam/apps/447040/capsule_616x353.jpg'
},
{
  id: 'far-cry-5',
  name: 'Far Cry 5',
  image: 'https://cdn.cloudflare.steamstatic.com/steam/apps/552520/capsule_616x353.jpg'
},
{
  id: 'far-cry-6',
  name: 'Far Cry 6',
  image: 'https://cdn.cloudflare.steamstatic.com/steam/apps/2369390/capsule_616x353.jpg'
},
{
  id: 'battlefield-1',
  name: 'Battlefield 1',
  image: 'https://cdn.cloudflare.steamstatic.com/steam/apps/1238840/capsule_616x353.jpg'
},
{
  id: 'battlefield-v',
  name: 'Battlefield V',
  image: 'https://cdn.cloudflare.steamstatic.com/steam/apps/1238810/capsule_616x353.jpg'
},
{
  id: 'apex-legends',
  name: 'Apex Legends',
  image: 'https://cdn.cloudflare.steamstatic.com/steam/apps/1172470/capsule_616x353.jpg'
},
{
  id: 'destiny-2',
  name: 'Destiny 2',
  image: 'https://cdn.cloudflare.steamstatic.com/steam/apps/1085660/capsule_616x353.jpg'
},
{
  id: 'warframe',
  name: 'Warframe',
  image: 'https://cdn.cloudflare.steamstatic.com/steam/apps/230410/capsule_616x353.jpg'
},
{
  id: 'noita',
  name: 'Noita',
  image: 'https://cdn.cloudflare.steamstatic.com/steam/apps/881100/capsule_616x353.jpg'
},
{
  id: 'project-zomboid',
  name: 'Project Zomboid',
  image: 'https://cdn.cloudflare.steamstatic.com/steam/apps/108600/capsule_616x353.jpg'
},
{
  id: 'subnautica',
  name: 'Subnautica',
  image: 'https://cdn.cloudflare.steamstatic.com/steam/apps/264710/capsule_616x353.jpg'
},
{
  id: 'subnautica-below-zero',
  name: 'Subnautica Below Zero',
  image: 'https://cdn.cloudflare.steamstatic.com/steam/apps/848450/capsule_616x353.jpg'
},
{
  id: 'outer-wilds',
  name: 'Outer Wilds',
  image: 'https://cdn.cloudflare.steamstatic.com/steam/apps/753640/capsule_616x353.jpg'
},
{
  id: 'control',
  name: 'Control',
  image: 'https://cdn.cloudflare.steamstatic.com/steam/apps/870780/capsule_616x353.jpg'
},
{
  id: 'alan-wake-2',
  name: 'Alan Wake 2',
  image: 'https://cdn.cloudflare.steamstatic.com/steam/apps/1087100/capsule_616x353.jpg'
},
{
  id: 'metro-exodus',
  name: 'Metro Exodus',
  image: 'https://cdn.cloudflare.steamstatic.com/steam/apps/412020/capsule_616x353.jpg'
},
{
  id: 'dishonored-2',
  name: 'Dishonored 2',
  image: 'https://cdn.cloudflare.steamstatic.com/steam/apps/403640/capsule_616x353.jpg'
},
{
  id: 'prey',
  name: 'Prey',
  image: 'https://cdn.cloudflare.steamstatic.com/steam/apps/480490/capsule_616x353.jpg'
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
