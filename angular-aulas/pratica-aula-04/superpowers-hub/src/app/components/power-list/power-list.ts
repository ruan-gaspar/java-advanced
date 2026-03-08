import { Component } from '@angular/core';
import { PowerService } from '../../services/power';
import { inject } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';

@Component({
  selector: 'app-power-list',
  imports: [],
  templateUrl: './power-list.html',
  styleUrl: './power-list.css',
})
export class PowerList {
  private powerService = inject(PowerService);

  powers = toSignal(this.powerService.getPowers(), { initialValue: [] });
}

