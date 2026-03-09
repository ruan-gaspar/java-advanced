import { Routes } from '@angular/router';
import { Home } from './home/home';
import { Form } from './form/form';

export const routes: Routes = [
  {path: 'home', component: Home, title: 'Todo | Home'},
  {path: 'new', component: Form, title: 'Todo | Nova Tarefa'}
];
