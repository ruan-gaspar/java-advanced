import { Component, inject } from '@angular/core';
import { TodoItem } from '../components/todo-item/todo-item';
import { ApiService } from '../service/api.service';
import { toSignal } from '@angular/core/rxjs-interop';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [TodoItem],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home {
  private service = inject(ApiService);

  todos = toSignal (this.service.getTodos());

}
