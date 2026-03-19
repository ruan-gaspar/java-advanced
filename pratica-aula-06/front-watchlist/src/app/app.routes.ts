import { Routes } from '@angular/router';
import { Home } from './home/home';
import { Form } from './form/form';

export const routes: Routes = [
  {path: 'home', component: Home, title: 'toWatch | Home'},
  {path: 'new', component: Form, title: 'toWatch | Novo Filme'},
];
