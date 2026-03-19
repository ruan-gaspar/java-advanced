import { Routes } from '@angular/router';
import { Home } from './home/home';
import { Form } from './form/form';
import { Login } from './login/login';
import { authGuard } from './auth-guard';

export const routes: Routes = [
  {path: 'home', component: Home, title: 'Todo | Home', canActivate: [authGuard]},
  {path: 'new', component: Form, title: 'Todo | Nova Tarefa', canActivate: [authGuard]},
  {path: '', component: Login, title: 'Todo | Login'}
];
