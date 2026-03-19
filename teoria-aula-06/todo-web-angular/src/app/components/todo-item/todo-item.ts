import { Component, inject, input, model } from '@angular/core';
import { ApiService } from '../../service/api.service';

@Component({
  selector: 'app-todo-item',
  imports: [],
  templateUrl: './todo-item.html',
  styleUrl: './todo-item.css',
})
export class TodoItem {
  id = input.required<number>();
  description = input<string>();
  completed = model<boolean>();

  private apiService = inject(ApiService);

  onClick(){
    this.apiService.markTodoAsCompleted(this.id()).subscribe({
      next: () => this.completed.set(true),
      error: (err) => console.error('Error marking todo as completed:', err)
    })
  }
}
