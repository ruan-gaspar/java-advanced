import { Component, inject } from '@angular/core';
import { ApiService } from '../../services/api.service';
import { Restaurant } from '../../models/restaurant.model';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-restaurants',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './restaurants.page.html'
})
export class RestaurantsPage {

  private api = inject(ApiService);

  restaurants: Restaurant[] = [];

  ngOnInit() {
    this.api.getRestaurants().subscribe(data => this.restaurants = data);
  }
}
