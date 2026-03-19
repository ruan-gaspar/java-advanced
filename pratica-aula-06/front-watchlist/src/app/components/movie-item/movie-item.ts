import { Component } from '@angular/core';
import { input } from '@angular/core';

@Component({
  selector: 'app-movie-item',
  imports: [],
  templateUrl: './movie-item.html',
  styleUrl: './movie-item.css',
})
export class MovieItem {
  id = input<number>();
  name = input<string>()
  watched = input<boolean>();
}
