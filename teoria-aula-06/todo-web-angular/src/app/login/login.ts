import { Component, inject } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { ApiService } from '../service/api.service';
import { Router } from '@angular/router';
import { AuthService } from '../service/auth.service'


@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
  private apiService = inject(ApiService);
  private authService = inject(AuthService);
  private router = inject(Router);


 loginForm = new FormGroup({
    username: new FormControl('', {nonNullable: true}),
    password: new FormControl('', {nonNullable:true })
  })

  onSubmit() {
    this.apiService.login(
      this.loginForm.getRawValue().username,
      this.loginForm.getRawValue().password).subscribe({
        next: (response) => {
          this.authService.login(response.token);
          this.router.navigate(['/home']);
        },
        error: (error) => console.error('Login failed:', error)
      })

  }
}
