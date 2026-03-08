import { Component, signal, OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { SectionTitle } from './components/section-title/section-title';
import { Power } from './models/power.model';
import { PowerCard } from './components/power-card/power-card';
import { error } from 'console';
import { PowerService } from './services/power';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, SectionTitle, PowerCard],
  standalone: true,
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App implements OnInit {
  protected powers: Power[] = [];
  protected readonly title = signal('superpowers-hub');

  constructor(private powerService: PowerService) {}
    ngOnInit(): void {
        this.powerService.getPowers().subscribe(
          (data) => {
          this.powers = data;
          console.log('Power loaded:',this.powers);
    },
    (error) => {
      console.error('Error loading powers:', error);
    }
    );
  }
}
