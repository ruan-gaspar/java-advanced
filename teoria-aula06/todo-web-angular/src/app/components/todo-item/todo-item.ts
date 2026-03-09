import { Component,input } from '@angular/core';

@Component({
  selector: 'app-todo-item',
  standalone: true,
  imports: [],
  templateUrl: './todo-item.html',
  styleUrl: './todo-item.css',
})
export class TodoItem {
  id = input<number>();
  description = input<string>();
  completed = input<boolean>();
}
