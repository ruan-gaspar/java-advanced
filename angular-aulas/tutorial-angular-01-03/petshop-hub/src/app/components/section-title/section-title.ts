import { Component, input } from '@angular/core';

@Component({
  selector: 'app-section-title',
  template:`
    <h2 class="border-orange-500 border-l-4 m-4 pl-2 font-semibold">
    {{ label() }}
    </h2>
  `
})
export class SectionTitle {
label =input.required<string>();
}
