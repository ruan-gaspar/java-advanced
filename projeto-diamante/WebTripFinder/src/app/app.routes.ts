import { Routes } from '@angular/router';
import { LoginComponent } from './features/auth/pages/login/login.component';
import { RegisterComponent } from './features/auth/pages/register/register.component';
import { HomeComponent } from './features/auth/pages/home/home.component';
import { SearchComponent } from './features/auth/pages/search/search.component';
import { PlaceDetailComponent } from './features/auth/pages/place-detail/place-detail.component';
import { FavoritesComponent } from './features/auth/pages/favorites/favorites.component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'places/search', component: SearchComponent },
  { path: 'places/:id', component: PlaceDetailComponent },
  { path: 'favorites', component: FavoritesComponent },
  { path: '**', redirectTo: '' }
];
