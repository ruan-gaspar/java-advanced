import { Component } from '@angular/core';
import { ReactiveFormsModule, FormControl } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';


@Component({
  selector: 'app-form',
  imports: [ReactiveFormsModule, HttpClientModule],
  templateUrl: './form.html',
  styleUrl: './form.css',
})
export class Form {
  name = new FormControl('');

   constructor(private http: HttpClient) {}

  save() {
    const movie = {
      name: this.name.value,
      watched: false
    };
this.http.post('http://localhost:8080/api/to-watch', movie)
      .subscribe({
        next: (res) => {
          console.log("Filme cadastrado", res);
        },
        error: (err) => {
          console.error(err);
        }
        });
  }
}
