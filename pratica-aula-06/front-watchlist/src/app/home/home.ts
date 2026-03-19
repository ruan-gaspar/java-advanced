import { Component } from '@angular/core';
import { MovieItem } from '../components/movie-item/movie-item';
import { ApiService } from '../services/api-service';
import { inject } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';

@Component({
  selector: 'app-home',
  imports: [MovieItem],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home {
  private service = inject(ApiService);

  all = toSignal(this.service.getAll());
}
