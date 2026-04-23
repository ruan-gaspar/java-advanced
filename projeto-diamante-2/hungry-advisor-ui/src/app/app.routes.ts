
  import { UsersPage } from './pages/users/users.page';
  import { RestaurantsPage } from './pages/restaurants/restaurants.page';
  import { RecommendationPage } from './pages/recommendation/recommendation.page';

  import { Routes } from '@angular/router';

  export const routes: Routes = [
    { path: '', component: UsersPage },
    { path: 'restaurants', component: RestaurantsPage },
    { path: 'recommendation/:id', component: RecommendationPage }
  ];
