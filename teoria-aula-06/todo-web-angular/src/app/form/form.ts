import { Component, inject } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { ApiService } from '../service/api.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-form',
  imports: [ReactiveFormsModule],
  templateUrl: './form.html',
  styleUrl: './form.css',
})
export class Form {
  private router = inject(Router);

  todoForm = new FormGroup({
    description: new FormControl(''),
    completed: new FormControl(false)
  })

  private apiService = inject(ApiService);

  onSubmit(){
    console.log('Form submitted with description:');

    this.apiService.postTodo(this.todoForm.getRawValue()).subscribe({
      next: () => this.router.navigate(['/home']),
      error: (err) => console.error('Error creating todo:', err)
    })
  }


}
