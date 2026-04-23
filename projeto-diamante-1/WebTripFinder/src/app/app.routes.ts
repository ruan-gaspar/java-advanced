import { Routes } from '@angular/router';
import { LoginComponent } from './features/auth/pages/login/login.component';
import { RegisterComponent } from './features/auth/pages/register/register.component';
import { HomeComponent } from './features/auth/pages/home/home.component';
import { PlaceDetailComponent } from './features/auth/pages/place-detail/place-detail.component';
import { FavoritesComponent } from './features/auth/pages/favorites/favorites.component';
import { authGuard } from './core/guards/auth.guard';
import { ProfileComponent } from './features/auth/pages/profile/profile.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },

  { path: '', component: HomeComponent, canActivate: [authGuard] },
  { path: 'places/:id', component: PlaceDetailComponent, canActivate: [authGuard] },
  { path: 'favorites', component: FavoritesComponent, canActivate: [authGuard] },
  { path: 'profile', component: ProfileComponent, canActivate: [authGuard] },
  { path: '**', redirectTo: '' }
];
