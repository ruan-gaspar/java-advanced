import { Component, input } from '@angular/core';

@Component({
  selector: 'app-section-title',
  imports: [],
  templateUrl: './section-title.html',
  styleUrls: ['./section-title.css'],
})
export class SectionTitle {
  label = input<string>();
}
