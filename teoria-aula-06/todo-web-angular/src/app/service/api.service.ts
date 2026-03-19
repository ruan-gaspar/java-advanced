import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { Observable } from "rxjs";

export interface Todo {
  id: number;
  description: string;
  completed: boolean;
}

export interface TodoPostRequest{
  description: string | null;
  completed: boolean | null;
}

@Injectable({providedIn: 'root'})
export class ApiService{
  private http = inject(HttpClient);
  private baseUrl = 'http://localhost:8080';

  getTodos(): Observable<Todo[]>   {
    return this.http.get<Todo[]>(this.baseUrl + '/todos')
  }

  postTodo(todo: TodoPostRequest): Observable<TodoPostRequest> {
    return this.http.post<TodoPostRequest>(this.baseUrl + '/todos', todo)
  }

  markTodoAsCompleted(id: number): Observable<Todo> {
    return this.http.put<Todo>(`${this.baseUrl}/todos/${id}`, {})
  }

  login(username: string | null, password: string | null): Observable<{ token: string }> {
    return this.http.post<{ token: string }>(`${this.baseUrl}/login`, { username, password })

  }
}
